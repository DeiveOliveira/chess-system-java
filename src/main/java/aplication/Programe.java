/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */
package aplication;

import boardgame.Board;
import boardgame.Position;
import chess.ChessException;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author win
 */
public class Programe {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ChessMatch chessMatch = new ChessMatch();
       List<ChessPiece> captured = new ArrayList<>();

        while (!chessMatch.getCheckMate()) {
            try{
            UI.clearScreen();
            UI.printMatch(chessMatch, captured);
            System.out.println("");
            System.out.print("Posicao de origem: ");
            ChessPosition source = UI.readChessPosition(sc);
            
            boolean [][] possibleMoves = chessMatch.possibleMove(source);
            UI.clearScreen();
            UI.printBoard(chessMatch.getPieces(), possibleMoves);
            
            System.out.println("");
            System.out.print("Destino: ");
            ChessPosition target = UI.readChessPosition(sc);

            ChessPiece capturedPiece = chessMatch.performChessMove(source, target);
           
            if(capturedPiece != null){
                captured.add(capturedPiece);
            }
            if(chessMatch.getPromoted() != null){
                System.out.println("Digite a peca a ser promovida (B/N/R/Q)");
                String type = sc.nextLine();
                chessMatch.replacePromotedPiece(type);
            }
            }
            catch(ChessException e){
                System.out.println(e.getMessage());
                sc.nextLine();
            }
            catch(InputMismatchException e){
                System.out.println(e.getMessage());
                sc.nextLine();
            }
            }
        UI.clearScreen();
        UI.printMatch(chessMatch, captured);
    }
}
