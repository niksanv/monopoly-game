package monopoly.view;

import javax.swing.*;
import monopoly.model.Board;
import monopoly.model.Dice;
import monopoly.model.Player;
import java.awt.Color;

/**
 * Панель игрового поля.
 * Содержит игровое поле, кубики и отображает игроков.
 */
public class GameBoardPanel extends JLayeredPane {
    /** Игровое поле */
    private Board board;

    /** Первый кубик */
    private Dice dice1;

    /** Второй кубик */
    private Dice dice2;

    /**
     * Создает панель игрового поля.
     */
    public GameBoardPanel() {
        setBorder(new javax.swing.border.LineBorder(Color.BLACK));
        setBounds(6, 6, 632, 630);
        setOpaque(true);

        initializeComponents();
    }

    /**
     * Инициализирует компоненты панели.
     */
    private void initializeComponents() {
        board = new Board(6, 6, 612, 612);
        board.setBackground(new Color(51, 255, 153));
        add(board, Integer.valueOf(0));

        dice1 = new Dice(244, 406, 40, 40);
        dice2 = new Dice(333, 406, 40, 40);
        add(dice1, Integer.valueOf(1));
        add(dice2, Integer.valueOf(1));
    }

    /**
     * Добавляет игрока на игровое поле.
     *
     * @param player объект игрока для добавления
     */
    public void addPlayer(Player player) {
        add(player, Integer.valueOf(1));
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
     * Возвращает первый кубик.
     *
     * @return первый кубик
     */
    public Dice getDice1() {
        return dice1;
    }

    /**
     * Возвращает второй кубик.
     *
     * @return второй кубик
     */
    public Dice getDice2() {
        return dice2;
    }

    /**
     * Обновляет отображение игрового поля.
     */
    public void updateBoard() {
        repaint();
    }
}