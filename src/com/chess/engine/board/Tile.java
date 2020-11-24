package com.chess.engine.board;

import com.chess.engine.pieces.Piece;
import com.google.common.collect.ImmutableMap;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class Tile {
    final protected int tileCoordinate;
    private static final Map<Integer, EmptyTile> EMPTY_TILES_CACHE = createAllPossibleEmptyTile();

    private static Map<Integer, EmptyTile> createAllPossibleEmptyTile() {
        final Map<Integer, EmptyTile> emptyTileMap = new HashMap<>();
        for (int i = 0; i < BoardUtils.NUM_TITLES; i++){
            emptyTileMap.put(i, new EmptyTile(i));
        }
        //Collections.unmodifiableMap(emptyTileMap);
        //use of immutable of a third library guava
        return ImmutableMap.copyOf(emptyTileMap);
    }
    public static Tile createTile(final int tileCoordinate, final Piece piece){
        return piece != null ? new OccupiedTile(tileCoordinate , piece) : EMPTY_TILES_CACHE.get(tileCoordinate);
    }
    private Tile(final int tileCoordinate){
        this.tileCoordinate = tileCoordinate;

    }


    public abstract boolean isTileOccupied();
    public abstract Piece getPiece();
    //Immutable class and static inner class
    public static final class EmptyTile extends Tile{
       private EmptyTile(final int coordinate){
            super(coordinate);
        }

        @Override
        public boolean isTileOccupied() {
            return false;

        }

        @Override
        public Piece getPiece() {
            return null;
        }

        @Override
        public String toString() {
            return "-";
        }
    }
    //Immutable class and static inner class
    public static final class OccupiedTile extends Tile{
        final private Piece pieceOnTile;

        private OccupiedTile(int tileCoordinate,final  Piece pieceOnTile){
            super(tileCoordinate);
            this.pieceOnTile = pieceOnTile;
        }

        @Override
        public String toString() {
           return getPiece().getPieceAlliance().isBlack() ? getPiece().toString().toLowerCase():
                   getPiece().toString();
        }

        @Override
        public boolean isTileOccupied() {
            return true;
        }

        @Override
        public Piece getPiece() {
            return this.pieceOnTile;
        }
    }
}
