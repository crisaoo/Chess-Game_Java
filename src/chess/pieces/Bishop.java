package chess.pieces;

import chess.ChessPiece;
import chess.Color;

import boardgame.Board;
import boardgame.Position;

public class Bishop extends ChessPiece {
    public Bishop (Board board, Color color){
        super(board, color);
    }

    @Override
    public boolean[][] possibleMoves(){
        boolean[][] aid = new boolean[getBoard().getRows()][getBoard().getColumns()];
        Position p = new Position(0,0);

        // NW
        p.setValues(position.getRow() -1, position.getColumn() -1);
        while(getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)){
            aid [p.getRow()][p.getColumn()] = true;
            p.setValues(p.getRow() -1, p.getColumn() -1);
        }

        if (getBoard().positionExists(p) && isThereOpponentPiece(p))
            aid[p.getRow()][p.getColumn()] = true;


        // NE
        p.setValues(position.getRow() -1, position.getColumn() +1);
        while(getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)){
            aid [p.getRow()][p.getColumn()] = true;
            p.setValues(p.getRow() -1, p.getColumn() +1);
        }

        if (getBoard().positionExists(p) && isThereOpponentPiece(p))
            aid[p.getRow()][p.getColumn()] = true;


        // SW
        p.setValues(position.getRow() +1, position.getColumn() -1);
        while(getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)){
            aid [p.getRow()][p.getColumn()] = true;
            p.setValues(p.getRow() +1, p.getColumn() -1);
        }

        if (getBoard().positionExists(p) && isThereOpponentPiece(p))
            aid[p.getRow()][p.getColumn()] = true;


        // SE
        p.setValues(position.getRow() +1, position.getColumn() +1);
        while(getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)){
            aid [p.getRow()][p.getColumn()] = true;
            p.setValues(p.getRow() +1, p.getColumn() +1);
        }

        if (getBoard().positionExists(p) && isThereOpponentPiece(p))
            aid[p.getRow()][p.getColumn()] = true;


        return aid;
    }


    @Override
    public String toString(){
        return "B";
    }

}