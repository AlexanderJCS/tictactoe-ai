package tictactoe;

import tictactoe.ai.Computer;
import tictactoe.ai.MiniMax;
import tictactoe.board.Board;
import jangl.Jangl;
import jangl.io.Window;
import jangl.io.mouse.Mouse;
import jangl.io.mouse.MouseEvent;
import org.lwjgl.glfw.GLFW;
import tictactoe.board.BoardState;

public class TicTacToe {
    private static final int BOARD_SIZE = 7;

    private final Board board;
    private final Computer ai;
    private Turn turn;

    public TicTacToe() {
        this.board = new Board(BOARD_SIZE);
        this.turn = Turn.X;
        this.ai = new MiniMax();
    }

    public void draw() {
        this.board.draw();
    }

    public void update() {
        if (this.turn == Turn.O) {
            this.ai.makeMove(this.board, this.turn);
            this.turn = this.turn.switchTurn();
        }

//        this.ai.makeMove(this.board, this.turn);
//        this.turn = this.turn.switchTurn();
//
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

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
