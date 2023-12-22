package Assignment3;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class LadderAndSnake {
    private final int[][] board;
    private final int numberOfPlayers;
    private final Map<Integer, Integer> ladderMap;
    private final Map<Integer, Integer> snakeMap;
    private final int[] playerPositions;

    public LadderAndSnake(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
        this.board = new int[10][10];
        this.playerPositions = new int[numberOfPlayers];
        this.ladderMap = new HashMap<>();
        this.snakeMap = new HashMap<>();
        initializeBoard();
    }

    private void initializeBoard() {
        ladderMap.put(3, 21);
        ladderMap.put(8, 30);
        ladderMap.put(21, 42);
        ladderMap.put(28, 84);
        ladderMap.put(58, 77);
        ladderMap.put(75, 86);
        ladderMap.put(80, 100);

        snakeMap.put(98, 79);
        snakeMap.put(95, 75);
        snakeMap.put(92, 88);
        snakeMap.put(83, 19);
        snakeMap.put(72, 49);
        snakeMap.put(69, 33);
        snakeMap.put(64, 60);
        snakeMap.put(48, 30);
    }

    public static int flipDice() {
        Random random = new Random();
        return random.nextInt(6) + 1;
//        return (int)(Math.random() * 6) + 1;

    }

    public void play() {
        List<Integer> playOrder = determinePlayOrder();
        System.out.println("Reached final decision on order of playing: " + playOrder);

        boolean gameFinished = false;
        while (!gameFinished) {
            for (int player : playOrder) {
                int diceValue = flipDice();
                System.out.print("Player " + player  + " got a dice value of " + diceValue + " ; ");
                movePlayer(player, diceValue);
                if (playerPositions[player - 1] == 100) {
                    System.out.println("Player " + player  + " wins!");
                    gameFinished = true;
                    break;
                }
                System.out.println("now at square " + playerPositions[player -1]);
            }
            if (gameFinished){
                System.out.println("Game over; Thank you!");
            }else {
                System.out.println("Game not over; flipping again.");
            }
        }
    }

    private List<Integer> determinePlayOrder() {
        List<PlayerDiceRoll> initialRolls = new ArrayList<>();
        for (int i = 0; i < numberOfPlayers; i++) {
            int diceValue = flipDice();
            initialRolls.add(new PlayerDiceRoll(i, diceValue));
            System.out.println("Player " + (i + 1) + " got a dice value of " + diceValue);
        }

        bubbleSort(initialRolls);

        // ties and order
        List<Integer> playOrder = new ArrayList<>();
        for (int i = 0; i < initialRolls.size(); i++) {
            PlayerDiceRoll current = initialRolls.get(i);
            playOrder.add(current.playerId + 1);

            // tie
            if (i < initialRolls.size() - 1 && current.diceRoll == initialRolls.get(i + 1).diceRoll) {
                // Resolve tie
                List<Integer> tiedPlayers = new ArrayList<>();
                tiedPlayers.add(current.playerId + 1);

                while (i < initialRolls.size() - 1 && current.diceRoll == initialRolls.get(i + 1).diceRoll) {
                    i++;
                    tiedPlayers.add(initialRolls.get(i).playerId + 1);
                }
                // Reroll
                List<Integer> tieResolvedOrder = rerollForTie(tiedPlayers);

                playOrder.removeAll(tiedPlayers);
                playOrder.addAll(tieResolvedOrder);
            }
        }

        return playOrder;
    }

    // reroll
    private List<Integer> rerollForTie(List<Integer> tiedPlayers) {

        List<Integer> resolvedOrder = new ArrayList<>();
        boolean tieExists;

        do {
            tieExists = false;
            List<PlayerDiceRoll> rerolls = new ArrayList<>();
            System.out.println("A tie was achieved between " + tiedPlayers  + ". Attempting to break the tie. ");
            for (int playerId : tiedPlayers) {
                int diceValue = flipDice();
                rerolls.add(new PlayerDiceRoll(playerId, diceValue));
                System.out.println("Player " + playerId + " got a dice value of " + diceValue);
            }

            // Sort
            bubbleSort(rerolls);

            // tie
            for (int i = 0; i < rerolls.size() - 1; i++) {
                if (rerolls.get(i).getDiceRoll() == rerolls.get(i + 1).getDiceRoll()) {
                    tieExists = true;
                    break;
                }
                resolvedOrder.add(rerolls.get(i).getPlayerId());
            }

            // reroll again
            if (tieExists) {
                tiedPlayers.clear();
                int lastDiceValue = rerolls.get(0).getDiceRoll();;
                for (PlayerDiceRoll roll : rerolls) {
                    if (roll.getDiceRoll() == lastDiceValue) {
                        tiedPlayers.add(roll.getPlayerId());
                    } else {
                        break;
                    }
                }
            } else {
                resolvedOrder.add(rerolls.get(rerolls.size() - 1).getPlayerId());
            }
        } while (tieExists);

        return resolvedOrder;
    }

    public void bubbleSort(List<PlayerDiceRoll> initialRolls) {
        for (int i = 0; i < initialRolls.size() - 1; i++) {
            for (int j = 0; j < initialRolls.size() - i - 1; j++) {
                if (initialRolls.get(j).diceRoll < initialRolls.get(j + 1).diceRoll) {
                    PlayerDiceRoll temp = initialRolls.get(j);
                    initialRolls.set(j, initialRolls.get(j + 1));
                    initialRolls.set(j + 1, temp);
                }
            }
        }
    }

    private static class PlayerDiceRoll {
        int playerId;
        int diceRoll;

        PlayerDiceRoll(int playerId, int diceRoll) {
            this.playerId = playerId;
            this.diceRoll = diceRoll;
        }

        public int getDiceRoll() {
            return diceRoll;
        }

        public int getPlayerId() {
            return playerId;
        }
    }

    private void movePlayer(int player, int diceValue) {
        int newPosition = playerPositions[player -1] + diceValue;

        if (newPosition > 100) {
            newPosition = 100 - (newPosition - 100);
        }

        if (ladderMap.containsKey(newPosition) && ladderMap.get(newPosition) != null) {
            newPosition = ladderMap.get(newPosition);
        }

        if (snakeMap.containsKey(newPosition) && snakeMap.get(newPosition) != null) {
            newPosition = snakeMap.get(newPosition);
        }

        playerPositions[player - 1] = newPosition;
    }

}


