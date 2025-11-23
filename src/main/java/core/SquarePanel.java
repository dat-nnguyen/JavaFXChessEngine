package core;

import core.GameEngine;
import entities.Board;
import entities.Piece;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.InputStream;

public class SquarePanel extends StackPane {

    private final int squareId;
    private final GameEngine engine;

    private final Rectangle background;
    private final ImageView pieceIcon;
    private final Rectangle highlightMarker;

    // Colors
    private static final Color LIGHT_COLOR = Color.web("#f0d9b5");
    private static final Color DARK_COLOR = Color.web("#b58863");
    private static final Color SOURCE_COLOR = Color.web("#769656");

    public SquarePanel(final GameEngine engine, final int squareId) {
        this.engine = engine;
        this.squareId = squareId;

        // 1. INCREASED SIZE: 70x70
        this.background = new Rectangle(70, 70);
        assignTileColor();

        // 2. INCREASED PIECE SIZE: 60px
        this.pieceIcon = new ImageView();
        this.pieceIcon.setFitWidth(60);
        this.pieceIcon.setPreserveRatio(true);
        this.pieceIcon.setSmooth(false);

        // 3. Marker
        this.highlightMarker = new Rectangle(15, 15);
        this.highlightMarker.setVisible(false);

        this.getChildren().addAll(background, highlightMarker, pieceIcon);

        setOnMouseClicked(e -> engine.handleMouseClick(this.squareId));
    }

    // ... (Rest of the class: drawSquare, enableHighlight, highlightSource, assignTilePieceIcon... KEEP THE SAME) ...
    public void drawSquare(final Board board) {
        assignTilePieceIcon(board);
        this.highlightMarker.setVisible(false);
        assignTileColor();
    }

    public void enableHighlight(Color color) {
        this.highlightMarker.setFill(color);
        this.highlightMarker.setVisible(true);
    }

    public void highlightSource() {
        this.background.setFill(SOURCE_COLOR);
    }

    private void assignTileColor() {
        boolean isRowEven = (this.squareId / 8) % 2 == 0;
        if (isRowEven) {
            this.background.setFill(this.squareId % 2 == 0 ? LIGHT_COLOR : DARK_COLOR);
        } else {
            this.background.setFill(this.squareId % 2 != 0 ? LIGHT_COLOR : DARK_COLOR);
        }
    }

    private void assignTilePieceIcon(final Board board) {
        this.pieceIcon.setImage(null);
        if (board.getSquare(this.squareId).isOccupied()) {
            final Piece piece = board.getSquare(this.squareId).getPiece();
            String allianceFolder = piece.getPieceAlliance().isWhite() ? "whitePiece" : "blackPiece";
            String colorPrefix = piece.getPieceAlliance().isWhite() ? "white" : "black";
            String typeName = piece.getPieceType().name().toLowerCase();
            String imagePath = "/assets/" + allianceFolder + "/" + colorPrefix + typeName + ".png";
            try {
                InputStream imageStream = getClass().getResourceAsStream(imagePath);
                if (imageStream != null) {
                    this.pieceIcon.setImage(new Image(imageStream));
                }
            } catch (Exception e) { e.printStackTrace(); }
        }
    }
}