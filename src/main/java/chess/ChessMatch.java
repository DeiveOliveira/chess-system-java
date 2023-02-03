/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chess;

import boardgame.Board;

public class ChessMatch {
    
    private Board board;
    
    public ChessMatch() {
        board = new Board(8,8);
    }
    
    public ChessPiece[][] getPieces(){
        ChessPiece[][] mat = new ChessPiece[board.getRows()][board.getColums()];
        for(int i=0; i<board.getRows(); i++){
            for(int j=0; j<board.getColums(); j++){
                mat[i][j] = (ChessPiece) board.pieces(i,j);
            }
        }
        return mat;
    }
    
}
