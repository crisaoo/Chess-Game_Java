package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.Color;

public class King extends ChessPiece {   
    private ChessMatch chessMatch;

    public King (Board board, Color color, ChessMatch chessMatch){
        super(board, color);
        this.chessMatch = chessMatch;
    }
    
    @Override
    public boolean[][] possibleMoves(){
        boolean[][] aid = new boolean[getBoard().getRows()][getBoard().getColumns()];

        Position p = new Position(0,0);

        // Above
        p.setValues(position.getRow() -1, position.getColumn());
        if (getBoard().positionExists(p) && canMove(p))
            aid[p.getRow()][p.getColumn()] = true;

        // Bellow
        p.setValues(position.getRow() +1, position.getColumn());
        if (getBoard().positionExists(p) && canMove(p))
            aid[p.getRow()][p.getColumn()] = true;

        // Left
        p.setValues(position.getRow(), position.getColumn() -1);
        if (getBoard().positionExists(p) && canMove(p))
            aid[p.getRow()][p.getColumn()] = true;

        // Right
        p.setValues(position.getRow(), position.getColumn() +1);
        if (getBoard().positionExists(p) && canMove(p))
            aid[p.getRow()][p.getColumn()] = true;

        // NW
        p.setValues(position.getRow() -1, position.getColumn() -1);
        if (getBoard().positionExists(p) && canMove(p))
            aid[p.getRow()][p.getColumn()] = true;

        // NE
        p.setValues(position.getRow() -1, position.getColumn() +1);
        if (getBoard().positionExists(p) && canMove(p))
            aid[p.getRow()][p.getColumn()] = true;

        // SW
        p.setValues(position.getRow() +1, position.getColumn() -1);
        if (getBoard().positionExists(p) && canMove(p))
            aid[p.getRow()][p.getColumn()] = true;

        // SE
        p.setValues(position.getRow() +1, position.getColumn() +1);
        if (getBoard().positionExists(p) && canMove(p))
            aid[p.getRow()][p.getColumn()] = true;


        // SPECIAL MOVE CASTLING: 
        if (getMoveCount()==0 && !chessMatch.getCheck()){ 
            // Kingside Rook
            p.setValues(position.getRow(), position.getColumn() +1);
            Position p2 = new Position(position.getRow(), position.getColumn() +2);
            
            if (!getBoard().thereIsAPiece(p) && !getBoard().thereIsAPiece(p2) ){
                p.setColumn(p2.getColumn() +1);
                if (testRookCastling(p))
                    aid[p2.getRow()][p2.getColumn()] = true;
            }
            
            // Queenside Rook
            p.setValues(position.getRow(), position.getColumn() -1);
            p2.setValues(position.getRow(), position.getColumn() -2);
            Position p3 = new Position(position.getRow(), position.getColumn() -3);

            if (!getBoard().thereIsAPiece(p) && !getBoard().thereIsAPiece(p2) && !getBoard().thereIsAPiece(p3) ){
                p.setColumn(p3.getColumn() -1);
                if (testRookCastling(p))
                    aid[p2.getRow()][p2.getColumn()] = true;
            }
        }

        return aid;
    }

    public boolean canMove (Position position){
       return !getBoard().thereIsAPiece(position) || isThereOpponentPiece(position) ;  /// diferente
    }

    public boolean testRookCastling (Position position){
        ChessPiece piece = (ChessPiece)getBoard().piece(position);
        return piece != null && piece instanceof Rook && piece.getColor().equals(getColor()) && piece.getMoveCount() == 0;
    }

    @Override
    public String toString(){
        return "K";
    }

}