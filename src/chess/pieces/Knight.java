package chess.pieces;

import chess.ChessPiece;
import chess.Color;

import boardgame.Board;
import boardgame.Position;

public class Knight extends ChessPiece {
    public Knight (Board board, Color color){
        super(board, color);
    }
    
    @Override
    public boolean[][] possibleMoves(){
        boolean[][] aid = new boolean[getBoard().getRows()][getBoard().getColumns()];
        Position p = new Position(0,0);

        p.setValues(position.getRow() -2, position.getColumn() -1);
        if (getBoard().positionExists(p) && canMove(p))
            aid[p.getRow()][p.getColumn()] = true;

        p.setValues(position.getRow() -2, position.getColumn() +1);
        if (getBoard().positionExists(p) && canMove(p))
            aid[p.getRow()][p.getColumn()] = true;

        p.setValues(position.getRow() +2, position.getColumn() -1);
        if (getBoard().positionExists(p) && canMove(p))
            aid[p.getRow()][p.getColumn()] = true;

        p.setValues(position.getRow() +2, position.getColumn() +1);
        if (getBoard().positionExists(p) && canMove(p))
            aid[p.getRow()][p.getColumn()] = true;


        p.setValues(position.getRow() -1, position.getColumn() -2);
        if (getBoard().positionExists(p) && canMove(p))
            aid[p.getRow()][p.getColumn()] = true;

        p.setValues(position.getRow() -1, position.getColumn() +2);
        if (getBoard().positionExists(p) && canMove(p))
            aid[p.getRow()][p.getColumn()] = true;

        p.setValues(position.getRow() +1, position.getColumn() -2);
        if (getBoard().positionExists(p) && canMove(p))
            aid[p.getRow()][p.getColumn()] = true;

        p.setValues(position.getRow() +1, position.getColumn() +2);
        if (getBoard().positionExists(p) && canMove(p))
            aid[p.getRow()][p.getColumn()] = true;

            
        return aid;
    }

    private boolean canMove (Position position){
        return isThereOpponentPiece(position) || !getBoard().thereIsAPiece(position);
    }

    @Override
    public String toString(){
        return "N";
    }

}