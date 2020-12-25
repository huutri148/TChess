package com.tests.chess.engine.board;

import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.player.MoveTransition;
import com.chess.engine.player.ai.Minimax;
import com.chess.engine.player.ai.MoveStrategy;
import org.junit.Test;

import static org.junit.Assert.*;

public class BoardTest {
    @Test
    public void initialBoard() {

        final Board board = Board.createStandardBoard();
        assertEquals(board.currentPlayer().getLegalMoves().size(), 20);
        assertEquals(board.currentPlayer().getOpponent().getLegalMoves().size(), 20);
        assertFalse(board.currentPlayer().isInCheck());
        assertFalse(board.currentPlayer().isInCheckMate());
        assertFalse(board.currentPlayer().isCastled());
//        assertTrue(board.currentPlayer().isKingSideCastleCapable());
//        assertTrue(board.currentPlayer().isQueenSideCastleCapable());
        assertEquals(board.currentPlayer(), board.getWhitePlayer());
        assertEquals(board.currentPlayer().getOpponent(), board.getBlackPlayer());
        assertFalse(board.currentPlayer().getOpponent().isInCheck());
        assertFalse(board.currentPlayer().getOpponent().isInCheckMate());
        assertFalse(board.currentPlayer().getOpponent().isCastled());
//        assertTrue(board.currentPlayer().getOpponent().isKingSideCastleCapable());
//        assertTrue(board.currentPlayer().getOpponent().isQueenSideCastleCapable());
//        assertTrue(board.whitePlayer().toString().equals("White"));
//        assertTrue(board.blackPlayer().toString().equals("Black"));
    }
    @Test
    public void testFoolsMate(){
        final Board board = Board.createStandardBoard();
        final MoveTransition t1 = board.currentPlayer()
                .makeMove(Move.MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("f2"),
                        BoardUtils.getCoordinateAtPosition("f3")));
        assertTrue(t1.getMoveStatus().isDone());

        final MoveTransition t2 = t1.getBoard()
                .currentPlayer()
                .makeMove(Move.MoveFactory.createMove(t1.getBoard(), BoardUtils.getCoordinateAtPosition("e7"),
                        BoardUtils.getCoordinateAtPosition("e5")));
        assertTrue(t2.getMoveStatus().isDone());

        final MoveTransition t3 = t2.getBoard()
                .currentPlayer()
                .makeMove(Move.MoveFactory.createMove(t2.getBoard(), BoardUtils.getCoordinateAtPosition("g2"),
                        BoardUtils.getCoordinateAtPosition("g4")));
        assertTrue(t3.getMoveStatus().isDone());

        final MoveStrategy strategy = new Minimax(4);

        final Move aiMove = strategy.execute(t3.getBoard());
        final  Move bestMove = Move.MoveFactory.createMove(t3.getBoard(), BoardUtils.getCoordinateAtPosition("d8"),
                        BoardUtils.getCoordinateAtPosition("h4"));
        assertEquals(aiMove, bestMove);
    }
}