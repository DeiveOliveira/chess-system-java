/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessPiece;
import chess.Color;

/**
 *
 * @author win
 */
public class King extends ChessPiece{

    public King(Color color, Board board) {
        super(color, board);
    }
    
    @Override
    public String toString(){
        return "K";
    }
    
    private boolean canMove(Position position){
        ChessPiece p = (ChessPiece)getBoard().piece(position);
        return p == null || p.getColor() != getColor();
    }
    
    @Override
    public boolean[][] possibleMoves() {
        boolean [][] mat = new boolean [getBoard().getRows()][getBoard().getColumns()];
        
        Position p = new Position(0,0);
        
        //Acima
        p.setValues(position.getRow()-1, position.getColumn());
        if(getBoard().positionExists(p) && canMove(p)){
            mat[p.getRow()][p.getColumn()] = true;
        }
        
         //Abaixo
        p.setValues(position.getRow()+1, position.getColumn());
        if(getBoard().positionExists(p) && canMove(p)){
            mat[p.getRow()][p.getColumn()] = true;
        }
        
         //Esquerda
        p.setValues(position.getRow(), position.getColumn()-1);
        if(getBoard().positionExists(p) && canMove(p)){
            mat[p.getRow()][p.getColumn()] = true;
        }
        
         //Direita
        p.setValues(position.getRow(), position.getColumn()+1);
        if(getBoard().positionExists(p) && canMove(p)){
            mat[p.getRow()][p.getColumn()] = true;
        }
          
         //NW
        p.setValues(position.getRow()-1, position.getColumn()-1);
        if(getBoard().positionExists(p) && canMove(p)){
            mat[p.getRow()][p.getColumn()] = true;
        }
        
         //NE
        p.setValues(position.getRow()-1, position.getColumn()+1);
        if(getBoard().positionExists(p) && canMove(p)){
            mat[p.getRow()][p.getColumn()] = true;
        }
        
         //SW
        p.setValues(position.getRow()+1, position.getColumn()-1);
        if(getBoard().positionExists(p) && canMove(p)){
            mat[p.getRow()][p.getColumn()] = true;
        }
        
         //SE
        p.setValues(position.getRow()+1, position.getColumn()+1);
        if(getBoard().positionExists(p) && canMove(p)){
            mat[p.getRow()][p.getColumn()] = true;
        }
        return mat;
    }
}
