/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */
package aplication;

import boardgame.Board;
import boardgame.Position;
import chess.ChessMatch;

/**
 *
 * @author win
 */
public class Programe {

    public static void main(String[] args) {
        Board board = new Board(8, 8);
        ChessMatch chessMatch = new ChessMatch();
        UI.printBoard(chessMatch.getPieces());

    }
}
