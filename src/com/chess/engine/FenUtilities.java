package com.chess.engine;

import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.pieces.Pawn;

public class FenUtilities {
    private FenUtilities (){
        throw new RuntimeException("Not instantiable");
    }
    public static Board createGameFromFEN(final String fenString){
        return null;
    }

    public static String createFEBFromGame(final Board board){
        return calculateBoardText(board) + " " +
                calculateCurrentPlayerText(board) + " " +
                calculateCastleText(board) +
                calculateEnPassantSquare(board ) + " " +
                "0 1";
    }

    private static String calculateBoardText(final Board board) {
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < BoardUtils.NUM_TILES; i++){
            final String tileText = board.getTile(i).toString();
            builder.append(tileText);
        }
        builder.insert(8,"/");
        builder.insert(17,"/");
        builder.insert(26,"/");
        builder.insert(35,"/");
        builder.insert(44,"/");
        builder.insert(53,"/");
        builder.insert(62,"/");

        return builder.toString().replaceAll("--------","8")
                                 .replaceAll("-------","7")
                                 .replaceAll("------","6")
                                 .replaceAll("-----","5")
                                 .replaceAll("----","4")
                                 .replaceAll("---","3")
                                 .replaceAll("--","2")
                                 .replaceAll("-","1");

    }

    private static String calculateEnPassantSquare(final Board board){
        final Pawn enPassantPawn = board.getEnPassantPawn();


        if (enPassantPawn != null){
            return BoardUtils.getPositionAtCoordinate(enPassantPawn.getPiecePosition() + (8) *
                    enPassantPawn.getPieceAlliance().getOppositeDirection());
        }
        return "-";
    }




    private static String calculateCastleText(final Board board) {
        final StringBuilder builder = new StringBuilder();

        if (board.getWhitePlayer().isKingSideCastleCapable()){
            builder.append("K");
        }

        if (board.getWhitePlayer().isQueenSideCastleCapable()){
            builder.append("Q");
        }


        if (board.getBlackPlayer().isKingSideCastleCapable()){
            builder.append("k");
        }

        if (board.getBlackPlayer().isQueenSideCastleCapable()){
            builder.append("q");
        }

        final String result = builder.toString();

        return result.isEmpty() ? "-" : result;
    }

    private static String calculateCurrentPlayerText(final Board board){
        return board.currentPlayer().toString().substring(0, 1).toLowerCase();
    }


}
