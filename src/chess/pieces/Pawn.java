package chess.pieces;

import chess.ChessMatch;
import chess.ChessPiece;
import chess.Color;

import boardgame.Board;
import boardgame.Position;

public class Pawn extends ChessPiece {
    private ChessMatch chessMatch;

    public Pawn (Board board, Color color, ChessMatch chessMatch){
        super(board, color);
        this.chessMatch = chessMatch;
    }

    @Override
    public boolean[][] possibleMoves(){
        boolean[][] aid = new boolean[getBoard().getRows()][getBoard().getColumns()];
        Position p = new Position(0,0);
        int x; // Para saber se o peão eh preto ou branco, pois os movimentos são diferentes

        x = (getColor().equals(Color.WHITE)) ? -1 : 1;      

        p.setValues(position.getRow() + x, position.getColumn());
        if (canMove(p)){
            aid[p.getRow()][p.getColumn()] = true;

            p.setValues(position.getRow() + 2*x , position.getColumn());
            if (canMove(p) && getMoveCount()==0)
                aid[p.getRow()][p.getColumn()] = true;
        }
        
        
        p.setValues(position.getRow() + x, position.getColumn() - 1);
        if (canCapture(p))
            aid[p.getRow()][p.getColumn()] = true;
        
        p.setValues(position.getRow() + x, position.getColumn() + 1);
        if (canCapture(p))
            aid[p.getRow()][p.getColumn()] = true;

        
        // SPECIAL MOVE: EN PASSANT
        ChessPiece enPassantPiece = chessMatch.getEnPassantVulnerable();
        if (enPassantPiece != null){
            Position left = new Position(position.getRow(), position.getColumn() - 1);
            Position right = new Position(position.getRow(), position.getColumn() + 1);
            
            // White piece
            if (getColor().equals(Color.WHITE) && position.getRow() == 3){
                p.setValues(position.getRow() -1, position.getColumn() -1);
                if(getBoard().positionExists(left) && isThereOpponentPiece(left) && getBoard().piece(left).equals(enPassantPiece))
                       aid[p.getRow()][p.getColumn()] = true;
                
                p.setValues(position.getRow()-1, position.getColumn() +1);  

                if(getBoard().positionExists(right) && isThereOpponentPiece(right) && getBoard().piece(right).equals(enPassantPiece))
                    aid[p.getRow()][p.getColumn()] = true;
            }

            // Black piece
            else if (getColor().equals(Color.BLACK) && position.getRow() == 4 ){
                p.setValues(position.getRow() +1, position.getColumn() -1);
                if(getBoard().positionExists(left) && isThereOpponentPiece(left) && getBoard().piece(left).equals(enPassantPiece))
                    aid[p.getRow()][p.getColumn()] = true;
                
                p.setValues(position.getRow()+1, position.getColumn() +1);

                if(getBoard().positionExists(right) && isThereOpponentPiece(right) && getBoard().piece(right).equals(enPassantPiece))
                    aid[p.getRow()][p.getColumn()] = true;
            }
        }
        

        return aid;
    }

    private boolean canMove (Position p){
        return getBoard().positionExists(p) && !getBoard().thereIsAPiece(p); 
    }

    private boolean canCapture (Position p){
        return getBoard().positionExists(p) && isThereOpponentPiece(p); 
    }

    @Override
    public String toString(){
        return "P";
    }

}