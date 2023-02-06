/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package boardgame;

public class Board {
    private int rows;
    private int columns;
    private Piece [][] pieces;

    public Board(int rows, int columns) {
        if(rows < 1 || columns < 1 ){
            throw new BoardException("Erro criando tabuleiro:É necessário que o número de linhas e"
                    + "seja maior ou igua a 1 ");
        }
        this.rows = rows;
        this.columns = columns;
        pieces = new Piece[rows][columns];
    }

    public int getColumns() {
        return columns;
    }

    public int getRows() {
        return rows;
    }
    
    public Piece pieces(int row, int column){
        if(!positionExists(row,column)){
            throw new BoardException("Posição não existe no tabuleiro");
        }
        return pieces[row][column];
    }
    public Piece piece(Position position){
        if(!positionExists(position)){
            throw new BoardException("Posição não existe no tabuleiro");
        }
        return pieces [position.getRow()][position.getColumn()];
    }
    
    public void placePice(Piece piece, Position position){
        if(thereIsAPiece(position)){
            throw new BoardException("Já existe uma peça nessa posição " + position);
        }
        pieces[position.getRow()][position.getColumn()] = piece;
        piece.position = position;
    }
    private boolean positionExists(int row, int column){
        return row >= 0 && row < rows && column >= 0 && column < columns;
    }
    
    public boolean positionExists(Position position){
        return positionExists(position.getRow(), position.getColumn());
    }
    
    public boolean thereIsAPiece(Position position){
         if(!positionExists(position)){
            throw new BoardException("Posição não existe no tabuleiro");
        }
        return piece(position) != null;
    }
    
    public Piece removePiece(Position position){
        if(!positionExists(position)){
            throw new BoardException("Posição não existe no tabuleiro");
        }
        if(piece(position) == null){
            return null;
        }
        Piece aux = piece(position);
        aux.position = null;
        pieces[position.getRow()][position.getColumn()] = null;
        return aux;
    }
    
}
