package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.chess.engine.board.Move.*;

public class Knight extends Piece{

    private final static int[] CANDIDATE_MOVE_COODINATES = {-17, -15, -10, -6,
    6, 10, 15, 17};
    public Knight( final Alliance pieceAlliance,final int piecePosition) {
        super(PieceType.KNIGHT,pieceAlliance, piecePosition);
    }
    @Override
    public Knight movePiece(final Move move) {
        return new Knight(move.getMovedPiece().getPieceAlliance(), move.getDestinationCoordinate());
    }
    @Override
    public Collection<Move> calculateLegalMoves(Board board) {
        int candidateDestinationCoordinate;
        final List<Move> legalMoves = new ArrayList<>();

        for (final int currentCandidateOffset : CANDIDATE_MOVE_COODINATES){
            candidateDestinationCoordinate = this.piecePosition + currentCandidateOffset;
            if (BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
                if (isFirstColumnExclusion(this.piecePosition, currentCandidateOffset) ||
                isSecondColumnExclusion(this.piecePosition, currentCandidateOffset) ||
                isSevenColumnExclusion(this.piecePosition, currentCandidateOffset) ||
                isEightColumnExclusion(this.piecePosition, currentCandidateOffset)){
                    continue;
                }
                final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
                if (!candidateDestinationTile.isTileOccupied()){
                    legalMoves.add(new MajorMove(board, this , candidateDestinationCoordinate));
                } else {
                    final Piece pieceAtDestination = candidateDestinationTile.getPiece();
                    final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance();
                    if(this.pieceAlliance != pieceAlliance){
                        legalMoves.add(new AttackMove(board, this, candidateDestinationCoordinate,
                                pieceAtDestination));
                    }
                }

            }
        }
        return ImmutableList.copyOf(legalMoves);
    }
    private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset){
        return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset == -17 || candidateOffset == - 10 ||
                candidateOffset == 6 || candidateOffset == 15);

    }
    private static boolean isSecondColumnExclusion(final int currentPosition,
                                                   final int candidateOffset ){
        return BoardUtils.SECOND_COLUMN[currentPosition] && ( candidateOffset == -10 || candidateOffset == 6);
    }
    private static boolean isSevenColumnExclusion(final int currentPosition,
                                                  final int candidateOffset){

        return BoardUtils.SEVENTH_COLUMN[currentPosition] && (candidateOffset == 6 || candidateOffset == -10 );
    }
    private static boolean isEightColumnExclusion(final int currentPostion,
                                                  final int candidateOffset){
        return BoardUtils.EIGHTH_COLUMN[currentPostion]  && (candidateOffset == 17 || candidateOffset == 10 ||
                candidateOffset == -6 || candidateOffset == 15);
    }
    @Override
    public String toString() {
        return PieceType.KNIGHT.toString();
    }
}
