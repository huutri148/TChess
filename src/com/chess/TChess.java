package com.chess;

import com.chess.engine.board.Board;
import com.chess.engine.gui.Table;

import java.io.IOException;

public class TChess {
    public static void main(String[] args) throws IOException {
        Board board = Board.createStandardBoard();
        System.out.println(board);
        Table table = new Table();
    }
}
