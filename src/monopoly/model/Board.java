package monopoly.model;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

/**
 * Класс игрового поля Monopoly.
 * Представляет собой игровую доску с клетками и отвечает за их отрисовку.
 */
public class Board extends JPanel {
    /** Список всех клеток на поле */
    private ArrayList<Square> allSquares = new ArrayList<>();

    /** Список клеток, которые нельзя купить (специальные клетки) */
    private ArrayList<Square> unbuyableSquares = new ArrayList<>();

    /**
     * Создает игровое поле с указанными параметрами.
     *
     * @param xCoord координата X верхнего левого угла поля
     * @param yCoord координата Y верхнего левого угла поля
     * @param width ширина поля
     * @param height высота поля
     */
    public Board(int xCoord, int yCoord, int width, int height) {
        setBorder(new LineBorder(Color.BLACK, 2));
        setBounds(xCoord, yCoord, width, height);
        setLayout(null);
        setOpaque(true);
        initializeSquares();
    }

    /**
     * Инициализирует клетки игрового поля.
     * Создает все клетки, настраивает их позиции и свойства.
     */
    private void initializeSquares() {
        String[] squareNames = {
                "Go", "Oriental Ave", "Community Chest", "Vermont Ave", "Connecticut Ave",
                "Roll once", "St. Charles Place", "Chance", "States Ave", "Virginia Ave",
                "Free Parking", "St. James Place", "Community Chest", "Tennessee Ave",
                "New York Ave", "Squeeze Play", "Pacific Ave", "North Carolina Ave",
                "Chance", "Pennsylvania Ave"
        };

        createAndConfigureSquares(squareNames);
        addMonopolyLabel();
        setDoubleBuffered(true);
    }

    /**
     * Создает и настраивает клетки на основе массива имен.
     *
     * @param names массив названий клеток
     */
    private void createAndConfigureSquares(String[] names) {
        int[][] positions = {
                {6, 6, 135}, {106, 6, 180}, {206, 6, 180}, {306, 6, 180}, {406, 6, 180},
                {506, 6, -135}, {506, 106, -90}, {506, 206, -90}, {506, 306, -90},
                {506, 406, -90}, {506, 506, -45}, {406, 506, 0}, {306, 506, 0},
                {206, 506, 0}, {106, 506, 0}, {6, 506, 45}, {6, 406, 90}, {6, 306, 90},
                {6, 206, 90}, {6, 106, 90}
        };

        int[] prices = {0, 100, 0, 100, 120, 0, 140, 0, 140, 160, 0, 180, 0, 180, 200, 0, 300, 300, 0, 320};
        int[] rents = {0, 6, 0, 6, 8, 0, 10, 0, 10, 12, 0, 14, 0, 14, 16, 0, 26, 26, 0, 28};

        for (int i = 0; i < names.length; i++) {
            Square square = new Square(
                    positions[i][0], positions[i][1], 100, 100,
                    names[i], positions[i][2]
            );

            if (prices[i] > 0) {
                square.setPrice(prices[i]);
                square.setRentPrice(rents[i]);
            } else {
                unbuyableSquares.add(square);
            }

            allSquares.add(square);
            add(square);
        }
    }

    /**
     * Добавляет центральную надпись "MONOPOLY" на игровое поле.
     */
    private void addMonopolyLabel() {
        JLabel lblMonopoly = new JLabel("MONOPOLY") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

                AffineTransform aT = g2.getTransform();
                Shape oldshape = g2.getClip();
                double x = getWidth() / 2.0;
                double y = getHeight() / 2.0;
                aT.rotate(Math.toRadians(-35), x, y);
                g2.setTransform(aT);
                g2.setClip(oldshape);
                super.paintComponent(g);
            }
        };

        lblMonopoly.setForeground(Color.WHITE);
        lblMonopoly.setBackground(Color.RED);
        lblMonopoly.setOpaque(true);
        lblMonopoly.setHorizontalAlignment(SwingConstants.CENTER);
        lblMonopoly.setFont(new Font("Lucida Grande", Font.BOLD, 40));
        lblMonopoly.setBounds(179, 277, 263, 55);
        add(lblMonopoly);
    }

    /**
     * Возвращает список клеток, которые нельзя купить.
     *
     * @return список непокупаемых клеток
     */
    public ArrayList<Square> getUnbuyableSquares() {
        return unbuyableSquares;
    }

    /**
     * Возвращает список всех клеток на поле.
     *
     * @return список всех клеток
     */
    public ArrayList<Square> getAllSquares() {
        return allSquares;
    }

    /**
     * Возвращает клетку по ее индексу на поле.
     *
     * @param location индекс клетки (0-19)
     * @return клетка по указанному индексу
     */
    public Square getSquareAtIndex(int location) {
        return allSquares.get(location);
    }

    /**
     * Отрисовывает фон игрового поля.
     *
     * @param g графический контекст для отрисовки
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(new Color(51, 255, 153));
        g.fillRect(0, 0, getWidth(), getHeight());
    }
}