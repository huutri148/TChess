package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

import java.util.Collection;

public abstract class Piece {


    protected final PieceType pieceType;
    protected final int piecePosition;
    protected final Alliance pieceAlliance;
    protected boolean isFirstMove;
    Piece(final PieceType pieceType,
            final Alliance pieceAlliance,
          final int piecePosition){
        this.pieceType = pieceType;
        this.piecePosition = piecePosition;
        this.pieceAlliance = pieceAlliance;
        this.isFirstMove = false;
    }
    public int getPiecePosition(){
        return this.piecePosition;
    }
    public Alliance getPieceAlliance(){
        return pieceAlliance;
    }
    public abstract Collection<Move> calculateLegalMoves(final Board board);

    public enum PieceType{
        PAWN("P"){
            @Override
            public boolean isKing() {
                return false;
            }
        },
        KNIGHT("N") {
            @Override
            public boolean isKing() {
                return false;
            }
        },
        BISHOP("B") {
            @Override
            public boolean isKing() {
                return false;
            }
        },
        ROOK("R") {
            @Override
            public boolean isKing() {
                return false;
            }
        },
        QUEEN("Q") {
            @Override
            public boolean isKing() {
                return false;
            }
        },
        KING("K") {
            @Override
            public boolean isKing() {
                return true;
            }
        };

        private String pieceName;

        PieceType(final String pieceName){
            this.pieceName = pieceName;
        }

        @Override
        public String toString() {
            return this.pieceName;
        }
        public abstract boolean isKing();

    }
    public PieceType getPieceType() {
        return pieceType;
    }

}
