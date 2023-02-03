/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package boardgame;

public class Board {
    private int rows;
    private int colums;
    private Piece [][] pieces;

    public Board(int rows, int colums) {
        this.rows = rows;
        this.colums = colums;
        pieces = new Piece[rows][colums];
    }

    public int getColums() {
        return colums;
    }

    public void setColums(int colums) {
        this.colums = colums;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }
    
    public Piece pieces(int row, int column){
        return pieces[row][column];
    }
    public Piece pieces(Position position){
        return pieces [position.getRow()][position.getColumn()];
    }
}
