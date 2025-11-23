package core;

import core.GameConfiguration;
import entities.Alliance;
import gui.ChessApp;
import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.io.InputStream;

public class GameSetup {

    private final StackPane rootPane;
    private final VBox optionPanel;
    private Font pixelFont;

    private ToggleGroup modeGroup;
    private VBox difficultyBox;
    private ToggleGroup difficultyGroup;
    private ComboBox<Integer> timeDropdown;
    private ToggleGroup colorGroup;

    public GameSetup() {
        this.rootPane = new StackPane();
        this.rootPane.setStyle("-fx-background-color: black;");

        // 1. Video Background
        addBackground("/assets/background.mp4");

        this.pixelFont = loadCustomFont("/assets/alagard.ttf", 20);
        this.optionPanel = new VBox(15);
        this.optionPanel.setMaxSize(500, 550);
        this.optionPanel.setAlignment(Pos.TOP_CENTER);
        this.optionPanel.setPadding(new Insets(30));

        this.optionPanel.setStyle(
                "-fx-background-color: rgba(0, 0, 0, 0.85);" +
                        "-fx-border-color: #8f563b;" +
                        "-fx-border-width: 4px;" +
                        "-fx-background-radius: 10;" +
                        "-fx-border-radius: 10;"
        );

        Label header = new Label("GAME CONFIGURATION");
        header.setFont(loadCustomFont("/assets/alagard.ttf", 36));
        header.setStyle("-fx-text-fill: #e67e22; -fx-effect: dropshadow(gaussian, black, 3, 1.0, 0, 0);");

        // --- MODE ---
        Label modeLabel = createHeaderLabel("Select Mode");
        RadioButton pvpBtn = createPixelRadioButton("Player vs Player");
        pvpBtn.setSelected(true);
        RadioButton aiBtn = createPixelRadioButton("Practice (vs AI)");

        modeGroup = new ToggleGroup();
        pvpBtn.setToggleGroup(modeGroup);
        aiBtn.setToggleGroup(modeGroup);

        // --- DIFFICULTY ---
        difficultyBox = new VBox(10);
        difficultyBox.setAlignment(Pos.CENTER);
        difficultyBox.setStyle("-fx-background-color: rgba(255, 255, 255, 0.05); -fx-padding: 10; -fx-background-radius: 5;");

        Label diffLabel = new Label("AI Difficulty");
        diffLabel.setFont(loadCustomFont("/assets/alagard.ttf", 22));
        diffLabel.setStyle("-fx-text-fill: #f1c40f;");

        RadioButton easyBtn = createPixelRadioButton("Easy");
        RadioButton medBtn = createPixelRadioButton("Medium");
        medBtn.setSelected(true);
        RadioButton hardBtn = createPixelRadioButton("Hard");

        difficultyGroup = new ToggleGroup();
        easyBtn.setToggleGroup(difficultyGroup);
        medBtn.setToggleGroup(difficultyGroup);
        hardBtn.setToggleGroup(difficultyGroup);

        difficultyBox.getChildren().addAll(diffLabel, easyBtn, medBtn, hardBtn);
        difficultyBox.setVisible(false);
        difficultyBox.setManaged(false);

        modeGroup.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
            boolean isAI = (newVal == aiBtn);
            difficultyBox.setVisible(isAI);
            difficultyBox.setManaged(isAI);
        });

        // --- TIME & COLOR ---
        Label timeLabel = createHeaderLabel("Time per Player (Minutes)");
        timeDropdown = new ComboBox<>();
        timeDropdown.getItems().addAll(10, 20, 30);
        timeDropdown.setValue(10);
        timeDropdown.setStyle("-fx-font-family: 'Alagard'; -fx-font-size: 18px; -fx-background-color: #dcdcdc;");

        Label colorLabel = createHeaderLabel("Choose Side");
        RadioButton whiteBtn = createPixelRadioButton("White");
        whiteBtn.setSelected(true);
        RadioButton blackBtn = createPixelRadioButton("Black");
        RadioButton randBtn = createPixelRadioButton("Random");

        colorGroup = new ToggleGroup();
        whiteBtn.setToggleGroup(colorGroup);
        blackBtn.setToggleGroup(colorGroup);
        randBtn.setToggleGroup(colorGroup);

        HBox colorBox = new HBox(20, whiteBtn, blackBtn, randBtn);
        colorBox.setAlignment(Pos.CENTER);

        // --- BUTTONS ---
        Button startBtn = createImageButton("/assets/start.png");

        // --- CRITICAL FIX: Restored the Launch Logic ---
        startBtn.setOnAction(e -> {
            // 1. Get Mode
            RadioButton selectedMode = (RadioButton) modeGroup.getSelectedToggle();
            boolean isAI = selectedMode.getText().contains("AI");
            GameConfiguration.GameMode mode = isAI ? GameConfiguration.GameMode.HUMAN_VS_AI : GameConfiguration.GameMode.HUMAN_VS_HUMAN;

            // 2. Get Difficulty
            GameConfiguration.Difficulty diff = null;
            if (isAI) {
                RadioButton selectedDiff = (RadioButton) difficultyGroup.getSelectedToggle();
                String t = selectedDiff.getText();
                if (t.equals("Easy")) diff = GameConfiguration.Difficulty.EASY;
                else if (t.equals("Medium")) diff = GameConfiguration.Difficulty.MEDIUM;
                else diff = GameConfiguration.Difficulty.HARD;
            }

            // 3. Get Time & Color
            int time = timeDropdown.getValue();

            RadioButton selectedColor = (RadioButton) colorGroup.getSelectedToggle();
            String cText = selectedColor.getText();
            Alliance alliance = null;
            if (cText.equals("White")) alliance = Alliance.WHITE;
            else if (cText.equals("Black")) alliance = Alliance.BLACK;

            // 4. Launch
            GameConfiguration config = new GameConfiguration(mode, diff, time, alliance);
            ChessApp.showGameEngine(config);
        });

        Button backBtn = createImageButton("/assets/back.png");
        backBtn.setOnAction(e -> ChessApp.showMainMenu());

        HBox buttonBox = new HBox(30, backBtn, startBtn);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(20, 0, 0, 0));

        optionPanel.getChildren().addAll(header, new Separator(), modeLabel, pvpBtn, aiBtn, difficultyBox, timeLabel, timeDropdown, colorLabel, colorBox, new Separator(), buttonBox);
        rootPane.getChildren().add(optionPanel);
    }

    public StackPane getLayout() { return this.rootPane; }

    // --- HELPERS (Same as before) ---
    private void addBackground(String videoPath) {
        try {
            String mediaUrl = getClass().getResource(videoPath).toExternalForm();
            Media media = new Media(mediaUrl);
            MediaPlayer player = new MediaPlayer(media);
            player.setCycleCount(MediaPlayer.INDEFINITE);
            player.setAutoPlay(true);
            player.setMute(true);
            MediaView mediaView = new MediaView(player);
            mediaView.fitWidthProperty().bind(this.rootPane.widthProperty());
            mediaView.fitHeightProperty().bind(this.rootPane.heightProperty());
            mediaView.setPreserveRatio(false);

            InputStream imgStream = getClass().getResourceAsStream("/assets/background.png");
            ImageView placeholder = new ImageView(new Image(imgStream));
            placeholder.fitWidthProperty().bind(this.rootPane.widthProperty());
            placeholder.fitHeightProperty().bind(this.rootPane.heightProperty());
            placeholder.setPreserveRatio(false);

            this.rootPane.getChildren().add(0, mediaView);
            this.rootPane.getChildren().add(1, placeholder);

            player.setOnPlaying(() -> {
                FadeTransition fade = new FadeTransition(Duration.millis(500), placeholder);
                fade.setFromValue(1.0);
                fade.setToValue(0.0);
                fade.setOnFinished(e -> this.rootPane.getChildren().remove(placeholder));
                fade.play();
            });
        } catch (Exception e) { e.printStackTrace(); this.rootPane.setStyle("-fx-background-color: #2b2b2b;"); }
    }

    private Button createImageButton(String path) {
        Button btn = new Button();
        InputStream is = getClass().getResourceAsStream(path);
        if (is != null) {
            ImageView v = new ImageView(new Image(is));
            v.setFitWidth(160); v.setPreserveRatio(true);
            btn.setGraphic(v);
            btn.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");
            btn.setOnMouseEntered(e -> btn.setScaleX(1.1));
            btn.setOnMouseExited(e -> btn.setScaleX(1.0));
        } else btn.setText("MISSING");
        return btn;
    }

    private Label createHeaderLabel(String t) { Label l = new Label(t); l.setFont(this.pixelFont); l.setStyle("-fx-text-fill: #b2bec3; -fx-underline: true;"); return l; }
    private RadioButton createPixelRadioButton(String t) { RadioButton r = new RadioButton(t); r.setFont(this.pixelFont); r.setStyle("-fx-text-fill: #f0e6d2; -fx-cursor: hand;"); return r; }
    private Font loadCustomFont(String p, double s) { try { InputStream is = getClass().getResourceAsStream(p); if (is != null) return Font.loadFont(is, s); } catch (Exception e) {} return new Font("Arial", s); }
}