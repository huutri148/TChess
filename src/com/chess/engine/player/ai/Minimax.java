package com.chess.engine.player.ai;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.player.MoveTransition;

public class Minimax implements MoveStrategy {
    private  final BoardEvaluator boardEvaluator;
    private final int searchDepth;
    public Minimax(final int searchDepth){
        this.boardEvaluator = new StandardBoardEvaluator();
        this.searchDepth = searchDepth;
    }
    @Override
    public Move execute(Board board) {
        final long startTime = System.currentTimeMillis();

        Move bestMove = null;
        int highestSeenValue = Integer.MIN_VALUE;
        int lowestSeenValue = Integer.MAX_VALUE;
        int currentValue;

        System.out.println(board.currentPlayer() + "THINKING with depth" + this.searchDepth);

        int numMoves = board.currentPlayer().getLegalMoves().size();
        for (final Move move : board.currentPlayer().getLegalMoves()){
            final MoveTransition moveTransition = board.currentPlayer().makeMove(move);
            if (moveTransition.getMoveStatus().isDone()){
                currentValue = board.currentPlayer().getAlliance().isWhite() ?
                        min(moveTransition.getBoard(), this.searchDepth -1) :
                        max(moveTransition.getBoard(), this.searchDepth -1);
                if (board.currentPlayer().getAlliance().isWhite() &&
                currentValue >= highestSeenValue){
                    highestSeenValue = currentValue;
                    bestMove = move;
                } else if (board.currentPlayer().getAlliance().isBlack() &&
                currentValue <= lowestSeenValue){
                    lowestSeenValue = currentValue;
                    bestMove = move;
                }
            }
        }
        final long executionTime = System.currentTimeMillis() - startTime;

        return bestMove;
    }

    @Override
    public String toString() {
        return "MiniMax";
    }
    public int min(final Board board,
                   final int depth){
        if(depth == 0 || isEndGameSceneario(board))
            return this.boardEvaluator.evaluate(board, depth);

        int lowestSeenValue = Integer.MAX_VALUE;
        for(final Move move : board.currentPlayer().getLegalMoves()){
            final MoveTransition moveTransition = board.currentPlayer().makeMove(move);
            if(moveTransition.getMoveStatus().isDone()){
                final int currentValue = max(moveTransition.getBoard(),depth - 1);
                    if(currentValue <= lowestSeenValue)
                        lowestSeenValue = currentValue;
                }
        }
        return lowestSeenValue;
    }
    public int max(final Board board,
                   final int depth){
        if(depth == 0 || isEndGameSceneario(board))
            return this.boardEvaluator.evaluate(board, depth);

        int highestSeenValue = Integer.MIN_VALUE;
        for(final Move move : board.currentPlayer().getLegalMoves()){
            final MoveTransition moveTransition = board.currentPlayer().makeMove(move);
            if(moveTransition.getMoveStatus().isDone()){
                final int currentValue = min(moveTransition.getBoard(),depth - 1);
                    if(currentValue >= highestSeenValue)
                        highestSeenValue = currentValue;
                }
            }
        return highestSeenValue;
    }

    private boolean isEndGameSceneario(final Board board) {
        return  board.currentPlayer().isInCheckMate() ||
                board.currentPlayer().inInStaleMate();
    }
}
