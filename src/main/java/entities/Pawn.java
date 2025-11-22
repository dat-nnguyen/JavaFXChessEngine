package entities;

import core.Move;
import core.Move.*; // Import all inner move classes
import utils.BoardUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Pawn extends Piece {

    private final static int[] CANDIDATE_MOVE_COORDINATES = {8, 16, 7, 9};

    public Pawn(final int piecePosition, final Alliance pieceAlliance) {
        super(piecePosition, pieceAlliance, PieceType.PAWN, true);
    }

    public Pawn(final int piecePosition, final Alliance pieceAlliance, final boolean isFirstMove) {
        super(piecePosition, pieceAlliance, PieceType.PAWN, isFirstMove);
    }

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {

        final List<Move> legalMoves = new ArrayList<>();

        for (final int currentCandidateOffset : CANDIDATE_MOVE_COORDINATES) {

            // Calculate destination based on Alliance Direction (-1 or 1)
            final int candidateDestinationCoordinate = this.getPiecePosition() + (this.getPieceAlliance().getDirection() * currentCandidateOffset);

            if (!BoardUtils.isValidSquareCoordinate(candidateDestinationCoordinate)) {
                continue;
            }

            // --- BEHAVIOR 1: SINGLE STEP (Offset 8) ---
            if (currentCandidateOffset == 8 && !board.getSquare(candidateDestinationCoordinate).isOccupied()) {

                // If this is a promotion, wrap it!
                if (this.getPieceAlliance().isPawnPromotionSquare(candidateDestinationCoordinate)) {
                    legalMoves.add(new PawnPromotion(new PawnMove(board, this, candidateDestinationCoordinate)));
                } else {
                    legalMoves.add(new PawnMove(board, this, candidateDestinationCoordinate));
                }
            }

            // --- BEHAVIOR 2: DOUBLE JUMP (Offset 16) ---
            else if (currentCandidateOffset == 16 && this.isFirstMove() &&
                    ((BoardUtils.SEVENTH_RANK[this.getPiecePosition()] && this.getPieceAlliance().isBlack()) ||
                            (BoardUtils.SECOND_RANK[this.getPiecePosition()] && this.getPieceAlliance().isWhite()))) {

                // Calculate the square we are jumping over
                final int behindCandidateDestinationCoordinate = this.getPiecePosition() + (this.getPieceAlliance().getDirection() * 8);

                // Both the jump target AND the square in between must be empty
                if (!board.getSquare(behindCandidateDestinationCoordinate).isOccupied() &&
                        !board.getSquare(candidateDestinationCoordinate).isOccupied()) {

                    legalMoves.add(new PawnJump(board, this, candidateDestinationCoordinate));
                }
            }

            // --- BEHAVIOR 3: ATTACK & EN PASSANT (Offset 7 & 9) ---
            else if (currentCandidateOffset == 7 &&
                    !((BoardUtils.EIGHTH_COLUMN[this.getPiecePosition()] && this.getPieceAlliance().isWhite()) ||
                            (BoardUtils.FIRST_COLUMN[this.getPiecePosition()] && this.getPieceAlliance().isBlack()))) {

                if (board.getSquare(candidateDestinationCoordinate).isOccupied()) {
                    final Piece pieceOnCandidate = board.getSquare(candidateDestinationCoordinate).getPiece();
                    if (this.getPieceAlliance() != pieceOnCandidate.getPieceAlliance()) {
                        // if attack Check promotion again
                        if (this.getPieceAlliance().isPawnPromotionSquare(candidateDestinationCoordinate)) {
                            legalMoves.add(new PawnPromotion(new PawnAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate)));
                        } else {
                            legalMoves.add(new PawnAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate));
                        }
                    }
                } else if (board.getEnPassantPawn() != null) {
                    // Check En Passant
                    if (board.getEnPassantPawn().getPiecePosition() == (this.getPiecePosition() + (this.getPieceAlliance().getOppositeDirection() * 1))) {
                        final Piece pieceOnCandidate = board.getEnPassantPawn();
                        if (this.getPieceAlliance() != pieceOnCandidate.getPieceAlliance()) {
                            legalMoves.add(new PawnEnPassantAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate));
                        }
                    }
                }
            }
            else if (currentCandidateOffset == 9 &&
                    !((BoardUtils.FIRST_COLUMN[this.getPiecePosition()] && this.getPieceAlliance().isWhite()) ||
                            (BoardUtils.EIGHTH_COLUMN[this.getPiecePosition()] && this.getPieceAlliance().isBlack()))) {

                if (board.getSquare(candidateDestinationCoordinate).isOccupied()) {
                    final Piece pieceOnCandidate = board.getSquare(candidateDestinationCoordinate).getPiece();
                    if (this.getPieceAlliance() != pieceOnCandidate.getPieceAlliance()) {
                        if (this.getPieceAlliance().isPawnPromotionSquare(candidateDestinationCoordinate)) {
                            legalMoves.add(new PawnPromotion(new PawnAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate)));
                        } else {
                            legalMoves.add(new PawnAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate));
                        }
                    }
                } else if (board.getEnPassantPawn() != null) {
                    // Check En Passant
                    if (board.getEnPassantPawn().getPiecePosition() == (this.getPiecePosition() - (this.getPieceAlliance().getOppositeDirection() * 1))) {
                        final Piece pieceOnCandidate = board.getEnPassantPawn();
                        if (this.getPieceAlliance() != pieceOnCandidate.getPieceAlliance()) {
                            legalMoves.add(new PawnEnPassantAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate));
                        }
                    }
                }
            }
        }
        return legalMoves;
    }

    @Override
    public Pawn movePiece(final Move move) {
        return new Pawn(move.getDestinationCoordinate(), move.getMovedPiece().getPieceAlliance());
    }

    @Override
    public String toString() {
        return PieceType.PAWN.toString();
    }

    public Piece getPromotionPiece() {
        return new Queen(this.getPiecePosition(), this.getPieceAlliance(), false);
    }
}