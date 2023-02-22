/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chess;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.Bishop;
import chess.pieces.King;
import chess.pieces.Knight;
import chess.pieces.Pawn;
import chess.pieces.Queen;
import chess.pieces.Rook;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ChessMatch {

    private Board board;
    private int turn;
    private Color currentPlayer;
    private boolean check;
    private boolean checkMate;
    private ChessPiece enPassantVulnerable;
    private ChessPiece promoted;

    private List<Piece> piecesOnTheBoard = new ArrayList<>();
    private List<Piece> capturedPieces = new ArrayList<>();

    public ChessMatch() {
        board = new Board(8, 8);
        turn = 1;
        currentPlayer = Color.WHITE;
        initialsetup();
    }

    public boolean getCheck() {
        return check;
    }

    public boolean getCheckMate() {
        return checkMate;
    }

    public int getTurn() {
        return turn;
    }

    public Color getCurrentPlayer() {
        return currentPlayer;
    }

    public ChessPiece[][] getPieces() {
        ChessPiece[][] mat = new ChessPiece[board.getRows()][board.getColumns()];
        for (int i = 0; i < board.getRows(); i++) {
            for (int j = 0; j < board.getColumns(); j++) {
                mat[i][j] = (ChessPiece) board.pieces(i, j);
            }
        }
        return mat;
    }

    public ChessPiece getEnPassanVulnerable() {
        return enPassantVulnerable;
    }

    public ChessPiece getPromoted() {
        return promoted;
    }

    private void placeNewPiece(char column, int row, ChessPiece piece) {
        board.placePice(piece, new ChessPosition(column, row).toPosition());
        piecesOnTheBoard.add(piece);
    }

    public boolean[][] possibleMove(ChessPosition sourcePosition) {
        Position position = sourcePosition.toPosition();
        validateSourcePosition(position);
        return board.piece(position).possibleMoves();
    }

    public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) {
        Position source = sourcePosition.toPosition();
        Position target = targetPosition.toPosition();
        validateSourcePosition(source);
        validateTargetPosition(source, target);
        Piece capturedPiece = makeMove(source, target);

        if (testCheck(currentPlayer)) {
            undoMove(source, target, capturedPiece);
            throw new ChessException("Voce nao pode se colocar em check");
        }
        ChessPiece movedPiece = (ChessPiece) board.piece(target);

        //Promotion
        promoted = null;
        if (movedPiece instanceof Pawn) {
            if (movedPiece.getColor() == Color.WHITE && target.getRow() == 0 || movedPiece.getColor() == Color.BLACK && target.getRow() == 7) {
                promoted = (ChessPiece) board.piece(target);
                promoted = replacePromotedPiece("Q");
            }
        }

        check = (testCheck(opponent(currentPlayer))) ? true : false;

        if (testCheckMate(opponent(currentPlayer))) {
            checkMate = true;
        } else {
            nextTurn();
        }

        //En passant
        if (movedPiece instanceof Pawn && (target.getRow() == source.getRow() - 2 || target.getRow() == source.getRow() + 2)) {
            enPassantVulnerable = movedPiece;
        } else {
            enPassantVulnerable = null;
        }

        return (ChessPiece) capturedPiece;
    }

    private void nextTurn() {
        turn++;
        currentPlayer = (currentPlayer == Color.WHITE) ? Color.BLACK : Color.WHITE;
    }

    private void validateTargetPosition(Position source, Position target) {
        if (!board.piece(source).possibleMove(target)) {
            throw new ChessException("A peça escolhida não pode se mover para a posição de destino");
        }
    }

    private void validateSourcePosition(Position position) {
        if (!board.thereIsAPiece(position)) {
            throw new ChessException("Não exite peça na posição de origem");
        }
        if (currentPlayer != ((ChessPiece) board.piece(position)).getColor()) {
            throw new ChessException("A peça escolhida nao é sua");

        }
        if (!board.piece(position).isThereAnyPossibleMove()) {
            throw new ChessException("Não existe movimento possiveis para a peça escolhida");
        }
    }

    private Piece makeMove(Position source, Position target) {
        ChessPiece p = (ChessPiece) board.removePiece(source);
        p.increaseMoveCount();
        Piece capturedPiece = board.removePiece(target);
        board.placePice(p, target);

        if (capturedPiece != null) {
            piecesOnTheBoard.remove(capturedPiece);
            this.capturedPieces.add(capturedPiece);
        }
        //Castling lado do rei
        if (p instanceof King && target.getColumn() == source.getColumn() + 2) {
            Position sourceT = new Position(source.getRow(), source.getColumn() + 3);
            Position targetT = new Position(source.getRow(), source.getColumn() + 1);
            ChessPiece rook = (ChessPiece) board.removePiece(sourceT);
            board.placePice(rook, targetT);
            rook.increaseMoveCount();
        }
        //Castling lado do rainha
        if (p instanceof King && target.getColumn() == source.getColumn() - 2) {
            Position sourceT = new Position(source.getRow(), source.getColumn() - 4);
            Position targetT = new Position(source.getRow(), source.getColumn() - 1);
            ChessPiece rook = (ChessPiece) board.removePiece(sourceT);
            board.placePice(rook, targetT);
            rook.increaseMoveCount();
        }

        //En passant
        if (p instanceof Pawn) {
            if (source.getColumn() != target.getColumn() && capturedPiece == null) {
                Position pawnPosition;
                if (p.getColor() == Color.WHITE) {
                    pawnPosition = new Position(target.getRow() + 1, target.getColumn());
                } else {
                    pawnPosition = new Position(target.getRow() - 1, target.getColumn());
                }
                capturedPiece = board.removePiece(pawnPosition);
                capturedPieces.add(capturedPiece);
                piecesOnTheBoard.remove(capturedPiece);
            }
        }

        return capturedPiece;

    }

    private void undoMove(Position source, Position target, Piece capturedPiece) {
        ChessPiece p = (ChessPiece) board.removePiece(target);
        p.decreaseMoveCount();
        board.placePice(p, source);

        if (capturedPiece != null) {
            board.placePice(capturedPiece, target);
            capturedPieces.remove(capturedPiece);
            piecesOnTheBoard.add(capturedPiece);
        }
        //Castling lado do rei
        if (p instanceof King && target.getColumn() == source.getColumn() + 2) {
            Position sourceT = new Position(source.getRow(), source.getColumn() + 3);
            Position targetT = new Position(source.getRow(), source.getColumn() + 1);
            ChessPiece rook = (ChessPiece) board.removePiece(targetT);
            board.placePice(rook, sourceT);
            rook.decreaseMoveCount();
        }
        //Castling lado do rainha
        if (p instanceof King && target.getColumn() == source.getColumn() - 2) {
            Position sourceT = new Position(source.getRow(), source.getColumn() - 4);
            Position targetT = new Position(source.getRow(), source.getColumn() - 1);
            ChessPiece rook = (ChessPiece) board.removePiece(targetT);
            board.placePice(rook, sourceT);
            rook.decreaseMoveCount();
        }
        //En passant
        if (p instanceof Pawn) {
            if (source.getColumn() != target.getColumn() && capturedPiece == enPassantVulnerable) {
                ChessPiece pawn = (ChessPiece) board.removePiece(target);
                Position pawnPosition;
                if (p.getColor() == Color.WHITE) {
                    pawnPosition = new Position(3, target.getColumn());
                } else {
                    pawnPosition = new Position(4, target.getColumn());
                }

                board.placePice(pawn, pawnPosition);
            }
        }
    }

    public ChessPiece replacePromotedPiece(String type) {
        if (promoted == null) {
            throw new IllegalStateException("Nao a peca para ser promovida");
        }
        if (!type.equals("B") && !type.equals("N") && !type.equals("R") && !type.equals("Q")) {
            return promoted;
        }
        Position pos = promoted.getChessPosition().toPosition();
        Piece p = board.removePiece(pos);
        piecesOnTheBoard.remove(p);

        ChessPiece newPiece = newPiece(type, promoted.getColor());
        board.placePice(newPiece, pos);
        piecesOnTheBoard.add(newPiece);

        return newPiece;
    }

    private ChessPiece newPiece(String type, Color color) {
        if (type.equals("B")) {
            return new Bishop(color, board);
        }
        if (type.equals("N")) {
            return new Knight(color, board);
        }
        if (type.equals("Q")) {
            return new Queen(color, board);
        }
        return new Rook(color, board);
    }

    private Color opponent(Color color) {
        return (color == Color.WHITE) ? Color.BLACK : Color.WHITE;
    }

    private Piece king(Color color) {
        List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece) x).getColor() == color).collect(Collectors.toList());
        for (Piece p : list) {
            if (p instanceof King) {
                return (ChessPiece) p;
            }
        }
        throw new IllegalStateException("Não existe rei (K) " + color + " no tabuleiro");
    }

    private boolean testCheck(Color color) {
        Position kingPosition = king(color).getChessPosition().toPosition();
        List<Piece> opponentPieces = piecesOnTheBoard.stream().filter(x -> ((ChessPiece) x).getColor() == opponent(color)).collect(Collectors.toList());
        for (Piece p : opponentPieces) {
            boolean[][] mat = p.possibleMoves();
            if (mat[kingPosition.getRow()][kingPosition.getColumn()]) {
                return true;
            }
        }
        return false;
    }

    private boolean testCheckMate(Color color) {
        if (!testCheck(color)) {
            return false;
        }
        List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece) x).getColor() == color).collect(Collectors.toList());
        for (Piece p : list) {
            boolean[][] mat = p.possibleMoves();
            for (int i = 0; i < board.getRows(); i++) {
                for (int j = 0; j < board.getColumns(); j++) {
                    if (mat[i][j]) {
                        Position source = ((ChessPiece) p).getChessPosition().toPosition();
                        Position target = new Position(i, j);
                        Piece capturedPiece = makeMove(source, target);
                        boolean testCheck = testCheck(color);
                        undoMove(source, target, capturedPiece);
                        if (!testCheck) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    private void initialsetup() {
        placeNewPiece('a', 1, new Rook(Color.WHITE, board));
        placeNewPiece('b', 1, new Knight(Color.WHITE, board));
        placeNewPiece('c', 1, new Bishop(Color.WHITE, board));
        placeNewPiece('d', 1, new Queen(Color.WHITE, board));
        placeNewPiece('e', 1, new King(Color.WHITE, board, this));
        placeNewPiece('f', 1, new Bishop(Color.WHITE, board));
        placeNewPiece('g', 1, new Knight(Color.WHITE, board));
        placeNewPiece('h', 1, new Rook(Color.WHITE, board));
        placeNewPiece('a', 2, new Pawn(Color.WHITE, board, this));
        placeNewPiece('b', 2, new Pawn(Color.WHITE, board, this));
        placeNewPiece('c', 2, new Pawn(Color.WHITE, board, this));
        placeNewPiece('d', 2, new Pawn(Color.WHITE, board, this));
        placeNewPiece('e', 2, new Pawn(Color.WHITE, board, this));
        placeNewPiece('f', 2, new Pawn(Color.WHITE, board, this));
        placeNewPiece('g', 2, new Pawn(Color.WHITE, board, this));
        placeNewPiece('h', 2, new Pawn(Color.WHITE, board, this));

        placeNewPiece('a', 8, new Rook(Color.BLACK, board));
        placeNewPiece('b', 8, new Knight(Color.BLACK, board));
        placeNewPiece('c', 8, new Bishop(Color.BLACK, board));
        placeNewPiece('d', 8, new Queen(Color.BLACK, board));
        placeNewPiece('e', 8, new King(Color.BLACK, board, this));
        placeNewPiece('f', 8, new Bishop(Color.BLACK, board));
        placeNewPiece('g', 8, new Knight(Color.BLACK, board));
        placeNewPiece('h', 8, new Rook(Color.BLACK, board));
        placeNewPiece('a', 7, new Pawn(Color.BLACK, board, this));
        placeNewPiece('b', 7, new Pawn(Color.BLACK, board, this));
        placeNewPiece('c', 7, new Pawn(Color.BLACK, board, this));
        placeNewPiece('d', 7, new Pawn(Color.BLACK, board, this));
        placeNewPiece('e', 7, new Pawn(Color.BLACK, board, this));
        placeNewPiece('f', 7, new Pawn(Color.BLACK, board, this));
        placeNewPiece('g', 7, new Pawn(Color.BLACK, board, this));
        placeNewPiece('h', 7, new Pawn(Color.BLACK, board, this));
    }
}
