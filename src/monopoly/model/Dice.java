package monopoly.model;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

/**
 * Класс игрального кубика.
 * Отображает случайное значение от 1 до 6 и может быть "брошен".
 */
public class Dice extends JPanel {
    /** Генератор случайных чисел для броска кубика */
    private Random random = new Random();

    /** Текущее значение на грани кубика (1-6) */
    private int faceValue = 1;

    /** Пропорциональные координаты точек для каждого значения кубика */
    private static final double[][][] DICE_POINTS_PROPORTIONAL = {
            {{0.5, 0.5}},
            {{0.25, 0.25}, {0.75, 0.75}},
            {{0.25, 0.25}, {0.5, 0.5}, {0.75, 0.75}},
            {{0.25, 0.25}, {0.75, 0.25}, {0.25, 0.75}, {0.75, 0.75}},
            {{0.25, 0.25}, {0.75, 0.25}, {0.5, 0.5}, {0.25, 0.75}, {0.75, 0.75}},
            {{0.25, 0.25}, {0.75, 0.25}, {0.25, 0.5}, {0.75, 0.5}, {0.25, 0.75}, {0.75, 0.75}}
    };

    /** Отношение размера точки к размеру кубика */
    private static final double POINT_SIZE_RATIO = 0.15;

    /**
     * Создает кубик с указанными параметрами.
     *
     * @param xCoord координата X верхнего левого угла кубика
     * @param yCoord координата Y верхнего левого угла кубика
     * @param width ширина кубика
     * @param height высота кубика
     */
    public Dice(int xCoord, int yCoord, int width, int height) {
        setBorder(new LineBorder(Color.BLACK, 1));
        setBounds(xCoord, yCoord, width, height);
        setOpaque(true);
        setBackground(Color.WHITE);
        setDoubleBuffered(true);
    }

    /**
     * Отрисовывает кубик с его текущим значением.
     *
     * @param g графический контекст для отрисовки
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());

        g.setColor(Color.BLACK);
        g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);

        g.setColor(Color.BLACK);
        drawDicePoints(g);
    }

    /**
     * Отрисовывает точки на грани кубика в соответствии с текущим значением.
     *
     * @param g графический контекст для отрисовки
     */
    private void drawDicePoints(Graphics g) {
        if (faceValue < 1 || faceValue > 6) return;

        double[][] points = DICE_POINTS_PROPORTIONAL[faceValue - 1];

        int pointDiameter = (int)(Math.min(getWidth(), getHeight()) * POINT_SIZE_RATIO);
        pointDiameter = Math.max(pointDiameter, 4);

        int pointRadius = pointDiameter / 2;

        for (double[] point : points) {
            int centerX = (int)(point[0] * getWidth());
            int centerY = (int)(point[1] * getHeight());

            g.fillOval(
                    centerX - pointRadius,
                    centerY - pointRadius,
                    pointDiameter,
                    pointDiameter
            );
        }
    }

    /**
     * Бросает кубик, устанавливая случайное значение от 1 до 6.
     * После броска перерисовывает кубик.
     */
    public void roll() {
        faceValue = random.nextInt(6) + 1;
        repaint();
    }

    /**
     * Возвращает текущее значение на грани кубика.
     *
     * @return значение кубика (1-6)
     */
    public int getFaceValue() {
        return faceValue;
    }
}