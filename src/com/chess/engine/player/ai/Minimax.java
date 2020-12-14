package com.chess.engine.player.ai;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.player.MoveTransition;

public class Minimax implements MoveStrategy {
    private  final BoardEvaluator boardEvaluator;
    public Minimax(){
        this.boardEvaluator = null;
    }
    @Override
    public Move execute(Board board, int depth) {
        return null;
    }

    @Override
    public String toString() {
        return "MiniMax";
    }
    public int min(final Board board,
                   final int depth){
        if(depth == 0)
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
        if(depth == 0)
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
}
