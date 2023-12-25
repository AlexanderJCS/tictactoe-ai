package ai;

import board.Board;
import board.BoardSpace;

import java.util.ArrayList;
import java.util.List;


public abstract class Computer {
    protected List<BoardSpace> getEmptySpaces(Board board) {
        List<BoardSpace> spaces = new ArrayList<>();

        for (BoardSpace[] row : board.getBoardSpaces()) {
            for (BoardSpace space : row) {
                if (space.getState() == BoardSpace.BoardState.EMPTY) {
                    spaces.add(space);
                }
            }
        }

        return spaces;
    }

    public abstract void makeMove(Board board, BoardSpace.BoardState playingAs);
}
