package monopoly.model;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

public class Square extends JPanel {
    private int number;
    private String name;
    private JLabel nameLabel;
    private JLabel priceLabel;
    private int price = 0;
    private int rentPrice = 0;
    private static int totalSquares = 0;

    public Square(int xCoord, int yCoord, int width, int height, String labelString, int rotationDegrees) {
        this.number = totalSquares++;
        this.name = labelString;

        setBorder(new LineBorder(Color.BLACK, 1));
        setBounds(xCoord, yCoord, width, height);
        setLayout(null);
        setOpaque(false);
        createLabels(labelString, rotationDegrees);
        setDoubleBuffered(true);
    }

    private void createLabels(String text, int rotationDegrees) {
        if (rotationDegrees == 0) {
            nameLabel = new JLabel(text);
            nameLabel.setBounds(0, 20, getWidth(), 20);
            nameLabel.setHorizontalAlignment(SwingConstants.CENTER);

            priceLabel = new JLabel(price > 0 ? "$" + price : "");
            priceLabel.setBounds(0, getHeight() - 35, getWidth(), 20);
            priceLabel.setHorizontalAlignment(SwingConstants.CENTER);
        } else {
            nameLabel = new RotatedLabel(text, rotationDegrees);
            setLabelBounds(rotationDegrees);

            priceLabel = new RotatedLabel(price > 0 ? "$" + price : "", rotationDegrees);
            setPriceLabelBounds(rotationDegrees);
        }

        nameLabel.setFont(new Font("Arial", Font.PLAIN, 9));
        nameLabel.setForeground(Color.BLACK);
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(nameLabel);

        priceLabel.setFont(new Font("Arial", Font.BOLD, 10));
        priceLabel.setForeground(Color.RED);
        priceLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(priceLabel);
    }

    private void setLabelBounds(int rotationDegrees) {
        switch (rotationDegrees) {
            case 90:
                nameLabel.setBounds(20, 0, getWidth(), getHeight());
                break;
            case -90:
                nameLabel.setBounds(-10, 0, getWidth(), getHeight());
                break;
            case 180:
                nameLabel.setBounds(0, 0, getWidth(), getHeight());
                break;
            default:
                nameLabel.setBounds(0, 0, getWidth(), getHeight());
        }
    }

    private void setPriceLabelBounds(int rotationDegrees) {
        switch (rotationDegrees) {
            case 90:
                priceLabel.setBounds(-30, 0, getWidth(), getHeight());
                break;
            case -90:
                priceLabel.setBounds(30, 0, getWidth(), getHeight());
                break;
            case 180:
                priceLabel.setBounds(0, -30, getWidth(), getHeight());
                break;
            default:
                priceLabel.setBounds(0, 0, getWidth(), getHeight());
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());
        paintColorBand(g);
    }

    private void paintColorBand(Graphics g) {
        Color[] colors = {Color.BLUE, Color.RED, Color.ORANGE, Color.GREEN};
        int[][] coloredSquares = {{1,3,4}, {6,8,9}, {11,13,14}, {16,17,19}};

        for (int i = 0; i < coloredSquares.length; i++) {
            for (int squareNum : coloredSquares[i]) {
                if (this.number == squareNum) {
                    drawColorBand(g, colors[i], i);
                    return;
                }
            }
        }
    }

    private void drawColorBand(Graphics g, Color color, int bandType) {
        g.setColor(color);

        switch (bandType) {
            case 0:
                g.fillRect(0, getHeight() - 20, getWidth(), 20);
                break;
            case 1:
                g.fillRect(0, 0, 20, getHeight());
                break;
            case 2:
                g.fillRect(0, 0, getWidth(), 20);
                break;
            case 3:
                g.fillRect(getWidth() - 20, 0, 20, getHeight());
                break;
        }
    }

    public void updatePriceDisplay() {
        priceLabel.setText(price > 0 ? "$" + price : "");
        priceLabel.repaint();
    }

    private class RotatedLabel extends JLabel {
        private int rotationDegrees;

        public RotatedLabel(String text, int rotationDegrees) {
            super(text);
            this.rotationDegrees = rotationDegrees;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            AffineTransform aT = g2.getTransform();
            Shape oldShape = g2.getClip();

            double x = getWidth() / 2.0;
            double y = getHeight() / 2.0;
            aT.rotate(Math.toRadians(rotationDegrees), x, y);

            g2.setTransform(aT);
            g2.setClip(oldShape);
            super.paintComponent(g);
        }
    }

    public void setRentPrice(int rentPrice) {
        this.rentPrice = rentPrice;
    }

    public int getRentPrice() {
        return rentPrice;
    }

    public void setPrice(int price) {
        this.price = price;
        updatePriceDisplay();
    }

    public int getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }
}