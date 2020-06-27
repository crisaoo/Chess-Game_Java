package app;

import java.security.InvalidParameterException;
import java.util.Scanner;
import java.util.InputMismatchException;

import chess.ChessException;
import chess.ChessMatch;
import chess.ChessPosition;

public class App {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ChessMatch partida = new ChessMatch();

        while(!partida.getCheckmate()){
            try{
                UI.clearScreen();
                UI.printMatch(partida);
                System.out.print("Source Position: ");
                ChessPosition source = UI.readChessPosition(sc);

                boolean [][] possibleMoves = partida.possibleMoves(source);
                UI.clearScreen();
                UI.printBoard(partida.getPieces(), possibleMoves);
                System.out.print("Target Position: ");
                ChessPosition target = UI.readChessPosition(sc);
                
                partida.performChessMove(source, target);

                if (partida.getPromotedPiece() != null){
                    System.out.print("Enter type piece for promotion (Q/R/B/N): ");
                    partida.replacePromotedPiece(sc.nextLine().toUpperCase());
                }
            }
            catch(ChessException e){
                System.out.println(e.getMessage());
                sc.nextLine();
            }
            catch(InputMismatchException e){
                System.out.println(e.getMessage());
                sc.nextLine();
            }
            catch(InvalidParameterException e){
                System.out.println(e.getMessage());
                partida.replacePromotedPiece("Q");
                sc.nextLine();
            }
        }
        UI.printGameover(partida);
        sc.close();
    }
}