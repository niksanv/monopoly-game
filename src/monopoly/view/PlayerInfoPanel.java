package monopoly.view;

import javax.swing.*;
import java.awt.*;
import monopoly.model.Player;
import monopoly.model.Board;

/**
 * Панель информации об игроках.
 * Отображает информацию о балансе, собственности и прогрессе каждого игрока.
 */
public class PlayerInfoPanel extends JPanel {
    /** Панель с информацией об игроках */
    private JPanel playerAssetsPanel;

    /** Менеджер компоновки для переключения между игроками */
    private java.awt.CardLayout cardLayout;

    /** Игровое поле для получения информации о клетках */
    private Board board;

    /**
     * Создает панель информации об игроках.
     *
     * @param board игровое поле для получения информации о клетках
     */
    public PlayerInfoPanel(Board board) {
        this.board = board;
        setBounds(81, 28, 246, 189);
        setLayout(new java.awt.CardLayout());

        playerAssetsPanel = this;
        cardLayout = (java.awt.CardLayout) getLayout();
    }

    /**
     * Инициализирует панели для каждого игрока.
     *
     * @param numberOfPlayers количество игроков
     */
    public void initializePlayerPanels(int numberOfPlayers) {
        java.awt.Color[] playerColors = {
                java.awt.Color.PINK, java.awt.Color.CYAN,
                java.awt.Color.GREEN, java.awt.Color.YELLOW
        };

        for (int i = 0; i < numberOfPlayers; i++) {
            JPanel panel = createPlayerPanel("Player " + (i + 1) + " All Wealth", playerColors[i]);
            playerAssetsPanel.add(panel, String.valueOf(i + 1));
        }

        showPlayerPanel(1);
    }

    /**
     * Создает панель для отображения информации об одном игроке.
     *
     * @param title заголовок панели
     * @param color фона панели
     * @return созданная панель игрока
     */
    private JPanel createPlayerPanel(String title, Color color) {
        JPanel panel = new JPanel();
        panel.setBackground(color);
        panel.setLayout(null);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBounds(0, 6, 240, 16);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(titleLabel);

        JTextArea textArea = createTextArea(10, 34, 230, 149);
        panel.add(textArea);

        return panel;
    }

    /**
     * Создает текстовую область для отображения информации.
     *
     * @param x координата X
     * @param y координата Y
     * @param width ширина
     * @param height высота
     * @return созданная текстовая область
     */
    private JTextArea createTextArea(int x, int y, int width, int height) {
        JTextArea textArea = new JTextArea();
        textArea.setBounds(x, y, width, height);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setFont(new Font("Arial", Font.PLAIN, 11));
        textArea.setBackground(Color.WHITE);
        return textArea;
    }

    /**
     * Обновляет информацию об указанном игроке.
     *
     * @param player объект игрока
     * @param playerIndex индекс игрока
     * @param rentMultiplier текущий множитель аренды
     */
    public void updatePlayerInfo(Player player, int playerIndex, int rentMultiplier) {
        Component[] components = playerAssetsPanel.getComponents();
        if (playerIndex < components.length) {
            JPanel panel = (JPanel) components[playerIndex];
            JTextArea textArea = (JTextArea) panel.getComponent(1);

            StringBuilder result = new StringBuilder();
            result.append("Current Balance: $").append(player.getWallet()).append("\n");
            result.append("Laps Completed: ").append(player.getLapsCompleted()).append("\n");
            result.append("Current Rent Multiplier: ").append(rentMultiplier).append("x\n");
            result.append("Title Deeds: \n");

            if (player.getTitleDeeds().isEmpty()) {
                result.append(" - None\n");
            } else {
                for (int deed : player.getTitleDeeds()) {
                    monopoly.model.Square square = board.getAllSquares().get(deed);
                    result.append(" - ").append(square.getName())
                            .append(" ($").append(square.getPrice())
                            .append(", Rent: $").append(square.getRentPrice() * rentMultiplier).append(")\n");
                }
            }

            textArea.setText(result.toString());
        }
    }

    /**
     * Показывает панель указанного игрока.
     *
     * @param playerNumber номер игрока (1-based)
     */
    public void showPlayerPanel(int playerNumber) {
        cardLayout.show(playerAssetsPanel, String.valueOf(playerNumber));
    }
}