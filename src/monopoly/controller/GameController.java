package monopoly.controller;

import javax.swing.*;
import monopoly.view.GameView;
import monopoly.view.GameBoardPanel;
import monopoly.view.ControlPanel;
import monopoly.view.InfoConsole;
import monopoly.model.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * Контроллер игры Monopoly.
 * Управляет игровой логикой, обработкой событий и взаимодействием между моделью и представлением.
 */
public class GameController {
    /** Главное окно игры */
    private GameView view;

    /** Модель игры */
    private Game game;

    /** Таймер для автоматической игры */
    private Timer autoGameTimer;

    /** Флаг автоматического режима игры */
    private boolean autoGameMode = false;

    /** Флаг работы автоматической игры */
    private boolean isAutoGameRunning = false;

    /** Флаг приостановки игры для показа диалога */
    private boolean gamePaused = false;

    /**
     * Создает контроллер игры.
     *
     * @param view главное окно игры
     * @param gameBoardPanel панель игрового поля
     * @param controlPanel панель управления
     * @param infoConsole консоль информации
     */
    public GameController(GameView view, GameBoardPanel gameBoardPanel,
                          ControlPanel controlPanel, InfoConsole infoConsole) {
        this.view = view;
    }

    /**
     * Инициализирует игру.
     * Запрашивает количество игроков и настраивает начальное состояние.
     */
    public void initializeGame() {
        int numberOfPlayers = showPlayerSelectionDialog();
        if (numberOfPlayers == 0) {
            System.exit(0);
        }

        game = new Game(numberOfPlayers);
        view.initializePlayerInfo(game.getBoard(), numberOfPlayers);

        setupEventListeners();
        addPlayersToBoard();
        updateInfo("Player 1 starts the game by clicking Roll Dice!\nPlayers: " + numberOfPlayers + " | Rent Multiplier: 1x");
    }

    /**
     * Показывает диалог выбора количества игроков.
     *
     * @return выбранное количество игроков или 0 если выбор отменен
     */
    private int showPlayerSelectionDialog() {
        String[] options = {"2 players", "3 players", "4 players"};
        String choice = (String) JOptionPane.showInputDialog(
                null,
                "Select number of players:",
                "Monopoly Game Setup",
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );

        return choice != null ? Integer.parseInt(choice.substring(0, 1)) : 0;
    }

    /**
     * Настраивает обработчики событий для кнопок управления.
     */
    private void setupEventListeners() {
        view.getControlPanel().getBtnRollDice().addActionListener(new RollDiceListener());
        view.getControlPanel().getBtnNextTurn().addActionListener(new NextTurnListener());
        view.getControlPanel().getBtnBuy().addActionListener(new BuyListener());
        view.getControlPanel().getBtnPayRent().addActionListener(new PayRentListener());
        view.getControlPanel().getBtnAutoGame().addActionListener(new AutoGameListener());
    }

    /**
     * Добавляет игроков на игровое поле.
     */
    private void addPlayersToBoard() {
        for (Player player : game.getPlayers()) {
            view.getGameBoardPanel().addPlayer(player);
        }
    }

    /**
     * Обновляет текст в консоли информации.
     *
     * @param text текст для отображения
     */
    private void updateInfo(String text) {
        view.getInfoConsole().setText(text);
    }

    /**
     * Обновляет информацию обо всех игроках.
     */
    private void updateAllPlayerInfo() {
        for (int i = 0; i < game.getPlayers().size(); i++) {
            view.getPlayerInfoPanel().updatePlayerInfo(
                    game.getPlayers().get(i), i, game.getRentMultiplier()
            );
        }
    }

    /**
     * Показывает панель текущего игрока.
     */
    private void showCurrentPlayerPanel() {
        view.getPlayerInfoPanel().showPlayerPanel(game.getCurrentPlayerIndex() + 1);
    }

    /**
     * Переходит к следующему небанкротному игроку.
     */
    private void moveToNextNonBankruptPlayer() {
        int startIndex = game.getCurrentPlayerIndex();
        int nextIndex = (game.getCurrentPlayerIndex() + 1) % game.getPlayers().size();

        // Ищем следующего небанкротного игрока
        int attempts = 0;
        while (game.getPlayers().get(nextIndex).isBankrupt() && attempts < game.getPlayers().size()) {
            nextIndex = (nextIndex + 1) % game.getPlayers().size();
            attempts++;
        }

        game.setCurrentPlayerIndex(nextIndex);
    }

    /**
     * Обновляет множитель аренды на основе пройденных кругов.
     */
    private void updateRentMultiplier() {
        int totalLaps = 0;
        for (Player player : game.getPlayers()) {
            totalLaps += player.getLapsCompleted();
        }

        if (totalLaps > game.getTotalLaps()) {
            game.updateTotalLaps();

            if (totalLaps % 2 == 0) {
                game.setRentMultiplier(game.getRentMultiplier() * 2);
                view.getInfoConsole().appendText("\nRent doubled! New multiplier: " + game.getRentMultiplier() + "x");
            }
        }
    }

    /**
     * Показывает диалоговое окно при выбытии игрока.
     * Приостанавливает автоматическую игру, если она активна.
     *
     * @param playerNumber номер выбывшего игрока
     */
    private void showPlayerEliminatedDialog(int playerNumber) {
        // Приостанавливаем автоигру, если она активна
        boolean wasAutoGame = autoGameMode;
        if (wasAutoGame) {
            pauseAutoGame();
        }

        // Создаем кастомное диалоговое окно
        JDialog dialog = new JDialog(view, "Player Eliminated", true);
        dialog.setLayout(new java.awt.BorderLayout());
        dialog.setSize(300, 150);
        dialog.setLocationRelativeTo(view);

        // Создаем панель с сообщением
        JPanel messagePanel = new JPanel();
        messagePanel.setLayout(new java.awt.BorderLayout());
        messagePanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel messageLabel = new JLabel(
                "<html><center><b>Player " + playerNumber + " has been eliminated!</b><br><br>" +
                        "The player has gone bankrupt and is out of the game.</center></html>",
                JLabel.CENTER
        );
        messageLabel.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 14));
        messagePanel.add(messageLabel, java.awt.BorderLayout.CENTER);

        // Создаем панель с кнопкой OK
        JPanel buttonPanel = new JPanel();
        JButton okButton = new JButton("Continue Game");
        okButton.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 12));
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
                // Возобновляем автоигру, если она была активна
                if (wasAutoGame) {
                    resumeAutoGame();
                }
            }
        });
        buttonPanel.add(okButton);

        // Добавляем компоненты в диалог
        dialog.add(messagePanel, java.awt.BorderLayout.CENTER);
        dialog.add(buttonPanel, java.awt.BorderLayout.SOUTH);

        // Делаем диалог модальным (блокирующим)
        dialog.setModal(true);
        dialog.setVisible(true);
    }

    /**
     * Приостанавливает автоматическую игру.
     */
    private void pauseAutoGame() {
        if (autoGameMode && autoGameTimer != null && autoGameTimer.isRunning()) {
            autoGameTimer.stop();
            gamePaused = true;
            view.getControlPanel().getBtnAutoGame().setText("Auto Game (Paused)");
        }
    }

    /**
     * Возобновляет автоматическую игру.
     */
    private void resumeAutoGame() {
        if (autoGameMode && gamePaused && autoGameTimer != null) {
            autoGameTimer.start();
            gamePaused = false;
            view.getControlPanel().getBtnAutoGame().setText("Stop Auto");
        }
    }

    /**
     * Проверяет условия завершения игры и обрабатывает окончание.
     */
    private void checkGameEnd() {
        if (game.isGameEnded()) return;

        if (game.checkGameOver()) {
            game.setGameEnded(true);
            Player winner = game.getWinner();

            if (winner != null) {
                updateInfo("GAME OVER! Player " + winner.getPlayerNumber() + " wins!");
                // Показываем финальное диалоговое окно
                showGameOverDialog(winner);
            } else {
                updateInfo("GAME OVER! All players are bankrupt!");
                // Показываем диалог о ничьей
                showDrawDialog();
            }

            view.getControlPanel().updateButtonStates(false, false, false, false);
            view.getControlPanel().getBtnAutoGame().setEnabled(false);

            if (autoGameMode && autoGameTimer != null) {
                autoGameTimer.stop();
                autoGameMode = false;
                view.getControlPanel().getBtnAutoGame().setText("Auto Game");
            }
        }
    }

    /**
     * Показывает диалоговое окно при победе игрока.
     *
     * @param winner победивший игрок
     */
    private void showGameOverDialog(Player winner) {
        // Приостанавливаем автоигру, если она активна
        boolean wasAutoGame = autoGameMode;
        if (wasAutoGame) {
            pauseAutoGame();
        }

        String message = "<html><center><b>GAME OVER!</b><br><br>" +
                "<font size='+1'>Player " + winner.getPlayerNumber() + " wins!</font><br><br>" +
                "Final Rent Multiplier: " + game.getRentMultiplier() + "x<br>" +
                "Total Laps Completed: " + game.getTotalLaps() + "</center></html>";

        JOptionPane.showMessageDialog(view,
                message,
                "Game Over",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Показывает диалоговое окно при ничьей.
     */
    private void showDrawDialog() {
        // Приостанавливаем автоигру, если она активна
        boolean wasAutoGame = autoGameMode;
        if (wasAutoGame) {
            pauseAutoGame();
        }

        String message = "<html><center><b>GAME OVER!</b><br><br>" +
                "<font size='+1'>All players are bankrupt!</font><br><br>" +
                "The game ends in a draw.</center></html>";

        JOptionPane.showMessageDialog(view,
                message,
                "Game Over",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Обработчик события броска кубиков.
     */
    private class RollDiceListener implements ActionListener {
        /**
         * Обрабатывает бросок кубиков и перемещение игрока.
         *
         * @param e событие нажатия кнопки
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            Player currentPlayer = game.getCurrentPlayer();

            // Проверяем, не банкрот ли текущий игрок
            if (currentPlayer.isBankrupt()) {
                updateInfo("Player " + (game.getCurrentPlayerIndex() + 1) + " is bankrupt and cannot play!");
                return;
            }

            Dice dice1 = view.getGameBoardPanel().getDice1();
            Dice dice2 = view.getGameBoardPanel().getDice2();

            dice1.roll();
            dice2.roll();
            int diceTotal = dice1.getFaceValue() + dice2.getFaceValue();

            if (dice1.getFaceValue() == dice2.getFaceValue()) {
                game.setDoubleDice(game.getCurrentPlayerIndex(), true);
                updateInfo("Double! Player " + (game.getCurrentPlayerIndex() + 1) + " gets another turn!");
            } else {
                game.setDoubleDice(game.getCurrentPlayerIndex(), false);
            }

            currentPlayer.move(diceTotal);
            int currentSquare = currentPlayer.getCurrentSquareNumber();
            Square square = game.getBoard().getAllSquares().get(currentSquare);

            updateRentMultiplier();

            if (square.getPrice() > 0) {
                updateInfo("Player " + (game.getCurrentPlayerIndex() + 1) + " landed on " + square.getName() +
                        " (Price: $" + square.getPrice() +
                        ", Base Rent: $" + square.getRentPrice() +
                        ", Current Rent: $" + game.calculateRent(square.getRentPrice()) +
                        ", Multiplier: " + game.getRentMultiplier() + "x)");
            } else {
                updateInfo("Player " + (game.getCurrentPlayerIndex() + 1) + " landed on " + square.getName());
            }

            checkSquareActions(currentPlayer, currentSquare);
            view.getGameBoardPanel().updateBoard();
            view.getControlPanel().getBtnRollDice().setEnabled(false);
            updateAllPlayerInfo();
            checkGameEnd();
        }

        /**
         * Проверяет возможные действия на текущей клетке и обновляет состояние кнопок.
         *
         * @param currentPlayer текущий игрок
         * @param squareNumber номер текущей клетки
         */
        private void checkSquareActions(Player currentPlayer, int squareNumber) {
            if (Player.ledger.containsKey(squareNumber)) {
                int ownerNumber = Player.ledger.get(squareNumber);

                if (ownerNumber != currentPlayer.getPlayerNumber()) {
                    view.getControlPanel().updateButtonStates(false, false, false, true);
                } else {
                    view.getControlPanel().updateButtonStates(false, true, false, false);
                }
            } else if (game.getBoard().getUnbuyableSquares().contains(
                    game.getBoard().getAllSquares().get(squareNumber))) {
                view.getControlPanel().updateButtonStates(false, true, false, false);
            } else {
                view.getControlPanel().updateButtonStates(false, true, true, false);
            }
        }
    }

    /**
     * Обработчик события покупки клетки.
     */
    private class BuyListener implements ActionListener {
        /**
         * Обрабатывает покупку клетки текущим игроком.
         *
         * @param e событие нажатия кнопки
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            Player currentPlayer = game.getCurrentPlayer();
            int currentSquare = currentPlayer.getCurrentSquareNumber();
            Square square = game.getBoard().getAllSquares().get(currentSquare);

            if (currentPlayer.getWallet() >= square.getPrice()) {
                updateInfo("Player " + (game.getCurrentPlayerIndex() + 1) + " bought " + square.getName() +
                        " for $" + square.getPrice() +
                        "\nBase Rent: $" + square.getRentPrice() +
                        ", Current Rent: $" + game.calculateRent(square.getRentPrice()));
                currentPlayer.buyTitleDeed(currentSquare);
                currentPlayer.withdraw(square.getPrice());

                // Проверяем, не стал ли игрок банкротом после покупки
                if (currentPlayer.isBankrupt()) {
                    updateInfo("Player " + (game.getCurrentPlayerIndex() + 1) + " went bankrupt!");
                    // Показываем диалог о выбытии игрока
                    showPlayerEliminatedDialog(game.getCurrentPlayerIndex() + 1);
                    // Переходим к следующему небанкротному игроку
                    moveToNextNonBankruptPlayer();
                    view.getPlayerInfoPanel().showPlayerPanel(game.getCurrentPlayerIndex() + 1);
                    view.getControlPanel().updateButtonStates(true, false, false, false);
                    updateInfo("Turn passed to Player " + (game.getCurrentPlayerIndex() + 1));
                }
            } else {
                updateInfo("Player " + (game.getCurrentPlayerIndex() + 1) + " cannot afford " + square.getName());
            }

            view.getControlPanel().getBtnBuy().setEnabled(false);
            updateAllPlayerInfo();
            view.getGameBoardPanel().updateBoard();
            checkGameEnd();
        }
    }

    /**
     * Обработчик события оплаты аренды.
     */
    private class PayRentListener implements ActionListener {
        /**
         * Обрабатывает оплату аренды за посещение чужой клетки.
         *
         * @param e событие нажатия кнопки
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            Player currentPlayer = game.getCurrentPlayer();
            int currentSquare = currentPlayer.getCurrentSquareNumber();

            int ownerNumber = Player.ledger.get(currentSquare);
            Player owner = game.getPlayers().get(ownerNumber - 1);

            int baseRent = game.getBoard().getAllSquares().get(currentSquare).getRentPrice();
            int rentAmount = game.calculateRent(baseRent);

            updateInfo("Player " + (game.getCurrentPlayerIndex() + 1) + " paid $" + rentAmount +
                    " rent to Player " + ownerNumber +
                    " (base: $" + baseRent + ", multiplier: " + game.getRentMultiplier() + "x)");

            currentPlayer.withdraw(rentAmount);
            owner.deposit(rentAmount);

            // Проверяем, не стал ли игрок банкротом после оплаты
            if (currentPlayer.isBankrupt()) {
                updateInfo("Player " + (game.getCurrentPlayerIndex() + 1) + " went bankrupt!");
                // Показываем диалог о выбытии игрока
                showPlayerEliminatedDialog(game.getCurrentPlayerIndex() + 1);
                // Переходим к следующему небанкротному игроку
                moveToNextNonBankruptPlayer();
                view.getPlayerInfoPanel().showPlayerPanel(game.getCurrentPlayerIndex() + 1);
                view.getControlPanel().updateButtonStates(true, false, false, false);
                updateInfo("Turn passed to Player " + (game.getCurrentPlayerIndex() + 1));
            } else {
                view.getControlPanel().updateButtonStates(false, true, false, false);
            }

            updateAllPlayerInfo();
            view.getGameBoardPanel().updateBoard();
            checkGameEnd();
        }
    }

    /**
     * Обработчик события перехода к следующему ходу.
     */
    private class NextTurnListener implements ActionListener {
        /**
         * Обрабатывает переход к следующему ходу.
         *
         * @param e событие нажатия кнопки
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            view.getControlPanel().updateButtonStates(true, false, false, false);

            if (game.hasDoubleDice(game.getCurrentPlayerIndex())) {
                // Если выпал дубль, тот же игрок ходит снова
                game.setDoubleDice(game.getCurrentPlayerIndex(), false);
                updateInfo("Player " + (game.getCurrentPlayerIndex() + 1) + " gets another turn (double dice)!");
            } else {
                // Переходим к следующему небанкротному игроку
                moveToNextNonBankruptPlayer();
                updateInfo("It's Player " + (game.getCurrentPlayerIndex() + 1) + "'s turn!\n" +
                        "Current Rent Multiplier: " + game.getRentMultiplier() + "x");
            }

            // Проверяем, не банкрот ли текущий игрок (на случай, если все игроки банкроты)
            if (game.getCurrentPlayer().isBankrupt()) {
                updateInfo("All remaining players are bankrupt! Game over.");
                checkGameEnd();
                return;
            }

            view.getPlayerInfoPanel().showPlayerPanel(game.getCurrentPlayerIndex() + 1);

            updateAllPlayerInfo();
            view.getGameBoardPanel().updateBoard();
            checkGameEnd();
        }
    }

    /**
     * Обработчик события включения/выключения автоматической игры.
     */
    private class AutoGameListener implements ActionListener {
        /**
         * Обрабатывает включение или выключение автоматического режима игры.
         *
         * @param e событие нажатия кнопки
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!autoGameMode) {
                autoGameMode = true;
                isAutoGameRunning = true;
                gamePaused = false;
                view.getControlPanel().getBtnAutoGame().setText("Stop Auto");
                updateInfo("Auto Game started! Game will play automatically...\n" +
                        "Current Rent Multiplier: " + game.getRentMultiplier() + "x");

                view.getControlPanel().updateButtonStates(false, false, false, false);

                autoGameTimer = new Timer(300, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (autoGameMode && isAutoGameRunning && !gamePaused) {
                            performAutoMove();
                        } else {
                            ((Timer) e.getSource()).stop();
                        }
                    }
                });
                autoGameTimer.start();

            } else {
                autoGameMode = false;
                isAutoGameRunning = false;
                gamePaused = false;
                view.getControlPanel().getBtnAutoGame().setText("Auto Game");
                updateInfo("Auto Game stopped.\n" +
                        "Current Rent Multiplier: " + game.getRentMultiplier() + "x");

                view.getControlPanel().getBtnRollDice().setEnabled(true);
                view.getControlPanel().getBtnNextTurn().setEnabled(false);

                if (autoGameTimer != null) {
                    autoGameTimer.stop();
                }
            }
        }

        /**
         * Выполняет один автоматический ход.
         */
        private void performAutoMove() {
            if (game.isGameEnded() || !autoGameMode || gamePaused) {
                if (autoGameTimer != null) {
                    autoGameTimer.stop();
                }
                return;
            }

            Player currentPlayer = game.getCurrentPlayer();

            // Пропускаем ход банкрота
            if (currentPlayer.isBankrupt()) {
                moveToNextNonBankruptPlayer();
                view.getPlayerInfoPanel().showPlayerPanel(game.getCurrentPlayerIndex() + 1);
                updateInfo("Auto Game: Player " + (game.getCurrentPlayerIndex() + 1) + "'s turn (other player bankrupt)");
                return;
            }

            // Симулируем задержку для реалистичности
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Dice dice1 = view.getGameBoardPanel().getDice1();
            Dice dice2 = view.getGameBoardPanel().getDice2();

            dice1.roll();
            dice2.roll();
            int diceTotal = dice1.getFaceValue() + dice2.getFaceValue();

            if (dice1.getFaceValue() == dice2.getFaceValue()) {
                game.setDoubleDice(game.getCurrentPlayerIndex(), true);
            } else {
                game.setDoubleDice(game.getCurrentPlayerIndex(), false);
            }

            currentPlayer.move(diceTotal);
            int currentSquare = currentPlayer.getCurrentSquareNumber();
            Square square = game.getBoard().getAllSquares().get(currentSquare);

            updateRentMultiplier();

            if (square.getPrice() > 0) {
                updateInfo("Auto Game: Player " + (game.getCurrentPlayerIndex() + 1) + " landed on " + square.getName() +
                        " (Price: $" + square.getPrice() +
                        ", Base Rent: $" + square.getRentPrice() +
                        ", Current Rent: $" + game.calculateRent(square.getRentPrice()) +
                        ", Multiplier: " + game.getRentMultiplier() + "x)");
            } else {
                updateInfo("Auto Game: Player " + (game.getCurrentPlayerIndex() + 1) + " landed on " + square.getName());
            }

            // Проверяем, что можно сделать на этой клетке
            if (Player.ledger.containsKey(currentSquare)) {
                int ownerNumber = Player.ledger.get(currentSquare);

                if (ownerNumber != currentPlayer.getPlayerNumber()) {
                    // Оплачиваем аренду автоматически с учетом множителя
                    Player owner = game.getPlayers().get(ownerNumber - 1);
                    int baseRent = square.getRentPrice();
                    int rentAmount = game.calculateRent(baseRent);

                    updateInfo("Auto Game: Player " + (game.getCurrentPlayerIndex() + 1) + " pays $" + rentAmount +
                            " rent to Player " + ownerNumber +
                            " (base: $" + baseRent + ", multiplier: " + game.getRentMultiplier() + "x)");

                    currentPlayer.withdraw(rentAmount);
                    owner.deposit(rentAmount);

                    // Проверяем, не стал ли игрок банкротом
                    if (currentPlayer.isBankrupt()) {
                        updateInfo("Auto Game: Player " + (game.getCurrentPlayerIndex() + 1) + " went bankrupt!");
                        // Показываем диалог о выбытии игрока
                        showPlayerEliminatedDialog(game.getCurrentPlayerIndex() + 1);
                        moveToNextNonBankruptPlayer();
                    }
                }
            } else if (!game.getBoard().getUnbuyableSquares().contains(square) && square.getPrice() > 0) {
                // Автоматически покупаем, если хватает денег
                if (currentPlayer.getWallet() >= square.getPrice()) {
                    updateInfo("Auto Game: Player " + (game.getCurrentPlayerIndex() + 1) + " buys " + square.getName() +
                            " for $" + square.getPrice());
                    currentPlayer.buyTitleDeed(currentSquare);
                    currentPlayer.withdraw(square.getPrice());

                    // Проверяем, не стал ли игрок банкротом после покупки
                    if (currentPlayer.isBankrupt()) {
                        updateInfo("Auto Game: Player " + (game.getCurrentPlayerIndex() + 1) + " went bankrupt!");
                        // Показываем диалог о выбытии игрока
                        showPlayerEliminatedDialog(game.getCurrentPlayerIndex() + 1);
                        moveToNextNonBankruptPlayer();
                    }
                } else {
                    updateInfo("Auto Game: Player " + (game.getCurrentPlayerIndex() + 1) + " cannot afford " + square.getName());
                }
            }

            // Переходим к следующему ходу, если не выпал дубль
            if (!game.hasDoubleDice(game.getCurrentPlayerIndex())) {
                moveToNextNonBankruptPlayer();
            }

            view.getPlayerInfoPanel().showPlayerPanel(game.getCurrentPlayerIndex() + 1);

            updateAllPlayerInfo();
            view.getGameBoardPanel().updateBoard();
            checkGameEnd();
        }
    }
}