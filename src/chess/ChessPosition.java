package chess;

import boardgame.Position;

public class ChessPosition {
    private char column;
    private int row;

    public ChessPosition (char column, int row){
        if (column < 'a' || column > 'h' || row < 1 || row > 8)
            throw new ChessException ("Error instantiating ChessPosition. Valid values are from a1 to h8.");

        this.row = row;
        this.column = column;
    }


    public int getRow() {
        return this.row;
    }

    public char getColumn() {
        return this.column;
    }


    protected Position toPosition (){
        String str = "abcdefgh";
        return new Position(8 - row, str.indexOf(column));
    }
    
    protected static ChessPosition fromPosition (Position position){
        String str = "abcdefgh";
        return new ChessPosition (str.charAt(position.getColumn()), 8 - position.getRow());
        // return new ChessPosition ((char)('a' + position.getColumn()), 8 - position.getRow());
    }

    
    @Override
    public String toString(){
        return column + "" + row;
    }
}

