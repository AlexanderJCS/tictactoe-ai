package tictactoe;

import tictactoe.ai.Computer;
import tictactoe.board.Board;
import jangl.Jangl;
import jangl.io.Window;
import jangl.io.mouse.Mouse;
import jangl.io.mouse.MouseEvent;
import org.lwjgl.glfw.GLFW;
import tictactoe.board.BoardState;

public class TicTacToe {
    private static final int BOARD_SIZE = 3;

    private final Board board;
    private final Computer xPlayer;
    private final Computer oPlayer;
    private Turn turn;

    /**
     * @param xPlayer The Computer object if the X player is a computer, null if it is a human
     * @param oPlayer The Computer object if the O player is a computer, null if it is a human
     */
    public TicTacToe(Computer xPlayer, Computer oPlayer) {
        this.board = new Board(BOARD_SIZE);
        this.turn = Turn.X;

        this.xPlayer = xPlayer;
        this.oPlayer = oPlayer;
    }

    public void draw() {
        this.board.draw();
    }

    private void playerMove() {
        for (MouseEvent event : Mouse.getEvents()) {
            // The event was not a left click
            if (event.button != GLFW.GLFW_MOUSE_BUTTON_1 || event.action != GLFW.GLFW_PRESS) {
                continue;
            }

            if (this.board.selectSpace(this.turn.toBoardState(), Mouse.getMousePos())) {
                this.turn = this.turn.switchTurn();
            }
        }
    }

    /**
     * Makes the move with the AI if it is not null, otherwise prompts human for input
     * @param ai The AI to make the move with, or null
     */
    private void makeMove(Computer ai) {
        if (ai == null) {
            this.playerMove();
        } else {
            ai.makeMove(this.board, this.turn);
            this.turn = this.turn.switchTurn();
        }
    }

    public void update() {
        if (this.turn == Turn.X) {
            this.makeMove(this.xPlayer);
            return;
        }

        this.makeMove(this.oPlayer);
    }

    public BoardState run() {
        while (Window.shouldRun() && this.board.winner() == BoardState.EMPTY) {
            Window.clear();

            this.update();
            this.draw();

            Jangl.update();
        }

        return this.board.winner();
    }
}
