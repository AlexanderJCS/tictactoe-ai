package tictactoe;

import jangl.Jangl;
import jangl.color.ColorFactory;
import jangl.io.Window;
import tictactoe.ai.MiniMax;

public class Main {
    public static void main(String[] args) {
        Jangl.init(0.5f, 1);
        Window.setVsync(true);
        Window.setClearColor(ColorFactory.WHITE);
        Window.setTitle("Tic Tac Toe");

        TicTacToe ttt = new TicTacToe(null, new MiniMax());
        System.out.println("Winner: " + ttt.run());

        Window.close();
    }
}