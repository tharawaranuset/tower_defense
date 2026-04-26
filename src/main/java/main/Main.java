package main;

import controller.GameController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import model.Projectile;
import model.entity.enemy.Enemy;
import model.entity.tower.Tower;
import thread.GameLoopThread;
import thread.SpawnThread;
import util.GameConfig;
import util.SoundManager;

import java.util.List;

public class Main extends Application {

    private Canvas canvas;
    private GraphicsContext gc;
    private Label goldLabel;
    private Label livesLabel;
    private Label waveLabel;
    private Label messageLabel;
    private ToggleGroup towerToggle;

    private GameController controller;
    private GameLoopThread loopThread;
    private SpawnThread spawnThread;

    private Button btnNextWave;
    private Button btnMute;

    private boolean gameOverSoundPlayed = false;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        controller = new GameController();

        BorderPane root = new BorderPane();
        root.setTop(buildHUD());
        root.setCenter(buildCanvas());
        root.setRight(buildShop());
        root.setBottom(buildMessageBar());

        stage.setTitle("Tower Defense");
        stage.setResizable(false);
        stage.setScene(new Scene(root));
        stage.setOnCloseRequest(e -> shutdown());
        stage.show();

        startThreads();
    }

    // ----------------------------------------------------------------
    //  Layout builders
    // ----------------------------------------------------------------

    private HBox buildHUD() {
        goldLabel = new Label("Gold: 150");
        livesLabel = new Label("Lives: 20");
        waveLabel = new Label("Wave: 0");

        goldLabel.setFont(Font.font(null, FontWeight.BOLD, 14));
        livesLabel.setFont(Font.font(null, FontWeight.BOLD, 14));
        waveLabel.setFont(Font.font(14));

        goldLabel.setTextFill(Color.GOLD);
        livesLabel.setTextFill(Color.LIGHTCORAL);
        waveLabel.setTextFill(Color.LIGHTBLUE);

        HBox hud = new HBox(24, goldLabel, livesLabel, waveLabel);
        hud.setPadding(new Insets(8, 12, 8, 12));
        hud.setStyle("-fx-background-color: #2b2b2b;");
        return hud;
    }

    private Canvas buildCanvas() {
        canvas = new Canvas(GameConfig.COLS * GameConfig.TILE_SIZE, GameConfig.ROWS * GameConfig.TILE_SIZE);
        gc = canvas.getGraphicsContext2D();
        canvas.setOnMouseClicked(this::handleMapClick);
        return canvas;
    }

    private VBox buildShop() {
        // build tower button
        towerToggle = new ToggleGroup();

        RadioButton btnArrow = buildTowerBtn("Arrow\n(50g)", "arrow");
        RadioButton btnCannon = buildTowerBtn("Cannon\n(80g)", "cannon");
        RadioButton btnIce = buildTowerBtn("Ice\n(70g)", "ice");
        btnArrow.setSelected(true);

        // combine to a panel
        Label shopTitle = new Label("Towers");
        shopTitle.setTextFill(Color.WHITE);
        shopTitle.setFont(Font.font("System", FontWeight.BOLD, 12));

        VBox shop = new VBox(
                10,
                shopTitle,
                btnArrow,
                btnCannon,
                btnIce,
                new Separator(), buildNextWaveBtn(),
                new Separator(), buildMuteBtn()
        );
        shop.setPadding(new Insets(10));
        shop.setPrefWidth(110);
        shop.setStyle("-fx-background-color: #3c3c3c;");
        return shop;
    }

    private Button buildNextWaveBtn() {
        btnNextWave = new Button("Next Wave");
        btnNextWave.setMaxWidth(Double.MAX_VALUE);
        btnNextWave.setOnAction(e -> {
            controller.getWaveController().nextWave();
            int wave = controller.getWaveController().getCurrentWave();
            showMessage("Wave " + wave + " started!");
            btnNextWave.setDisable(true);
        });
        return btnNextWave;
    }

    private Button buildMuteBtn() {
        btnMute = new javafx.scene.control.Button("Mute");
        btnMute.setMaxWidth(Double.MAX_VALUE);
        btnMute.setOnAction(e -> {
            SoundManager sm = SoundManager.getInstance();
            sm.setMuted(!sm.isMuted());
            btnMute.setText(sm.isMuted() ? "Unmute" : "Mute");
        });
        return btnMute;
    }

    private RadioButton buildTowerBtn(String label, String type) {
        RadioButton rb = new RadioButton(label);
        rb.setToggleGroup(towerToggle);
        rb.setUserData(type);
        rb.setTextFill(Color.WHITE);
        rb.setWrapText(true);
        return rb;
    }

    private HBox buildMessageBar() {
        messageLabel = new Label("Choose a tower and click on the map to place it");
        messageLabel.setStyle("-fx-text-fill: #aaaaaa; -fx-font-size: 12px;");
        HBox bar = new HBox(messageLabel);
        bar.setPadding(new Insets(4, 10, 4, 10));
        bar.setStyle("-fx-background-color: #1e1e1e;");
        return bar;
    }

    // ----------------------------------------------------------------
    //  Rendering, called from GameLoopThread
    // ----------------------------------------------------------------

    public void render() {
        Platform.runLater(() -> {
            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            drawMap();
            drawTowers();
            drawProjectiles();
            drawEnemies();
            updateHUD();
            if (controller.isGameOver()) {
                drawGameOver();
                showMessage("GAME OVER");
            }
        });
    }

    private void drawMap() {
        controller.getMap().render(gc);
    }

    private void drawTowers() {
        List<Tower> towers = controller.getMap().getTowers();

        for (Tower t : towers) {
            t.render(gc);
        }
    }

    private void drawProjectiles() {
        // controller.getMap().getProjectiles().forEach(p -> p.render(gc));
        List<Projectile> projectiles = controller.getMap().getProjectiles();

        for (Projectile projectile : projectiles) {
            projectile.render(gc);
        }
    }

    private void drawEnemies() {
        List<Enemy> enemies = controller.getMap().getEnemies();

        for (Enemy enemy : enemies) {
            enemy.render(gc);
        }
    }

    private void updateHUD() {
        goldLabel.setText("Gold: " + controller.getGold());
        livesLabel.setText("Lives: " + controller.getLives());
        waveLabel.setText("Wave: " + controller.getWaveController().getCurrentWave());

        if (btnNextWave != null) {
            boolean canStart = controller.getWaveController().isWaveComplete();
            // TODO: move controller.isGameOver() to somewhere
            btnNextWave.setDisable(!canStart || controller.isGameOver());
        }
    }

    private void drawGameOver() {
        if (!gameOverSoundPlayed) {
            SoundManager.getInstance().stopBgm();
            SoundManager.getInstance().play("game_over");
            gameOverSoundPlayed = true;
        }

        gc.setFill(Color.color(0, 0, 0, 0.6));
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setFill(Color.RED);
        gc.setFont(Font.font(48));
        gc.fillText("GAME OVER", GameConfig.COLS * GameConfig.TILE_SIZE / 2.0 - 140, GameConfig.ROWS * GameConfig.TILE_SIZE / 2.0);
    }

    // ----------------------------------------------------------------
    //  Input, Buy Tower
    // ----------------------------------------------------------------

    private void handleMapClick(MouseEvent e) {
        if (controller.isGameOver()) return;

        Toggle selected = towerToggle.getSelectedToggle();
        if (selected == null) return;

        int col = (int) (e.getX() / GameConfig.TILE_SIZE);
        int row = (int) (e.getY() / GameConfig.TILE_SIZE);
        String type = (String) selected.getUserData();

        boolean ok = controller.buyTower(type, col, row);
        showMessage(ok
                ? type + " tower placed at (" + col + ", " + row + ")"
                : "Cannot place here or not enough gold!");
    }

    // ----------------------------------------------------------------
    //  Threads
    // ----------------------------------------------------------------

    private void startThreads() {
        loopThread = new GameLoopThread(controller, this);
        loopThread.setDaemon(true); // set to be background thread, die if close
        loopThread.start();

        spawnThread = new SpawnThread(controller.getWaveController());
        spawnThread.setDaemon(true);
        spawnThread.start();

        SoundManager.getInstance().playBgm();
    }

    private void shutdown() {
        if (loopThread != null) loopThread.stopLoop();
        if (spawnThread != null) spawnThread.stopSpawn();
        SoundManager.getInstance().stopBgm();
    }

    public void showMessage(String msg) {
        Platform.runLater(() -> messageLabel.setText(msg));
    }
}