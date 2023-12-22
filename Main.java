package Assignment3;

import java.util.InputMismatchException;
import java.util.Scanner;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {
        Scanner kb = new Scanner(System.in);
        int numberOfPlayers = 0;
        int attempts = 0;
        while (attempts < 4) {
            System.out.print("Enter the number of players (2-4): ");
            try {
                numberOfPlayers = kb.nextInt();
                if (numberOfPlayers >= 2 && numberOfPlayers <= 4) {
                    break;
                } else {
                    System.out.println("Invalid input. Please enter a value between 2 and 4.");
                    System.out.println("You have four chances and you have " + (4-attempts-1) + " chance.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a numerical value between 2 and 4.");
                System.out.println("You have four chances and you have " + (4-attempts-1) + " chance.");
                kb.next();
            }
            attempts++;
        }
        if (attempts == 4) {
            System.out.println("Incorrect input entered 4 times. Program will terminate.");
            return;
        }
        LadderAndSnake game = new LadderAndSnake(numberOfPlayers);
        System.out.println("Game is Played by " + numberOfPlayers + " players.");
        System.out.println("Now deciding which player will start playing......");
        game.play();

    }
}