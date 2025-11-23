package core;

import core.BoardPanel;
import core.GameConfiguration;
import entities.Board;
import core.Move;
import entities.MoveTransition;
import entities.Piece;
import entities.Square;
import gui.ChessApp;
import gui.SoundManager;
import gui.TimerPanel;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

import java.io.InputStream;
import java.util.Collection;

public class GameEngine {

    private final StackPane rootLayer;
    private final BorderPane uiLayer;

    private final BoardPanel boardPanel;
    private final TimerPanel gameTimer;
    private Board chessBoard;

    private VBox pauseMenu;
    private VBox confirmationOverlay;
    private Font pixelFont;

    private Square sourceSquare;
    private Square destinationSquare;
    private Piece humanMovedPiece;

    private GameConfiguration config;

    public GameEngine(GameConfiguration config) {
        this.config = config;
        this.chessBoard = Board.createStandardBoard();

        // Load Font
        this.pixelFont = loadCustomFont("/assets/alagard.ttf", 20);

        this.gameTimer = new TimerPanel(config.getTimeControlMinutes());
        this.boardPanel = new BoardPanel(this, config);

        this.rootLayer = new StackPane();
        addBackground("/assets/playing.png");

        this.uiLayer = new BorderPane();
        this.uiLayer.setCenter(this.boardPanel);
        this.uiLayer.setBottom(this.gameTimer);

        createPauseMenu();
        createConfirmationOverlay();

        Button menuBtn = createImageButton("/assets/pause.png", 100);
        menuBtn.setOnAction(e -> {
            SoundManager.playClick();
            gameTimer.pause();
            pauseMenu.setVisible(true);
            confirmationOverlay.setVisible(false);
        });

        StackPane.setAlignment(menuBtn, Pos.TOP_LEFT);
        StackPane.setMargin(menuBtn, new Insets(15));

        this.rootLayer.getChildren().addAll(this.uiLayer, menuBtn, pauseMenu, confirmationOverlay);

        this.boardPanel.drawBoard(this.chessBoard);
    }

    public StackPane getLayout() { return this.rootLayer; }

    private void createPauseMenu() {
        this.pauseMenu = new VBox(15);
        this.pauseMenu.setAlignment(Pos.CENTER);
        this.pauseMenu.setMaxSize(350, 300);

        this.pauseMenu.setStyle(
                "-fx-background-color: rgba(0, 0, 0, 0.9);" +
                        "-fx-border-color: #8f563b; -fx-border-width: 4px;" +
                        "-fx-background-radius: 10; -fx-border-radius: 10;"
        );

        Label title = new Label("GAME PAUSED");
        title.setFont(loadCustomFont("/assets/alagard.ttf", 32));
        title.setStyle("-fx-text-fill: #e67e22; -fx-effect: dropshadow(gaussian, black, 3, 1.0, 0, 0);");

        Button continueBtn = createImageButton("/assets/continue.png", 200);
        continueBtn.setOnAction(e -> {
            SoundManager.playClick();
            this.pauseMenu.setVisible(false);
            gameTimer.resume();
        });

        Button exitBtn = createImageButton("/assets/exitmatch.png", 200);
        exitBtn.setOnAction(e -> {
            SoundManager.playClick();
            this.pauseMenu.setVisible(false);
            this.confirmationOverlay.setVisible(true);
        });

        this.pauseMenu.getChildren().addAll(title, continueBtn, exitBtn);
        this.pauseMenu.setVisible(false);
    }

    private void createConfirmationOverlay() {
        this.confirmationOverlay = new VBox(20);
        this.confirmationOverlay.setAlignment(Pos.CENTER);
        this.confirmationOverlay.setMaxSize(400, 250);
        this.confirmationOverlay.setStyle(
                "-fx-background-color: rgba(50, 0, 0, 0.95);" +
                        "-fx-border-color: #c0392b; -fx-border-width: 4px;" +
                        "-fx-background-radius: 10;"
        );

        // Use Pixel Font
        Label warning = new Label("You will lose the match.\nAre you sure?");
        warning.setFont(loadCustomFont("/assets/alagard.ttf", 24));
        warning.setStyle("-fx-text-fill: #ecf0f1; -fx-text-alignment: center;");

        HBox buttons = new HBox(20);
        buttons.setAlignment(Pos.CENTER);

        Button yesBtn = createImageButton("/assets/yes.png", 100);
        yesBtn.setOnAction(e -> {
            SoundManager.playClick();
            ChessApp.showMainMenu();
        });

        Button noBtn = createImageButton("/assets/no.png", 100);
        noBtn.setOnAction(e -> {
            SoundManager.playClick();
            this.confirmationOverlay.setVisible(false);
            gameTimer.resume();
        });

        buttons.getChildren().addAll(yesBtn, noBtn);
        this.confirmationOverlay.getChildren().addAll(warning, buttons);
        this.confirmationOverlay.setVisible(false);
    }

    // --- UTILS ---
    private Button createImageButton(String path, double width) {
        Button btn = new Button();
        InputStream is = getClass().getResourceAsStream(path);
        if (is != null) {
            ImageView v = new ImageView(new Image(is));
            v.setFitWidth(width); v.setPreserveRatio(true);
            btn.setGraphic(v);
            btn.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");
            btn.setOnMouseEntered(e -> btn.setScaleX(1.1));
            btn.setOnMouseExited(e -> btn.setScaleX(1.0));
        }
        return btn;
    }

    private Font loadCustomFont(String path, double size) {
        try {
            InputStream is = getClass().getResourceAsStream(path);
            if (is != null) return Font.loadFont(is, size);
        } catch (Exception e) { }
        return new Font("Arial", size);
    }

    private void addBackground(String imagePath) {
        try {
            InputStream is = getClass().getResourceAsStream(imagePath);
            if (is != null) {
                ImageView bg = new ImageView(new Image(is));
                bg.fitWidthProperty().bind(rootLayer.widthProperty());
                bg.fitHeightProperty().bind(rootLayer.heightProperty());
                bg.setPreserveRatio(false);
                this.rootLayer.getChildren().add(0, bg);
            }
        } catch (Exception e) {
            this.rootLayer.setStyle("-fx-background-color: #202020;");
        }
    }

    public void handleMouseClick(int squareId) {
        boardPanel.drawBoard(this.chessBoard);

        if (sourceSquare != null) {
            destinationSquare = chessBoard.getSquare(squareId);
            final Move move = findLegalMove(sourceSquare.getSquareCoordinate(), destinationSquare.getSquareCoordinate());

            if (move != null) {
                final MoveTransition transition = chessBoard.getCurrentPlayer().makeMove(move);
                if (transition.getMoveStatus().isDone()) {
                    this.chessBoard = transition.getTransitionBoard();
                    this.gameTimer.switchTurn();
                    boardPanel.drawBoard(this.chessBoard);
                    System.out.println("Move executed!");
                }
            }
            sourceSquare = null;
            destinationSquare = null;
            humanMovedPiece = null;
        } else {
            Square clickedSquare = chessBoard.getSquare(squareId);
            if (clickedSquare.isOccupied()) {
                Piece piece = clickedSquare.getPiece();
                if (piece.getPieceAlliance() == chessBoard.getCurrentPlayer().getAlliance()) {
                    sourceSquare = clickedSquare;
                    humanMovedPiece = piece;
                    boardPanel.highlightSourceSquare(squareId);
                    final Collection<Move> legalMoves = piece.calculateLegalMoves(this.chessBoard);
                    boardPanel.highlightLegals(legalMoves, piece.getPieceAlliance());
                }
            }
        }
    }

    private Move findLegalMove(int currentPos, int destinationPos) {
        for (final Move move : this.chessBoard.getCurrentPlayer().getLegalMoves()) {
            if (move.getMovedPiece().getPiecePosition() == currentPos &&
                    move.getDestinationCoordinate() == destinationPos) {
                return move;
            }
        }
        return null;
    }
}