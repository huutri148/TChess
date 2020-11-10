package com.chess.engine.board;

public class BoardUtils {
    public static final boolean[] FIRST_COLUMN = initColumn(0) ;
    public static final boolean[] SECOND_COLUMN = initColumn(1) ;
    public static final boolean[] SEVENTH_COLUMN = initColumn(6) ;
    public static final boolean[] EIGHTH_COLUMN = initColumn(7) ;

    public static final boolean[] SECOND_ROW = null;
    public static final boolean[] SEVENTH_ROW = null;
    public static final int NUM_TITLES = 64;
    public static final int NUM_TILES_PER_ROW = 8;
    private BoardUtils(){
        throw new RuntimeException("You can not instantiate me!");
    }
    private static boolean[] initColumn(int columnNumber) {
        final boolean[] column = new boolean[NUM_TITLES];
        do{
            column[columnNumber] = true;
            columnNumber += NUM_TILES_PER_ROW;
        }while (columnNumber < NUM_TITLES);
        return column;
    }
    public static boolean isValidTileCoordinate(int coordinate) {
        return coordinate >= 0 && coordinate < NUM_TITLES;
    }
}
