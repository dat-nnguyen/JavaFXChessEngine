package gui;

import core.GameConfiguration;
import core.GameEngine;
import core.Move;
import entities.Alliance;
import entities.Board;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BoardPanel extends GridPane {

    private final List<SquarePanel> boardSquares;

    public BoardPanel(GameEngine engine) {
        this.boardSquares = new ArrayList<>();
        this.setMaxSize(600, 600);
        this.setAlignment(javafx.geometry.Pos.CENTER);
        this.setStyle("-fx-border-color: #8f563b; -fx-border-width: 8px; -fx-effect: dropshadow(three-pass-box, black, 10, 0, 0, 0);");
        // Create 64 squares
        for (int i = 0; i < 64; i++) {
            final SquarePanel squarePanel = new SquarePanel(engine, i);
            this.boardSquares.add(squarePanel);
            this.add(squarePanel, i % 8, i / 8);
        }
    }
    public BoardPanel(GameEngine engine, GameConfiguration config) {
        this.boardSquares = new ArrayList<>();

        this.setMaxSize(560, 560);
        this.setAlignment(javafx.geometry.Pos.CENTER);
        this.setHgap(0);
        this.setVgap(0);

        this.setStyle(
                "-fx-background-color: #b58863;" +
                        "-fx-border-color: #8f563b; -fx-border-width: 8px; -fx-effect: dropshadow(three-pass-box, black, 10, 0, 0, 0);"
        );

        boolean flipBoard = (config.getPlayerColor() == Alliance.BLACK);

        for (int i = 0; i < 64; i++) {
            final SquarePanel squarePanel = new SquarePanel(engine, i);
            this.boardSquares.add(squarePanel);

            int col = i % 8;
            int row = i / 8;

            if (flipBoard) {
                col = 7 - col;
                row = 7 - row;
            }

            this.add(squarePanel, col, row);
        }
    }
    public void highlightLegals(final Collection<Move> legalMoves, final Alliance alliance) {
        final Color highlightColor = alliance.isWhite() ?
                Color.rgb(100, 255, 100, 0.6) : // Green
                Color.rgb(255, 100, 100, 0.6);  // Red

        for (final Move move : legalMoves) {
            final SquarePanel panel = this.boardSquares.get(move.getDestinationCoordinate());
            panel.enableHighlight(highlightColor);
        }
    }
    public void highlightSourceSquare(int squareId) {
        if (squareId >= 0 && squareId < boardSquares.size()) {
            boardSquares.get(squareId).highlightSource();
        }
    }
    public void drawBoard(final Board board) {
        for (final SquarePanel squarePanel : boardSquares) {
            squarePanel.drawSquare(board);
        }
    }
}