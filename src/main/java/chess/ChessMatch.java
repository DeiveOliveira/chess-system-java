/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chess;

import boardgame.Board;
import boardgame.Position;
import chess.pieces.King;
import chess.pieces.Rook;

public class ChessMatch {
    
    private Board board;
    
    public ChessMatch() {
        board = new Board(8,8);
        initialsetup();
    }
    
    public ChessPiece[][] getPieces(){
        ChessPiece[][] mat = new ChessPiece[board.getRows()][board.getColumns()];
        for(int i=0; i<board.getRows(); i++){
            for(int j=0; j<board.getColumns(); j++){
                mat[i][j] = (ChessPiece) board.pieces(i,j);
            }
        }
        return mat;
    }
    private void initialsetup(){
        board.placePice(new Rook(Color.WHITE, board), new Position(2,1));
        board.placePice(new King(Color.BLACK, board), new Position(0, 4));
        board.placePice(new King(Color.WHITE, board), new Position(7, 4));
    }
}
