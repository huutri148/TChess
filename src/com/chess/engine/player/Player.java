package com.chess.engine.player;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.pieces.King;
import com.chess.engine.pieces.Piece;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class Player {
    protected final Board board;
    protected final King playerKing;
    protected final Collection<Move> legalMoves;
    private final boolean isInCheck;
    Player(final Board board,
           final Collection<Move> legalMoves,
           final Collection<Move> opponentMoves){
        this.board = board;
        this.playerKing = establishKing();
        // calculate and concat so the legal moves list don't have
        // the King Castle move
        this.legalMoves = ImmutableList.copyOf(Iterables.concat(legalMoves,calculateKingCastles(legalMoves, opponentMoves)));
        this.isInCheck = !Player.calculateAttackOnTile(this.playerKing.getPiecePosition(),
                opponentMoves).isEmpty();
    }

    public static Collection<Move> calculateAttackOnTile(int piecePosition,
                                                           Collection<Move> opponentMoves) {
        final List<Move> attackMoves = new ArrayList<>();
        for(final Move move : opponentMoves){
            if(piecePosition == move.getDestinationCoordinate()){
                attackMoves.add(move);
            }
        }
        return ImmutableList.copyOf(attackMoves);
    }

    private King establishKing() {
        for(final Piece piece : getActivePieces() ){
            if(piece.getPieceType().isKing()){
                return (King)piece;
            }

        }
        throw new RuntimeException("Should not reach here! Not a valid board!");
    }
    public boolean isMoveLegal(final Move move){
        return this.legalMoves.contains(move);
    }
    public King getPlayerKing() {
        return playerKing;
    }

    // Todo: implement these method below!!!
    public boolean isInCheck(){
        return isInCheck;
    }
    public boolean isInCheckMate(){
        return this.isInCheck && !hasEscapeMoves();
    }
    public  Collection<Move> getLegalMoves(){
        return legalMoves;
    }
    protected boolean hasEscapeMoves() {
        for (final Move move : this.legalMoves){
            final MoveTransition transtion = makeMove(move);
            if(transtion.getMoveStatus().isDone()){
                return true;
            }
        }
        return false;
    }

    // all of the move will be in the checkmate position
    public boolean inInStaleMate(){
        return !this.isInCheck && !hasEscapeMoves();
    }
    public boolean isCastled(){
        return false;
    }

    public MoveTransition makeMove(final Move move){
        if (!isMoveLegal(move)){
            return new MoveTransition(this.board,move,MoveStatus.ILLEGAL_MOVE);
        }

        final Board transitionBoard = move.execute();

        final Collection<Move> kingAttacks = Player.calculateAttackOnTile(transitionBoard.currentPlayer().
                getOpponent().getPlayerKing().getPiecePosition(),
                transitionBoard.currentPlayer().getLegalMoves());
        if(!kingAttacks.isEmpty()){
            return new MoveTransition(this.board, move,MoveStatus.LEAVES_PLAYER_IN_CHECK);
        }
        return new MoveTransition(transitionBoard, move, MoveStatus.DONE);
    }

    public abstract Collection<Piece> getActivePieces();
    public abstract Alliance getAlliance();
    public abstract Player getOpponent();

    protected abstract Collection<Move> calculateKingCastles(Collection<Move> playerLegals,
                                                             Collection<Move> opponentsLegals);


}
