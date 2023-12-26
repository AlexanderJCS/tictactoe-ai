package tictactoe;

import tictactoe.board.BoardSpace;
import tictactoe.board.BoardState;

public enum Turn {
    X, O;

    public BoardState toBoardState() {
        return switch (this) {
            case X -> BoardState.X;
            case O -> BoardState.O;
        };
    }

    public Turn switchTurn() {
        return switch (this) {
            case X -> Turn.O;
            case O -> Turn.X;
        };
    }
}