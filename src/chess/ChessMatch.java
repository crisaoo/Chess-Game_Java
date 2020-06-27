package chess;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;

import chess.pieces.King;
import chess.pieces.Queen;
import chess.pieces.Rook;
import chess.pieces.Knight;
import chess.pieces.Bishop;
import chess.pieces.Pawn;

public class ChessMatch {
    private Board board;
    private int turn;
    private Color currentPlayer;
    private boolean check;
    private boolean checkmate;
    private List<ChessPiece> captureds;
    private List<ChessPiece> piecesOnTheBoard;
    private ChessPiece enPassantVulnerable;
    private ChessPiece promotedPiece;

    
    public ChessMatch (){
        board = new Board (8,8); 
        turn = 1;
        currentPlayer = Color.WHITE;
        captureds = new ArrayList<>();
        piecesOnTheBoard = new ArrayList<>();
        initialSetup();
    }

    
    public int getTurn() {
        return turn;
    }
    
    public Color getCurrentPlayer() {
        return currentPlayer;
    }
    
    public boolean getCheck(){
        return check;
    }
    
    public boolean getCheckmate(){
        return checkmate;
    }
    
    public List<ChessPiece> getCaptureds(){
        return captureds;
    }

    public ChessPiece getEnPassantVulnerable(){
        return enPassantVulnerable;
    }

    public ChessPiece getPromotedPiece(){
        return promotedPiece;
    }


    public ChessPiece [][] getPieces(){
        ChessPiece pieces [][] = new ChessPiece [board.getRows()][board.getColumns()];
        
        for (int i =0; i < board.getRows(); i++)
            for (int j =0; j < board.getColumns(); j++)
                pieces[i][j] = (ChessPiece) board.piece(i,j);

         return pieces;
    }

    public boolean [][] possibleMoves(ChessPosition sourcePosition){    
        Position source = sourcePosition.toPosition();
        validateSourcePosition(source);
        return board.piece(source).possibleMoves();
    }
    
    
    // -------------------------------------------------- MOVIMENTOS
    public void performChessMove (ChessPosition sourcePosition, ChessPosition targetPosition){
        Position source = sourcePosition.toPosition();
        Position target = targetPosition.toPosition();
        validateSourcePosition(source);
        validateTargetPosition(source, target);
        Piece capturedPiece = makeMove(source, target);

        if (testCheck(currentPlayer)){
            undoMove(source, target, capturedPiece);
            throw new ChessException("You cannot put yourself in check.");
        }

        // Promotion
        promotedPiece = null;
        ChessPiece movedPiece = (ChessPiece)board.piece(target);
        if (movedPiece instanceof Pawn)
            if ((movedPiece.getColor().equals(Color.WHITE) && target.getRow() ==0) || (movedPiece.getColor().equals(Color.BLACK) && target.getRow() ==7) ) 
                promotedPiece = movedPiece;
        
            
        check = testCheck(opponent(currentPlayer));
        
        
        if (testCheckmate(opponent(currentPlayer)))
            checkmate = true;
        else
            nextTurn();
        
        // Special Move: En Passant
        if (board.piece(target) instanceof Pawn && (target.getRow() == source.getRow() + 2  || target.getRow() == source.getRow() - 2) )
            enPassantVulnerable = (ChessPiece)board.piece(target);
        else
            enPassantVulnerable = null;
    }
    
    private Piece makeMove (Position source, Position target){
        Piece piece = board.removePiece(source);
        Piece capturedPiece = board.removePiece(target);
        ((ChessPiece)piece).increaseMoveCount();
        
        if (capturedPiece != null){
            captureds.add((ChessPiece)capturedPiece);
            piecesOnTheBoard.remove(capturedPiece);
        }

        board.placePiece(piece, target);

        // --------------------------- Special Move: Castling
        if (piece instanceof King && target.getColumn() == source.getColumn() +2)
            return makeMove (new Position(source.getRow(), 7), new Position(target.getRow(), 5) );
            
        if (piece instanceof King && target.getColumn() == source.getColumn() -2)
            return makeMove (new Position(source.getRow(), 0), new Position(target.getRow(), 3) );
        // ---------------------------
        

        // --------------------------- Special Move: En Passant
        if (piece instanceof Pawn && target.getColumn() != source.getColumn() && capturedPiece == null )
            capturedPiece = board.removePiece(enPassantVulnerable.getChessPosition().toPosition());    
        // ---------------------------

        return capturedPiece;
    }
    
    public void undoMove (Position source, Position target, Piece captured){
        Piece piece = board.removePiece(target);
        board.placePiece(piece,source);
        ((ChessPiece)piece).decreaseMoveCount();

        // --------------------------- Special Move: Castling
        if (piece instanceof King && target.getColumn() == source.getColumn() +2)
            undoMove (new Position(source.getRow(), 7), new Position(target.getRow(), 5), null );
            
        if (piece instanceof King && target.getColumn() == source.getColumn() -2)
            undoMove (new Position(source.getRow(), 0), new Position(target.getRow(), 3), null );
        // ---------------------------


        // --------------------------- Special Move: En Passant
        if (piece instanceof Pawn && target.getColumn() != source.getColumn() && captured.equals(enPassantVulnerable) ){
            if (enPassantVulnerable.getColor().equals(Color.WHITE))
                board.placePiece(captured, new Position(4, target.getColumn()));
            else
                board.placePiece(captured, new Position(3, target.getColumn()));

            captureds.remove(captured);
            piecesOnTheBoard.add((ChessPiece)captured);
        }
        // ---------------------------

        else if (captured != null){
            board.placePiece(captured, target);
            captureds.remove(captured);
            piecesOnTheBoard.add((ChessPiece)captured);
        }
    }
    

    public void replacePromotedPiece (String type){
        if (promotedPiece == null)
            throw new IllegalStateException("There is no piece to be promoted.");
        ChessPiece newPiece = newPiece(type);
        Position p = promotedPiece.getChessPosition().toPosition();

        board.removePiece(p);
        piecesOnTheBoard.remove(promotedPiece);
        placeNewPiece(newPiece, ChessPosition.fromPosition(p));
        piecesOnTheBoard.add(newPiece);

        check = testCheck(opponent(promotedPiece.getColor()));
    }

    private ChessPiece newPiece (String type){        
        if (type.equals("Q"))
            return new Queen(board, promotedPiece.getColor());
        if (type.equals("R"))
            return new Rook(board, promotedPiece.getColor());
        if (type.equals("B"))
            return new Bishop(board, promotedPiece.getColor());
        if (type.equals("N"))
            return new Knight(board, promotedPiece.getColor());

        throw new InvalidParameterException("Invalid type for promotion, default promotion piece placed.");
    }


    private void validateSourcePosition (Position source){
        if (!board.thereIsAPiece(source))
            throw new ChessException("There is no piece on source position.");
        if (!( (ChessPiece)board.piece(source) ).getColor().equals(currentPlayer) )
            throw new ChessException("The chosen piece is not yours.");
        if (!board.piece(source).isThereAnyPossibleMove())
            throw new ChessException("There is no possible move for the chosen piece.");
        }
        
        private void validateTargetPosition (Position source, Position target){
        if (!board.piece(source).possibleMove(target))
            throw new ChessException("The chosen piece cannot move to target position.");
    }
    
    
    private void nextTurn (){
        currentPlayer = (currentPlayer.equals(Color.WHITE))? Color.BLACK : Color.WHITE;
        turn++;
    }
    // -------------------------------------------------------------
    
    
    // -------------------------------------------------- TESTE DE XEQUE E XEQUE-MATE
    public boolean testCheck (Color color){
        List <Piece> opponentPieces = piecesOnTheBoard.stream().filter(x -> x.getColor().equals(opponent(color) ) ).collect(Collectors.toList());
        
        for (Piece x : opponentPieces)
            if (x.possibleMove(kingPosition(color)) )
                return true;
        return false;
    }
 
    public boolean testCheckmate (Color color){
        if (!testCheck(color))
            return false;
        
        List <ChessPiece> pieces = piecesOnTheBoard.stream().filter(x -> x.getColor().equals(color)).collect(Collectors.toList());
        for (ChessPiece piece : pieces)
            for (int i=0; i < board.getRows(); i++)
                for (int j=0; j < board.getColumns(); j++)
                    if (piece.possibleMove(new Position (i,j) ) ){
                        Position source = piece.getChessPosition().toPosition(); 
                        Position target = new Position(i,  j);
                        Piece captured = makeMove(source, target);
                        boolean testCheck = testCheck(color);

                        undoMove(source, target, captured);

                        if (!testCheck)
                            return false;
                    }
        return true;
    }

    private Position kingPosition (Color color){
        List <ChessPiece> king = piecesOnTheBoard.stream().filter(x -> x instanceof King).collect(Collectors.toList());
        
        for (ChessPiece x : king)
            if (x.getColor().equals(color))
                return x.getChessPosition().toPosition();

        throw new IllegalStateException("The King " + color  + " was not found.");
    } 

    private Color opponent (Color color){
        return (color.equals(Color.WHITE)) ? Color.BLACK : Color.WHITE;
    }
    // ------------------------------------------------------------------------------

    
    // -------------------------------------------------- COLOCANDO PECAS NO TABULEIRO
    protected void placeNewPiece (ChessPiece chessPiece, ChessPosition chessPosition){
        board.placePiece(chessPiece, chessPosition.toPosition());
        piecesOnTheBoard.add(chessPiece);
    }
    
    private void initialSetup (){  
        
        placeNewPiece(new Rook(board, Color.WHITE), new ChessPosition('a', 1));
        placeNewPiece(new Knight(board, Color.WHITE), new ChessPosition('b', 1));
        placeNewPiece(new Bishop(board, Color.WHITE), new ChessPosition('c', 1));
        placeNewPiece(new Queen(board, Color.WHITE), new ChessPosition('d', 1));
        placeNewPiece(new King(board, Color.WHITE, this), new ChessPosition('e', 1));
        placeNewPiece(new Bishop(board, Color.WHITE), new ChessPosition('f', 1));
        placeNewPiece(new Knight(board, Color.WHITE), new ChessPosition('g', 1));
        placeNewPiece(new Rook(board, Color.WHITE), new ChessPosition('h', 1));
        placeNewPiece(new Pawn (board, Color.WHITE, this), new ChessPosition('a', 2));
        placeNewPiece(new Pawn (board, Color.WHITE, this), new ChessPosition('b', 2));
        placeNewPiece(new Pawn (board, Color.WHITE, this), new ChessPosition('c', 2));
        placeNewPiece(new Pawn (board, Color.WHITE, this), new ChessPosition('d', 2));
        placeNewPiece(new Pawn (board, Color.WHITE, this), new ChessPosition('e', 2));
        placeNewPiece(new Pawn (board, Color.WHITE, this), new ChessPosition('f', 2));
        placeNewPiece(new Pawn (board, Color.WHITE, this), new ChessPosition('g', 2));
        placeNewPiece(new Pawn (board, Color.WHITE, this), new ChessPosition('h', 2));


        placeNewPiece(new Rook(board, Color.BLACK), new ChessPosition('a', 8));
        placeNewPiece(new Knight(board, Color.BLACK), new ChessPosition('b', 8));
        placeNewPiece(new Bishop(board, Color.BLACK), new ChessPosition('c', 8));
        placeNewPiece(new Queen(board, Color.BLACK), new ChessPosition('d', 8));
        placeNewPiece(new King(board, Color.BLACK, this), new ChessPosition('e', 8));
        placeNewPiece(new Bishop(board, Color.BLACK), new ChessPosition('f', 8));
        placeNewPiece(new Knight(board, Color.BLACK), new ChessPosition('g', 8));
        placeNewPiece(new Rook(board, Color.BLACK), new ChessPosition('h', 8));
        placeNewPiece(new Pawn (board, Color.BLACK, this), new ChessPosition('a', 7));
        placeNewPiece(new Pawn (board, Color.BLACK, this), new ChessPosition('b', 7));
        placeNewPiece(new Pawn (board, Color.BLACK, this), new ChessPosition('c', 7));
        placeNewPiece(new Pawn (board, Color.BLACK, this), new ChessPosition('d', 7));
        placeNewPiece(new Pawn (board, Color.BLACK, this), new ChessPosition('e', 7));
        placeNewPiece(new Pawn (board, Color.BLACK, this), new ChessPosition('f', 7));
        placeNewPiece(new Pawn (board, Color.BLACK, this), new ChessPosition('g', 7));
        placeNewPiece(new Pawn (board, Color.BLACK, this), new ChessPosition('h', 7));
    }
    // -------------------------------------------------------------------------------

}