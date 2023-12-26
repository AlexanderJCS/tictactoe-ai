package tictactoe.ai;

import tictactoe.Turn;
import tictactoe.board.Board;
import tictactoe.board.BoardSpace;
import tictactoe.board.BoardState;

import java.util.ArrayList;
import java.util.List;


public abstract class Computer implements Runnable {
    protected Board board;
    protected Turn playingAs;

    /**
     * Sets values to null, but you are expected to use setBoard() and setPlayingAs() before calling run()
     */
    public Computer() {
        this(null, null);
    }

    public Computer(Board board, Turn playingAs) {
        this.board = board;
        this.playingAs = playingAs;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public void setPlayingAs(Turn playingAs) {
        this.playingAs = playingAs;
    }

    protected List<BoardSpace> getEmptySpaces() {
        List<BoardSpace> spaces = new ArrayList<>();

        for (BoardSpace[] row : this.board.getBoardSpaces()) {
            for (BoardSpace space : row) {
                if (space.getState() == BoardState.EMPTY) {
                    spaces.add(space);
                }
            }
        }

        return spaces;
    }
}
