package monopoly.view;

import javax.swing.*;
import java.awt.*;

/**
 * Панель управления игрой.
 * Содержит кнопки для управления ходом игры.
 */
public class ControlPanel extends JPanel {
    /** Кнопка броска кубиков */
    private JButton btnRollDice;

    /** Кнопка перехода к следующему ходу */
    private JButton btnNextTurn;

    /** Кнопка покупки клетки */
    private JButton btnBuy;

    /** Кнопка оплаты аренды */
    private JButton btnPayRent;

    /** Кнопка включения/выключения автоматической игры */
    private JButton btnAutoGame;

    /**
     * Создает панель управления.
     */
    public ControlPanel() {
        setBackground(Color.LIGHT_GRAY);
        setBorder(new javax.swing.border.LineBorder(Color.BLACK));
        setBounds(634, 6, 419, 600);
        setLayout(null);

        initializeButtons();
    }

    /**
     * Инициализирует кнопки управления.
     */
    private void initializeButtons() {
        btnRollDice = createButton("Roll Dice", 81, 413, 246, 53);
        btnNextTurn = createButton("Next Turn", 81, 519, 246, 53);
        btnBuy = createButton("Buy", 81, 478, 117, 29);
        btnPayRent = createButton("Pay Rent", 210, 478, 117, 29);
        btnAutoGame = createButton("Auto Game", 140, 250, 117, 29);

        btnNextTurn.setEnabled(false);
        btnBuy.setEnabled(false);
        btnPayRent.setEnabled(false);

        add(btnRollDice);
        add(btnNextTurn);
        add(btnBuy);
        add(btnPayRent);
        add(btnAutoGame);
    }

    /**
     * Создает кнопку с указанными параметрами.
     *
     * @param text текст кнопки
     * @param x координата X
     * @param y координата Y
     * @param width ширина
     * @param height высота
     * @return созданная кнопка
     */
    private JButton createButton(String text, int x, int y, int width, int height) {
        JButton button = new JButton(text);
        button.setBounds(x, y, width, height);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        return button;
    }

    /**
     * Возвращает кнопку броска кубиков.
     *
     * @return кнопка Roll Dice
     */
    public JButton getBtnRollDice() { return btnRollDice; }

    /**
     * Возвращает кнопку перехода к следующему ходу.
     *
     * @return кнопка Next Turn
     */
    public JButton getBtnNextTurn() { return btnNextTurn; }

    /**
     * Возвращает кнопку покупки клетки.
     *
     * @return кнопка Buy
     */
    public JButton getBtnBuy() { return btnBuy; }

    /**
     * Возвращает кнопку оплаты аренды.
     *
     * @return кнопка Pay Rent
     */
    public JButton getBtnPayRent() { return btnPayRent; }

    /**
     * Возвращает кнопку автоматической игры.
     *
     * @return кнопка Auto Game
     */
    public JButton getBtnAutoGame() { return btnAutoGame; }

    /**
     * Обновляет состояния кнопок управления.
     *
     * @param rollEnabled доступность кнопки броска кубиков
     * @param nextEnabled доступность кнопки следующего хода
     * @param buyEnabled доступность кнопки покупки
     * @param rentEnabled доступность кнопки оплаты аренды
     */
    public void updateButtonStates(boolean rollEnabled, boolean nextEnabled,
                                   boolean buyEnabled, boolean rentEnabled) {
        btnRollDice.setEnabled(rollEnabled);
        btnNextTurn.setEnabled(nextEnabled);
        btnBuy.setEnabled(buyEnabled);
        btnPayRent.setEnabled(rentEnabled);
    }
}