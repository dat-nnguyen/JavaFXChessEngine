package entities;

import Players.BlackPlayer;
import Players.Player;
import Players.WhitePlayer;
import core.Move;
import java.util.*;

public class Board {

    private final List<Square> gameBoard;
    private final Collection<Piece> whitePieces;
    private final Collection<Piece> blackPieces;

    // stored as Pawn (specific) not Piece (generic)
    private final Pawn enPassantPawn;

    private final WhitePlayer whitePlayer;
    private final BlackPlayer blackPlayer;
    private final Player currentPlayer;

    private Board(final Builder builder) {
        this.gameBoard = createGameBoard(builder);

        // Assign the En Passant pawn (nullable)
        this.enPassantPawn = builder.enPassantPawn;

        this.whitePieces = calculateActivePieces(this.gameBoard, Alliance.WHITE);
        this.blackPieces = calculateActivePieces(this.gameBoard, Alliance.BLACK);

        // Call the REAL calculation method
        final Collection<Move> whiteStandardLegalMoves = calculateLegalMoves(this.whitePieces);
        final Collection<Move> blackStandardLegalMoves = calculateLegalMoves(this.blackPieces);

        this.whitePlayer = new WhitePlayer(this, whiteStandardLegalMoves, blackStandardLegalMoves);
        this.blackPlayer = new BlackPlayer(this, whiteStandardLegalMoves, blackStandardLegalMoves);

        this.currentPlayer = builder.nextMoveMaker.choosePlayer(this.whitePlayer, this.blackPlayer);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 64; i++) {
            final String tileText = this.gameBoard.get(i).toString();
            builder.append(String.format("%3s", tileText));
            if ((i + 1) % 8 == 0) {
                builder.append("\n");
            }
        }
        return builder.toString();
    }

    // --HELPER METHODS--

    private static List<Square> createGameBoard(final Builder builder) {
        final List<Square> squares = new ArrayList<>(64);
        for (int i = 0; i < 64; i++) {
            squares.add(Square.createSquare(i, builder.boardConfig.get(i)));
        }
        return squares;
    }

    private static Collection<Piece> calculateActivePieces(final List<Square> gameBoard,
                                                           final Alliance alliance) {
        final List<Piece> activePieces = new ArrayList<>();
        for (final Square square : gameBoard) {
            if (square.isOccupied()) {
                final Piece piece = square.getPiece();
                if (piece.getPieceAlliance() == alliance) {
                    activePieces.add(piece);
                }
            }
        }
        return activePieces;
    }

    // --- THE FIXED METHOD ---
    private Collection<Move> calculateLegalMoves(final Collection<Piece> pieces) {
        final List<Move> legalMoves = new ArrayList<>();

        for (final Piece piece : pieces) {
            // We verify the piece calculation logic (Knight, Rook, etc.) here
            legalMoves.addAll(piece.calculateLegalMoves(this));
        }
        return legalMoves;
    }

    // --GETTERS--
    public Collection<Piece> getBlackPieces() {
        return this.blackPieces;
    }
    public Square getSquare(final int squareCoordinate) {
        return this.gameBoard.get(squareCoordinate);
    }
    public Collection<Piece> getWhitePieces() {
        return this.whitePieces;
    }
    public Pawn getEnPassantPawn() {
        return this.enPassantPawn;
    }
    public Player getCurrentPlayer() {
        return this.currentPlayer;
    }
    public BlackPlayer getBlackPlayer() {
        return this.blackPlayer;
    }
    public WhitePlayer getWhitePlayer() {
        return this.whitePlayer;
    }

    // -- BUILDER --
    public static class Builder {

        Map<Integer, Piece> boardConfig;
        Alliance nextMoveMaker;
        // FIXED: Change type to Pawn
        Pawn enPassantPawn;

        public Builder() {
            this.boardConfig = new HashMap<>();
        }

        public Builder setPiece(final Piece piece) {
            this.boardConfig.put(piece.getPiecePosition(), piece);
            return this;
        }

        public Builder setNextMoveMaker(final Alliance nextMoveMaker) {
            this.nextMoveMaker = nextMoveMaker;
            return this;
        }

        // FIXED: Accept Pawn directly to match Board field
        public void setEnPassantPawn(Pawn enPassantPawn) {
            this.enPassantPawn = enPassantPawn;
        }

        public Board build() {
            return new Board(this);
        }
    }
}