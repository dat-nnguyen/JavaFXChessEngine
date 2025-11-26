import entities.*;
import core.Move;
import entities.MoveTransition;
import entities.Board;
import entities.PieceType;
import entities.Alliance;
import entities.King;
import entities.Pawn;
import entities.Rook;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MoveTest {

    @Test
    public void testCastling() {
        final Board.Builder builder = new Board.Builder();
        // Setup: White King (60) and Rook (63) with empty space between
        builder.setPiece(new King(60, Alliance.WHITE, true));
        builder.setPiece(new Rook(63, Alliance.WHITE, true));
        // Add a Black King just to make the board valid
        builder.setPiece(new King(4, Alliance.BLACK));

        builder.setNextMoveMaker(Alliance.WHITE);
        final Board board = builder.build();

        // Try to move King E1 -> G1 (Castle)
        final Move castleMove = Move.MoveFactory.createMove(board, 60, 62);

        assertTrue(castleMove.isCastlingMove());

        final MoveTransition transition = board.getCurrentPlayer().makeMove(castleMove);
        assertTrue(transition.getMoveStatus().isDone());

        final Board newBoard = transition.getTransitionBoard();

        // Verify King is at G1 (62)
        assertEquals(PieceType.KING, newBoard.getSquare(62).getPiece().getPieceType());
        // Verify Rook jumped to F1 (61)
        assertEquals(PieceType.ROOK, newBoard.getSquare(61).getPiece().getPieceType());
    }

    @Test
    public void testEnPassant() {
        final Board.Builder builder = new Board.Builder();
        // White Pawn at E5 (Index 28)
        builder.setPiece(new Pawn(28, Alliance.WHITE));
        // Black Pawn at D7 (Index 11), ready to jump
        builder.setPiece(new Pawn(11, Alliance.BLACK));
        builder.setPiece(new King(60, Alliance.WHITE));
        builder.setPiece(new King(4, Alliance.BLACK));

        builder.setNextMoveMaker(Alliance.BLACK);
        final Board board = builder.build();

        // 1. Black moves Pawn D7 -> D5 (Double Jump)
        // D5 is index 27
        final Move blackMove = Move.MoveFactory.createMove(board, 11, 27);
        final Board boardAfterJump = board.getCurrentPlayer().makeMove(blackMove).getTransitionBoard();

        // 2. White captures En Passant (E5 -> D6)
        // CRITICAL FIX: D6 is Index 19, NOT 20!
        final Move enPassantMove = Move.MoveFactory.createMove(boardAfterJump, 28, 19);

        // Assert: The engine recognizes this as an Attack Move
        assertTrue(enPassantMove.isAttack());

        final Board boardAfterCapture = boardAfterJump.getCurrentPlayer().makeMove(enPassantMove).getTransitionBoard();

        // Assert: The captured Black Pawn (at D5/27) is gone
        assertFalse(boardAfterCapture.getSquare(27).isOccupied());

        // Assert: The White Pawn is at D6/19
        assertTrue(boardAfterCapture.getSquare(19).isOccupied());
        assertEquals(PieceType.PAWN, boardAfterCapture.getSquare(19).getPiece().getPieceType());
    }
}