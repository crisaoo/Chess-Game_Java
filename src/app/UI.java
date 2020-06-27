package app;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import chess.ChessPosition;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.Color;

public class UI {
    // https://stackoverflow.com/questions/5762491/how-to-print-color-in-console-using-system-out-println

	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_BLACK = "\u001B[30m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_YELLOW = "\u001B[33m";
	public static final String ANSI_BLUE = "\u001B[34m";
	public static final String ANSI_PURPLE = "\u001B[35m";
	public static final String ANSI_CYAN = "\u001B[36m";
	public static final String ANSI_WHITE = "\u001B[37m";

	public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
	public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
	public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
	public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
	public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
	public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
	public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
    public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";


    public static ChessPosition readChessPosition (Scanner sc){
        try{
            String str = sc.nextLine().toLowerCase();
            char column = str.charAt(0);
            int row = Integer.parseInt(str.substring(1));
            
            return new ChessPosition(column, row);
        }
        catch (RuntimeException e){
            throw new InputMismatchException("Error reading position. Valid values are from a1 to h8.");
        }
    }
        
    
    //------------------------------------------------ IMPRESSÃO DA PARTIDA
    public static void printMatch (ChessMatch partida){
        printBoard(partida.getPieces());

        System.out.println("Turn " + partida.getTurn());

        if (partida.getCheck())
            System.out.println(ANSI_GREEN + "CHECK!!" + ANSI_RESET);
            
        if(!partida.getCaptureds().isEmpty())
            printCapturedPieces(partida.getCaptureds());

        if (partida.getCurrentPlayer().equals(Color.BLACK))
            System.out.println("\nWaiting Player: " + ANSI_YELLOW + partida.getCurrentPlayer() + ANSI_RESET + "\n");
        else
            System.out.println("\nWaiting Player: " + ANSI_WHITE + partida.getCurrentPlayer() + ANSI_RESET + "\n");        
        }
    
        //------------------------------------------------ IMPRESSÃO DO TABULEIRO
        public static void printBoard(ChessPiece [][] pieces){
            int i = 0;

        System.out.println();
        for (ChessPiece [] aux : pieces){
            System.out.print(ANSI_PURPLE + (pieces.length - i) + " " + ANSI_RESET);
            i++;
            
            for(ChessPiece x : aux )
                printPiece(x, false);
        
            System.out.println();
        }

        System.out.println(ANSI_PURPLE + "  a b c d e f g h \n" + ANSI_RESET);
    }
    
    public static void printBoard(ChessPiece [][] pieces, boolean [][] possibleMoves){
        System.out.println();
        for (int i=0; i < pieces.length; i++){
            System.out.print(ANSI_PURPLE + (pieces.length - i) + " " + ANSI_RESET);
            
            for(int j=0; j < pieces[i].length; j++ )
            printPiece(pieces[i][j], possibleMoves[i][j]);
            
            System.out.println();
        }
        
        System.out.println(ANSI_PURPLE + "  a b c d e f g h \n" + ANSI_RESET);
    }
    
    //------------------------------------------------ IMPRESSÃO DA PEÇA
    public static void printPiece (ChessPiece x, boolean possibleMove){
        if (possibleMove)
            System.out.print(ANSI_BLUE);

            if (x == null)
            System.out.print("-");
        else if (possibleMove)
        System.out.print(ANSI_RED + x);
        else if (x.getColor().equals(Color.WHITE))
            System.out.print(ANSI_WHITE  + x);
        else
        System.out.print(ANSI_YELLOW + x);        

        System.out.print(" " + ANSI_RESET);
    }

    //------------------------------------------------ IMPRESSÃO DAS PEÇAS CAPTURADAS
    public static void printCapturedPieces (List<ChessPiece> capturedPieces){
        List <ChessPiece> white = capturedPieces.stream().filter(x -> x.getColor().equals(Color.WHITE)).collect(Collectors.toList());       
        List <ChessPiece> black = capturedPieces.stream().filter(x -> x.getColor().equals(Color.BLACK)).collect(Collectors.toList());

        System.out.println("\nCaptured pieces:");
        
        System.out.print(ANSI_WHITE + "[]");
        if (!white.isEmpty()){
            System.out.print(" = ");
            for (ChessPiece x : white)
                System.out.print(x + " ");        
        }
        
        System.out.print("\n" + ANSI_YELLOW + "[]");    
        if (!black.isEmpty()){
            System.out.print(" = ");
            for (ChessPiece x : black)
                System.out.print(x + " ");
        }
        System.out.println(ANSI_RESET);
    }

    //------------------------------------------------ IMPRESSÃO DO FIM DE JOGO
    public static void printGameover(ChessMatch partida) {
        clearScreen();
        printBoard(partida.getPieces());
        System.out.println("Turn: " + partida.getTurn());    
        printCapturedPieces(partida.getCaptureds());

        System.out.println();
        System.out.println(ANSI_RED + "CHECKMATE" + ANSI_RESET);

        if (partida.getCurrentPlayer().equals(Color.WHITE))
            System.out.println(ANSI_WHITE + "WHITE" + ANSI_RESET + " PIECES WINNER!!!");
            else
            System.out.println(ANSI_YELLOW + "BLACK" + ANSI_RESET + " PIECES WINNER!!!");
    }


    public static void clearScreen (){
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
    

}