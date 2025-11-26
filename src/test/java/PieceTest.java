import entities.Alliance;
import entities.Board;

import entities.King;
import entities.Rook;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PieceTest {

    @Test
    public void testRookMovesOnEmptyBoard() {
        final Board.Builder builder = new Board.Builder();

        // 1. The Rook we want to test (at D4 / 35)
        builder.setPiece(new Rook(35, Alliance.WHITE));

        // 2. Place it far away (e.g., corner) so it doesn't block the Rook
        builder.setPiece(new King(63, Alliance.WHITE));

        // 3. Add a Black King too (BlackPlayer needs one)
        builder.setPiece(new King(4, Alliance.BLACK));

        builder.setNextMoveMaker(Alliance.WHITE);

        final Board board = builder.build();

        // Get moves specifically for the Rook (filter out King moves)
        // expect 14 moves for the Rook + 3 moves for the King in the corner = 17 total
        assertEquals(14 + 3, board.getCurrentPlayer().getLegalMoves().size());
    }
}