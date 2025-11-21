package entities;

import core.Move;
import core.Move.AttackMove;
import core.Move.MajorMove;
import utils.BoardUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Knight extends Piece {
    // fix 8 moves that Knight can make
    private final static int[] CANDIDATE_MOVES_COORINATES = {-17, -15, -10, -6, 6, 10, 15, 17};

    public Knight(final int piecePosition, final Alliance pieceAlliance){
        super(piecePosition, pieceAlliance, PieceType.KNIGHT, true);
    }

    public Knight(final int piecePosition, final Alliance pieceAlliance, final boolean isFirstMove){
        super(piecePosition, pieceAlliance, PieceType.KNIGHT, isFirstMove);
    }

    private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset) {
        return BoardUtils.FIRST_COLUMN[currentPosition] &&
                (candidateOffset == -17 || candidateOffset == -10 || candidateOffset == 6 || candidateOffset == 15);
    }

    private static boolean isSecondColumnExclusion(final int currentPosition, final int candidateOffset) {
        return BoardUtils.SECOND_COLUMN[currentPosition] &&
                (candidateOffset == -10 || candidateOffset == 6);
    }

    private static boolean isSeventhColumnExclusion(final int currentPosition, final int candidateOffset) {
        return BoardUtils.SEVENTH_COLUMN[currentPosition] &&
                (candidateOffset == -6 || candidateOffset == 10);
    }

    private static boolean isEighthColumnExclusion(final int currentPosition, final int candidateOffset) {
        return BoardUtils.EIGHTH_COLUMN[currentPosition] &&
                (candidateOffset == -15 || candidateOffset == -6 || candidateOffset == 10 || candidateOffset == 17);
    }


    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {
        List<Move> legalMoves = new ArrayList<>();

        for(int currentCandidateOffset : CANDIDATE_MOVES_COORINATES){
            final int candidateDestinationCoordinate = this.getPiecePosition() + currentCandidateOffset;
            // check valid square
            // if res < 0 or res >= 64, continue
            if (!BoardUtils.isValidSquareCoordinate(candidateDestinationCoordinate)) {
                continue;
            }
            // the teleport fix
            if (isFirstColumnExclusion(this.getPiecePosition(), currentCandidateOffset) ||
                    isSecondColumnExclusion(this.getPiecePosition(), currentCandidateOffset) ||
                    isSeventhColumnExclusion(this.getPiecePosition(), currentCandidateOffset) ||
                    isEighthColumnExclusion(this.getPiecePosition(), currentCandidateOffset)) {
                continue;
            }
            final Square candidateSquare = board.getSquare(candidateDestinationCoordinate);

            if (!candidateSquare.isOccupied()) {
                // Empty Square = Major Move
                legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
            } else {
                // Occupied Square = Attack Move
                final Piece pieceAtDestination = candidateSquare.getPiece();
                final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance();

                if (this.getPieceAlliance() != pieceAlliance) {
                    legalMoves.add(new AttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination));
                }
            }
        }
        return legalMoves ;
    }

    @Override
    public Knight movePiece(final Move move){
        return new Knight(move.getDestinationCoordinate(), move.getMovedPiece().getPieceAlliance());
    }
}
