package tictactoe.ai;

import tictactoe.board.Board;
import tictactoe.board.BoardSpace;
import tictactoe.board.BoardState;

import java.util.ArrayList;
import java.util.List;


public abstract class Computer {
    protected List<BoardSpace> getEmptySpaces(Board board) {
        List<BoardSpace> spaces = new ArrayList<>();

        for (BoardSpace[] row : board.getBoardSpaces()) {
            for (BoardSpace space : row) {
                if (space.getState() == BoardState.EMPTY) {
                    spaces.add(space);
                }
            }
        }

        return spaces;
    }

    public abstract void makeMove(Board board, BoardSpace.BoardState playingAs);
}
