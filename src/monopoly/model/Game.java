package monopoly.model;

import java.util.ArrayList;

/**
 * Класс, представляющий игровую сессию Monopoly.
 * Управляет состоянием игры, игроками и игровой логикой.
 */
public class Game {
    /** Игровое поле */
    private Board board;

    /** Список игроков */
    private ArrayList<Player> players;

    /** Флаги дублей для каждого игрока */
    private ArrayList<Boolean> doubleDiceFlags;

    /** Индекс текущего игрока */
    private int currentPlayerIndex = 0;

    /** Множитель аренды (прогрессивная система) */
    private int rentMultiplier = 1;

    /** Общее количество пройденных кругов всеми игроками */
    private int totalLaps = 0;

    /** Флаг завершения игры */
    private boolean gameEnded = false;

    /**
     * Создает новую игровую сессию с указанным количеством игроков.
     *
     * @param numberOfPlayers количество игроков (2-4)
     */
    public Game(int numberOfPlayers) {
        this.board = new Board(6, 6, 612, 612);
        this.players = new ArrayList<>();
        this.doubleDiceFlags = new ArrayList<>();

        initializePlayers(numberOfPlayers);
    }

    /**
     * Инициализирует игроков с заданным количеством.
     *
     * @param numberOfPlayers количество игроков
     */
    private void initializePlayers(int numberOfPlayers) {
        java.awt.Color[] playerColors = {
                java.awt.Color.PINK, java.awt.Color.CYAN,
                java.awt.Color.GREEN, java.awt.Color.YELLOW
        };

        for (int i = 0; i < numberOfPlayers; i++) {
            Player player = new Player(i + 1, playerColors[i]);
            players.add(player);
            doubleDiceFlags.add(false);
        }
    }

    /**
     * Возвращает игровое поле.
     *
     * @return объект игрового поля
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Возвращает список всех игроков.
     *
     * @return список игроков
     */
    public ArrayList<Player> getPlayers() {
        return players;
    }

    /**
     * Возвращает текущего игрока.
     *
     * @return объект текущего игрока
     */
    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    /**
     * Возвращает индекс текущего игрока.
     *
     * @return индекс текущего игрока (0-based)
     */
    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    /**
     * Устанавливает индекс текущего игрока.
     *
     * @param index новый индекс текущего игрока
     */
    public void setCurrentPlayerIndex(int index) {
        this.currentPlayerIndex = index;
    }

    /**
     * Проверяет, выпал ли дубль у указанного игрока.
     *
     * @param playerIndex индекс игрока
     * @return true если выпал дубль, иначе false
     */
    public boolean hasDoubleDice(int playerIndex) {
        return doubleDiceFlags.get(playerIndex);
    }

    /**
     * Устанавливает флаг дубля для указанного игрока.
     *
     * @param playerIndex индекс игрока
     * @param value значение флага
     */
    public void setDoubleDice(int playerIndex, boolean value) {
        doubleDiceFlags.set(playerIndex, value);
    }

    /**
     * Возвращает текущий множитель аренды.
     *
     * @return множитель аренды
     */
    public int getRentMultiplier() {
        return rentMultiplier;
    }

    /**
     * Устанавливает множитель аренды.
     *
     * @param multiplier новый множитель аренды
     */
    public void setRentMultiplier(int multiplier) {
        this.rentMultiplier = multiplier;
    }

    /**
     * Возвращает общее количество пройденных кругов.
     *
     * @return количество кругов
     */
    public int getTotalLaps() {
        return totalLaps;
    }

    /**
     * Обновляет счетчик общего количества кругов.
     */
    public void updateTotalLaps() {
        int laps = 0;
        for (Player player : players) {
            laps += player.getLapsCompleted();
        }
        this.totalLaps = laps;
    }

    /**
     * Проверяет, завершена ли игра.
     *
     * @return true если игра завершена, иначе false
     */
    public boolean isGameEnded() {
        return gameEnded;
    }

    /**
     * Устанавливает статус завершения игры.
     *
     * @param ended true для завершения игры, false в противном случае
     */
    public void setGameEnded(boolean ended) {
        this.gameEnded = ended;
    }

    /**
     * Рассчитывает стоимость аренды с учетом текущего множителя.
     *
     * @param baseRent базовая стоимость аренды
     * @return стоимость аренды с учетом множителя
     */
    public int calculateRent(int baseRent) {
        return baseRent * rentMultiplier;
    }

    /**
     * Переходит к следующему игроку, если у текущего не выпал дубль.
     */
    public void nextPlayer() {
        if (!doubleDiceFlags.get(currentPlayerIndex)) {
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        }
    }

    /**
     * Проверяет, завершилась ли игра.
     * Игра завершается, когда остается 1 или менее активных игроков.
     *
     * @return true если игра должна завершиться, иначе false
     */
    public boolean checkGameOver() {
        int activePlayers = 0;
        for (Player player : players) {
            if (!player.isBankrupt()) {
                activePlayers++;
            }
        }
        return activePlayers <= 1;
    }

    /**
     * Возвращает победителя игры.
     *
     * @return объект победителя или null если победителя нет
     */
    public Player getWinner() {
        for (Player player : players) {
            if (!player.isBankrupt()) {
                return player;
            }
        }
        return null;
    }
}