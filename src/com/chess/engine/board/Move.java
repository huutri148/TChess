package com.chess.engine.board;

import com.chess.engine.pieces.Pawn;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Rook;
import com.chess.engine.player.MoveStatus;

public abstract class Move {
    protected final Board board;
    protected final Piece movedPiece;
    protected final int destinationCoordinate;
    protected final boolean isFirstMove;

    public static final Move NULL_MOVE = new NullMove();
    Move(final Board board,
         final Piece movedPiece,
         final int destinationCoordinate){
        this.board = board;
        this.movedPiece = movedPiece;
        this.destinationCoordinate = destinationCoordinate;
        this.isFirstMove = movedPiece.isFirstMove();
    }
    private Move(final Board board,
                 final int destinationCoordinate){
        this.board = board;
        this.destinationCoordinate = destinationCoordinate;
        this.movedPiece = null;
        this.isFirstMove = false;
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;

        result = prime * result + this.destinationCoordinate;
        result = prime * result + this.movedPiece.hashCode();
        result = prime * result + this.movedPiece.getPiecePosition();
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
       if(this == obj){
           return true;
       }
       if(!(obj instanceof Move)){
           return false;
       }
       final Move otherMove = (Move)obj;
       return   getCurrentCoordinate() == ((Move) obj).getCurrentCoordinate() &&
               getDestinationCoordinate() == otherMove.getDestinationCoordinate()
               && getMovedPiece().equals(otherMove.getMovedPiece());
    }

    public int getCurrentCoordinate(){
        return this.movedPiece.getPiecePosition();
    }
    public boolean isAttack(){
        return false;
    }
    public boolean isCastlingMove(){
        return false;
    }
    public Piece getAttackedPiece(){
        return null;
    }
    public int getDestinationCoordinate() {
        return destinationCoordinate;
    }
    public Piece getMovedPiece() {
        return movedPiece;
    }

    public Board execute() {
        final Board.Builder builder = new Board.Builder();
        for (final Piece piece : this.board.currentPlayer().getActivePieces()){
            if(!this.movedPiece.equals(piece)){
                builder.setPiece(piece);
            }
        }
        for (final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()){
            builder.setPiece(piece);
        }
        //move the moved piece
        builder.setPiece(this.movedPiece.movePiece(this));
        builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());

        return builder.build();
    }

    public static final class MajorMove extends Move{

        public MajorMove(final Board board,
                  final Piece movedPiece,
                  final int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }

        @Override
        public boolean equals( final Object obj) {
            return this == obj || obj instanceof  MajorMove && super.equals(obj);
        }

        @Override
        public String toString() {
            return movedPiece.getPieceType().toString() +
                    BoardUtils.getPositionAtCoordinate(this.destinationCoordinate);
        }
    }



    public static  class AttackMove extends Move {
        final Piece attackedPiece;

        public AttackMove(final Board board,
                          final Piece movedPiece,
                          final int destinationCoordinate,
                          final Piece attackedPiece) {
            super(board, movedPiece, destinationCoordinate);
            this.attackedPiece = attackedPiece;
        }

        @Override
        public int hashCode() {
           return this.attackedPiece.hashCode() + super.hashCode();
        }

        @Override
        public boolean equals(final Object obj) {
            if(this == obj){
                return true;
            }
            if(!(obj instanceof AttackMove)){
                return false;
            }
            final AttackMove otherAttackMove = (AttackMove) obj;
            return super.equals(otherAttackMove) && getAttackedPiece().equals(otherAttackMove.getAttackedPiece());
        }

        @Override
        public Board execute() {

            return null;
        }

        @Override
        public boolean isAttack() {
           return true;
        }

        @Override
        public Piece getAttackedPiece() {
           return this.attackedPiece;
        }
    }
    public static final class PawnMove extends Move{

        PawnMove(final Board board,final  Piece movedPiece,final int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }
    }
    public static class PawnAttackMove extends AttackMove{

        public PawnAttackMove(final Board board,final Piece movedPiece,final int destinationCoordinate,final Piece attackedPiece) {
            super(board, movedPiece, destinationCoordinate, attackedPiece);
        }
    }
    public static final class PawnEnPassantAttackMove extends PawnAttackMove{

        public PawnEnPassantAttackMove(final Board board,final Piece movedPiece,final int destinationCoordinate, Piece attackedPiece) {
            super(board, movedPiece, destinationCoordinate, attackedPiece);
        }
    }
    public static class PawnJump extends Move{

        public PawnJump(final Board board, final Piece movedPiece, final int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }

        @Override
        public Board execute() {
           final Board.Builder builder = new Board.Builder();
           for(final Piece piece : this.board.currentPlayer().getActivePieces()){
               if(!this.movedPiece.equals(piece)){
                   builder.setPiece(piece);
               }
           }
           for(final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()){
               builder.setPiece(piece);
           }
           final Pawn movedPawn = (Pawn) this.movedPiece.movePiece(this);
           builder.setPiece(movedPawn);
           builder.setEnPassantPawn(movedPawn);
           builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
           return builder.build();
        }
    }
    static class CastleMove extends Move{

        protected final Rook castleRook;
        protected final int castleRookStart;
        protected final int castleRookDestination;
        CastleMove(final Board board,final Piece movedPiece,final int destinationCoordinate
        , final Rook castleRook, final int castleRookStart, final int castleRookDestination) {
            super(board, movedPiece, destinationCoordinate);
            this.castleRook = castleRook;
            this.castleRookStart = castleRookStart;
            this.castleRookDestination = castleRookDestination;
        }
        public Rook getCastleRook(){
            return  this.castleRook;
        }

        @Override
        public boolean isCastlingMove() {
            return true;
        }

        @Override
        public Board execute() {
           final Board.Builder builder = new Board.Builder();
           for(final Piece piece : this.board.currentPlayer().getActivePieces()){
               if(!this.movedPiece.equals(piece) && !this.castleRook.equals(piece))
                   builder.setPiece(piece);
           }
           for(final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()){
               builder.setPiece(piece);
           }
           builder.setPiece(this.movedPiece.movePiece(this));

           //Todo look into the first move
           builder.setPiece(new Rook(this.castleRook.getPieceAlliance(),this.castleRookDestination));
           builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
           return builder.build();
        }
    }
    public static final class KingSideCastleMove extends CastleMove{

        public KingSideCastleMove(final Board board, final Piece movedPiece,
                                  final int destinationCoordinate, final Rook castleRook, final int castleRookStart, final int castleRookDestination) {
            super(board, movedPiece, destinationCoordinate,castleRook,castleRookStart,castleRookDestination);
        }
    }
    public static final class  QueenSideCastleMove extends CastleMove{

        public QueenSideCastleMove(final Board board, final Piece movedPiece, final int destinationCoordinate,
                                   final Rook castleRook, final int castleRookStart, final int castleRookDestination) {
            super(board, movedPiece, destinationCoordinate,castleRook,castleRookStart,castleRookDestination);
        }
    }
    public static final class NullMove extends Move{

        NullMove() {
            super(null,null,-1);
        }

        @Override
        public Board execute() {
           throw new RuntimeException("cannot execute the null move!");
        }
    }
    public static class MoveFactory{
        private MoveFactory(){
            throw  new RuntimeException(("Not instaniable!"));
        }
        public static Move createMove(final Board board,
                                      final int currentCoordinate,
                                      final int destinationCoordinate){
            for(final Move move : board.getAllLegalMoves()){
                if(move.getCurrentCoordinate() == destinationCoordinate){
                    return move;
                }
            }
            return NULL_MOVE;
        }
    }
}
