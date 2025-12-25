package monopoly.view;

import javax.swing.*;
import java.awt.*;

/**
 * Консоль для вывода информации о ходе игры.
 * Отображает текущие события и сообщения для игроков.
 */
public class InfoConsole extends JPanel {
    /** Текстовая область для вывода информации */
    private JTextArea textArea;

    /**
     * Создает консоль информации.
     */
    public InfoConsole() {
        setBounds(81, 312, 246, 68);
        setLayout(null);
        setBackground(Color.WHITE);

        textArea = new JTextArea();
        textArea.setBounds(6, 6, 234, 56);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);
        textArea.setFont(new Font("Arial", Font.PLAIN, 12));
        textArea.setText("Game started. Select number of players.");

        add(textArea);
    }

    /**
     * Устанавливает текст в консоли.
     *
     * @param text текст для отображения
     */
    public void setText(String text) {
        textArea.setText(text);
    }

    /**
     * Добавляет текст в консоль.
     *
     * @param text текст для добавления
     */
    public void appendText(String text) {
        textArea.append("\n" + text);
    }

    /**
     * Очищает консоль.
     */
    public void clear() {
        textArea.setText("");
    }

    /**
     * Возвращает текущий текст консоли.
     *
     * @return текущий текст
     */
    public String getText() {
        return textArea.getText();
    }
}