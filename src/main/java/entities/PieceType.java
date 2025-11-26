package entities;

/**
 * Enum representing all chess piece types.
 * Each piece has a string identifier and a standard value for AI evaluation.
 */
public enum PieceType {

    PAWN("P", 100) {
        @Override
        public boolean isKing() { return false; }
        @Override
        public boolean isRook() { return false; }
    },
    KNIGHT("N", 300) {
        @Override
        public boolean isKing() { return false; }
        @Override
        public boolean isRook() { return false; }
    },
    BISHOP("B", 300) {
        @Override
        public boolean isKing() { return false; }
        @Override
        public boolean isRook() { return false; }
    },
    ROOK("R", 500) {
        @Override
        public boolean isKing() { return false; }
        @Override
        public boolean isRook() { return true; }
    },
    QUEEN("Q", 900) {
        @Override
        public boolean isKing() { return false; }
        @Override
        public boolean isRook() { return false; }
    },
    KING("K", 10000) {
        @Override
        public boolean isKing() { return true; }
        @Override
        public boolean isRook() { return false; }
    };

    private final String pieceName;
    private final int pieceValue;

    /**
     * Constructor for piece type.
     *
     * @param pieceName  the single-letter representation of the piece (e.g., "P" for pawn)
     * @param pieceValue the numeric value of the piece for evaluation purposes
     */
    PieceType(String pieceName, int pieceValue) {
        this.pieceName = pieceName;
        this.pieceValue = pieceValue;
    }

    /**
     * Returns the string identifier of the piece.
     *
     * @return single-letter piece name
     */
    @Override
    public String toString() {
        return this.pieceName;
    }

    /**
     * Returns the value of the piece for evaluation in AI or scoring.
     *
     * @return the integer value of the piece
     */
    public int getPieceValue() {
        return this.pieceValue;
    }

    // Abstract methods to enforce implementation (optional but good for safety)
    // Or use default "false" implementation

    public abstract boolean isKing();
    public abstract boolean isRook();
}