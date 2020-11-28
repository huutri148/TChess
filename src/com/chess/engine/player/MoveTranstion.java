package com.chess.engine.player;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

public class MoveTranstion {
    private final Board transitionBoard;
    private final Move move;
    private final MoveStatus moveStatus;
    public MoveTranstion(final Board transtionBoard,
                         final Move move,
                         final MoveStatus moveStatus){
        this.transitionBoard = transtionBoard;
        this.move = move;
        this.moveStatus = moveStatus;
    }

    public MoveStatus getMoveStatus() {
        return moveStatus;
    }
}
