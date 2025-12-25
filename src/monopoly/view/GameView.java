package monopoly.view;

import javax.swing.*;
import java.awt.*;
import monopoly.controller.GameController;
import monopoly.model.Board;

/**
 * Главное окно приложения Monopoly.
 * Содержит все компоненты интерфейса и управляет их отображением.
 */
public class GameView extends JFrame {
    /** Панель игрового поля */
    private GameBoardPanel gameBoardPanel;

    /** Панель управления */
    private ControlPanel controlPanel;

    /** Панель информации об игроках */
    private PlayerInfoPanel playerInfoPanel;

    /** Консоль информации */
    private InfoConsole infoConsole;

    /** Контроллер игры */
    private GameController controller;

    /**
     * Создает главное окно игры.
     */
    public GameView() {
        initializeFrame();
        initializeComponents();
        setupController();
        setVisible(true);
    }

    /**
     * Инициализирует параметры главного окна.
     */
    private void initializeFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1080, 720);
        setTitle("Monopoly Game");

        JPanel contentPanel = new JPanel();
        contentPanel.setBorder(new javax.swing.border.EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPanel);
        contentPanel.setLayout(null);
        contentPanel.setBackground(Color.WHITE);
    }

    /**
     * Инициализирует компоненты интерфейса.
     */
    private void initializeComponents() {
        gameBoardPanel = new GameBoardPanel();
        controlPanel = new ControlPanel();
        infoConsole = new InfoConsole();

        getContentPane().add(gameBoardPanel);
        getContentPane().add(controlPanel);

        // Добавляем консоль информации на панель управления
        controlPanel.add(infoConsole);
    }

    /**
     * Настраивает контроллер игры.
     */
    private void setupController() {
        controller = new GameController(this, gameBoardPanel, controlPanel, infoConsole);
        controller.initializeGame();
    }

    /**
     * Инициализирует панель информации об игроках.
     *
     * @param board игровое поле
     * @param numberOfPlayers количество игроков
     */
    public void initializePlayerInfo(Board board, int numberOfPlayers) {
        playerInfoPanel = new PlayerInfoPanel(board);
        playerInfoPanel.initializePlayerPanels(numberOfPlayers);
        controlPanel.add(playerInfoPanel);
    }

    /**
     * Возвращает панель игрового поля.
     *
     * @return панель игрового поля
     */
    public GameBoardPanel getGameBoardPanel() { return gameBoardPanel; }

    /**
     * Возвращает панель управления.
     *
     * @return панель управления
     */
    public ControlPanel getControlPanel() { return controlPanel; }

    /**
     * Возвращает панель информации об игроках.
     *
     * @return панель информации об игроках
     */
    public PlayerInfoPanel getPlayerInfoPanel() { return playerInfoPanel; }

    /**
     * Возвращает консоль информации.
     *
     * @return консоль информации
     */
    public InfoConsole getInfoConsole() { return infoConsole; }

    /**
     * Возвращает контроллер игры.
     *
     * @return контроллер игры
     */
    public GameController getController() { return controller; }
}