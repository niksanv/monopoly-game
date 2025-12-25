package monopoly;

import javax.swing.SwingUtilities;
import monopoly.view.GameView;

/**
 * Главный класс приложения Monopoly.
 * Отвечает за запуск игры и инициализацию графического интерфейса.
 */
public class MonopolyMain {
    /**
     * Точка входа в приложение.
     * Настраивает внешний вид приложения и запускает главное окно.
     *
     * @param args аргументы командной строки (не используются)
     */
    public static void main(String[] args) {
        try {
            javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            GameView gameView = new GameView();
            gameView.setVisible(true);
        });
    }
}