package boardgame;

public abstract class Piece {
    protected Position position;
    private Board board;

    public Piece (Board board){
        this.board = board;
        this.position = null;
    }

    protected Board getBoard (){
        return board;
    }

    public abstract boolean[][] possibleMoves();

    public boolean possibleMove(Position position){
        return possibleMoves()[position.getRow()][position.getColumn()];
    }

    public boolean isThereAnyPossibleMove (){
        boolean aid [][] = possibleMoves();

        for (boolean i[] : aid)
            for (boolean j : i)
                if (j)
                    return true;
        return false;
    }

}