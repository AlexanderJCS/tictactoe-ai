package board;

import jangl.coords.WorldCoords;

import java.util.Arrays;

public class Board {
    private static final float PADDING = 0.02f;
    private final int size;
    private final Lines lines;

    private final BoardSpace[][] boardSpaces;

    public Board(int size) {
        this.size = size;
        this.boardSpaces = genBoardSpaces(size);
        this.lines = new Lines(size, PADDING);
    }

    private BoardSpace.BoardState[] getStates(BoardSpace[] spaces) {
        BoardSpace.BoardState[] states = new BoardSpace.BoardState[spaces.length];

        for (int i = 0; i < spaces.length; i++) {
            states[i] = spaces[i].getState();
        }

        return states;
    }

    public BoardSpace[] getCol(int col) {
        BoardSpace[] column = new BoardSpace[this.size];

        for (int i = 0; i < this.size; i++) {
            column[i] = this.boardSpaces[i][col];
        }

        return column;
    }

    public BoardSpace.BoardState[][] getRowsColsDiags() {
        BoardSpace.BoardState[][] rowsColsDiags = new BoardSpace.BoardState[this.size * 2 + 2][this.size];

        // Rows
        for (int i = 0; i < this.size; i++) {
            rowsColsDiags[i] = this.getStates(this.boardSpaces[i]);
        }

        // Columns
        for (int i = 0; i < this.size; i++) {
            rowsColsDiags[i + this.size] = this.getStates(this.getCol(i));
        }

        // Top left bottom right diagonal
        BoardSpace[] topLeftBottomRightDiag = new BoardSpace[this.size];
        for (int i = 0; i < this.size; i++) {
            topLeftBottomRightDiag[i] = this.boardSpaces[i][i];
        }

        rowsColsDiags[this.size * 2] = this.getStates(topLeftBottomRightDiag);

        // Top right bottom left diagonal
        BoardSpace[] topRightBottomLeftDiag = new BoardSpace[this.size];
        for (int i = 0; i < this.size; i++) {
            topRightBottomLeftDiag[i] = this.boardSpaces[i][this.size - i - 1];
        }

        rowsColsDiags[this.size * 2 + 1] = this.getStates(topRightBottomLeftDiag);

        return rowsColsDiags;
    }

    /**
     * Checks for a winner.
     * @return The state of the winner, or EMPTY if there is no winner. Null if there is a tie.
     */
    public BoardSpace.BoardState winner() {
        boolean tied = true;
        for (BoardSpace[] row : this.boardSpaces) {
            for (BoardSpace space : row) {
                if (space.getState() == BoardSpace.BoardState.EMPTY) {
                    tied = false;
                    break;
                }
            }

            if (!tied) {
                break;
            }
        }

        if (tied) {
            return null;
        }

        BoardSpace.BoardState[][] rowsColsDiags = this.getRowsColsDiags();

        for (BoardSpace.BoardState[] rowColDiag : rowsColsDiags) {
            boolean winner = true;
            for (int i = 1; i < rowColDiag.length; i++) {
                if (rowColDiag[i] != rowColDiag[0]) {
                    winner = false;
                    break;
                }
            }

            if (winner) {
                return rowColDiag[0];
            }
        }

        return BoardSpace.BoardState.EMPTY;
    }

    /**
     * Selects a space on the board.
     * @param state The state to set the space to.
     * @param location The location of the space.
     * @return Whether the space was selected.
     */
    public boolean selectSpace(BoardSpace.BoardState state, WorldCoords location) {
        for (BoardSpace[] row : this.boardSpaces) {
            for (BoardSpace space : row) {
                if (!space.collides(location)) {
                    continue;
                }

                // Do not override a space that is already selected
                if (space.getState() != BoardSpace.BoardState.EMPTY) {
                    return false;
                }

                space.setState(state);
                return true;
            }
        }

        return false;
    }

    public BoardSpace[][] getBoardSpaces() {
        return this.boardSpaces;
    }

    public void draw() {
        for (BoardSpace[] boardSpaceRow : this.boardSpaces) {
            for (BoardSpace boardSpace : boardSpaceRow) {
                boardSpace.draw();
            }
        }

        this.lines.draw();
    }

    private static BoardSpace[][] genBoardSpaces(int size) {
        BoardSpace[][] boardSpaces = new BoardSpace[size][size];
        WorldCoords topRight = WorldCoords.getTopRight();

        for (int y = 0; y < boardSpaces.length; y++) {
            for (int x = 0; x < boardSpaces[y].length; x++) {
                WorldCoords topLeft = new WorldCoords(
                        topRight.x / size * x + PADDING,
                        topRight.y / size * (y + 1) - PADDING
                );

                boardSpaces[y][x] = new BoardSpace(topLeft, topRight.x / size - PADDING * 2);
            }
        }

        return boardSpaces;
    }
}
