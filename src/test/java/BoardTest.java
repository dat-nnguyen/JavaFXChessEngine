import entities.Board;
import core.Move;
import entities.MoveTransition;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    @Test
    public void testInitialBoard() {
        final Board board = Board.createStandardBoard();

        // Check Piece Counts
        assertEquals(16, board.getWhitePieces().size());
        assertEquals(16, board.getBlackPieces().size());

        // Check Player Turn
        assertTrue(board.getCurrentPlayer().getAlliance().isWhite());
        assertFalse(board.getCurrentPlayer().getAlliance().isBlack());

        // Check Total Legal Moves (Standard Chess opening has 20 moves: 16 pawn + 4 knight)
        assertEquals(20, board.getCurrentPlayer().getLegalMoves().size());
        assertEquals(20, board.getCurrentPlayer().getOpponent().getLegalMoves().size());
    }

    @Test
    public void testFoolsMate() {
        final Board board = Board.createStandardBoard();

        // White moves Pawn f2 -> f3
        final Move t1 = Move.MoveFactory.createMove(board, 53, 45);
        final MoveTransition transition1 = board.getCurrentPlayer().makeMove(t1);
        assertTrue(transition1.getMoveStatus().isDone());

        // Black moves Pawn e7 -> e5
        final Board board2 = transition1.getTransitionBoard();
        final Move t2 = Move.MoveFactory.createMove(board2, 12, 28);
        final MoveTransition transition2 = board2.getCurrentPlayer().makeMove(t2);
        assertTrue(transition2.getMoveStatus().isDone());

        // White moves Pawn g2 -> g4
        final Board board3 = transition2.getTransitionBoard();
        final Move t3 = Move.MoveFactory.createMove(board3, 54, 38);
        final MoveTransition transition3 = board3.getCurrentPlayer().makeMove(t3);
        assertTrue(transition3.getMoveStatus().isDone());

        // Black moves Queen d8 -> h4 (Index 3 -> 39) -> CHECKMATE
        final Board board4 = transition3.getTransitionBoard();
        final Move t4 = Move.MoveFactory.createMove(board4, 3, 39);
        final MoveTransition transition4 = board4.getCurrentPlayer().makeMove(t4);
        assertTrue(transition4.getMoveStatus().isDone());

        final Board board5 = transition4.getTransitionBoard();
        assertTrue(board5.getCurrentPlayer().isInCheckMate());
    }
}