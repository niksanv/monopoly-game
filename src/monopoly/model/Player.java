package monopoly.model;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.*;
import javax.swing.border.LineBorder;

/**
 * Класс игрока в Monopoly.
 * Представляет игрока с его свойствами, деньгами и собственностью.
 */
public class Player extends JPanel {
    /** Номер игрока */
    private int playerNumber;

    /** Метка с номером игрока */
    private JLabel lblPlayerNumber;

    /** Текущая позиция игрока на поле (индекс клетки 0-19) */
    private int currentSquareNumber = 0;

    /** Список индексов клеток, принадлежащих игроку */
    private ArrayList<Integer> titleDeeds = new ArrayList<>();

    /** Количество денег у игрока */
    private int wallet = 3200;

    /** Флаг банкротства игрока */
    private boolean isBankrupt = false;

    /** Количество пройденных кругов */
    private int lapsCompleted = 0;

    /** Общее количество созданных игроков */
    public static int totalPlayers = 0;

    /** Реестр собственности: индекс клетки -> номер владельца */
    public static HashMap<Integer, Integer> ledger = new HashMap<>();

    /** Базовые координаты клеток на поле */
    private static final int[][] SQUARE_BASE_COORDINATES = {
            {6, 106, 206, 306, 406, 506, 506, 506, 506, 506, 506, 406, 306, 206, 106, 6, 6, 6, 6, 6},
            {6, 6, 6, 6, 6, 6, 106, 206, 306, 406, 506, 506, 506, 506, 506, 506, 406, 306, 206, 106}
    };

    /** Смещения для позиционирования нескольких игроков на одной клетке */
    private static final int[][] PLAYER_OFFSETS = {
            {25, 25},
            {65, 25},
            {25, 65},
            {65, 65}
    };

    /**
     * Создает игрока с указанным номером и цветом.
     *
     * @param playerNumber номер игрока (1-4)
     * @param color цвет фишки игрока
     */
    public Player(int playerNumber, Color color) {
        this.playerNumber = playerNumber;
        this.setBackground(color);
        this.setOpaque(true);
        this.setBorder(new LineBorder(Color.BLACK, 1));

        lblPlayerNumber = new JLabel("" + playerNumber);
        lblPlayerNumber.setFont(new Font("Lucida Grande", Font.BOLD, 13));
        lblPlayerNumber.setForeground(Color.BLACK);
        lblPlayerNumber.setHorizontalAlignment(SwingConstants.CENTER);
        lblPlayerNumber.setVerticalAlignment(SwingConstants.CENTER);
        lblPlayerNumber.setBounds(0, 0, 20, 20);

        this.add(lblPlayerNumber);

        this.setBounds(0, 0, 20, 20);
        this.setLayout(null);
        totalPlayers++;
    }

    /**
     * Возвращает список индексов клеток, принадлежащих игроку.
     *
     * @return список индексов клеток собственности
     */
    public ArrayList<Integer> getTitleDeeds() {
        return titleDeeds;
    }

    /**
     * Возвращает количество денег у игрока.
     *
     * @return текущий баланс игрока
     */
    public int getWallet() {
        return wallet;
    }

    /**
     * Возвращает текущую позицию игрока на поле.
     *
     * @return индекс текущей клетки (0-19)
     */
    public int getCurrentSquareNumber() {
        return currentSquareNumber;
    }

    /**
     * Возвращает номер игрока.
     *
     * @return номер игрока
     */
    public int getPlayerNumber() {
        return playerNumber;
    }

    /**
     * Проверяет, является ли игрок банкротом.
     *
     * @return true если игрок банкрот, иначе false
     */
    public boolean isBankrupt() {
        return isBankrupt;
    }

    /**
     * Устанавливает статус банкротства игрока.
     * Если игрок становится банкротом, освобождает все его собственность.
     *
     * @param bankrupt true для объявления банкротом, false в противном случае
     */
    public void setBankrupt(boolean bankrupt) {
        this.isBankrupt = bankrupt;
        if (bankrupt) {
            // Освобождаем всю собственность игрока при банкротстве
            for (int deed : new ArrayList<>(titleDeeds)) {
                ledger.remove(deed);
            }
            titleDeeds.clear();
            setVisible(false);
        }
    }

    /**
     * Возвращает количество пройденных кругов.
     *
     * @return количество кругов
     */
    public int getLapsCompleted() {
        return lapsCompleted;
    }

    /**
     * Увеличивает счетчик пройденных кругов на 1.
     */
    public void incrementLap() {
        lapsCompleted++;
    }

    /**
     * Снимает указанную сумму с баланса игрока.
     * Если баланс становится <= 0, игрок объявляется банкротом.
     *
     * @param amount сумма для снятия
     */
    public void withdraw(int amount) {
        wallet -= amount;
        if (wallet < 0) {
            wallet = 0;
        }
        // Игрок становится банкротом, если у него нет денег
        if (wallet == 0) {
            setBankrupt(true);
        }
    }

    /**
     * Добавляет указанную сумму на баланс игрока.
     *
     * @param amount сумма для добавления
     */
    public void deposit(int amount) {
        wallet += amount;
    }

    /**
     * Проверяет, принадлежит ли указанная клетка игроку.
     *
     * @param squareNumber индекс клетки
     * @return true если клетка принадлежит игроку, иначе false
     */
    public boolean hasTitleDeed(int squareNumber) {
        return titleDeeds.contains(squareNumber);
    }

    /**
     * Покупает указанную клетку для игрока.
     *
     * @param squareNumber индекс покупаемой клетки
     */
    public void buyTitleDeed(int squareNumber) {
        if (!ledger.containsKey(squareNumber)) {
            titleDeeds.add(squareNumber);
            ledger.put(squareNumber, this.getPlayerNumber());
        }
    }

    /**
     * Перемещает игрока на указанное количество клеток.
     *
     * @param diceTotal сумма выпавших на кубиках очков
     */
    public void move(int diceTotal) {
        if (isBankrupt) return;

        boolean passedGo = false;
        if (currentSquareNumber + diceTotal > 19) {
            deposit(200);
            incrementLap();
            passedGo = true;
        }

        int targetSquare = (currentSquareNumber + diceTotal) % 20;
        currentSquareNumber = targetSquare;

        int baseX = SQUARE_BASE_COORDINATES[0][targetSquare];
        int baseY = SQUARE_BASE_COORDINATES[1][targetSquare];

        int playerIndex = Math.min(playerNumber - 1, PLAYER_OFFSETS.length - 1);
        int offsetX = PLAYER_OFFSETS[playerIndex][0];
        int offsetY = PLAYER_OFFSETS[playerIndex][1];

        this.setLocation(baseX + offsetX, baseY + offsetY);
    }

    /**
     * Отрисовывает компонент игрока.
     *
     * @param g графический контекст для отрисовки
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (!isOpaque()) {
            setOpaque(true);
        }
    }
}