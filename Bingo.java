import java.util.ArrayList;
import java.util.Random;

// Plays Bingo
  // You yourself can't play b/c there's no real playing to do

public class Bingo {
  // Representation of 5 by 5 numbers
  private final int[][] card;
  // Array of booleans with trues if number has been called
  private final boolean[][] cardBool;
  // Name of Bingo player
  private final String name;

  // Available numbers left to be pulled
  private static ArrayList<Integer> available;
  // ArrayList of players
  private static final ArrayList<Bingo> players = new ArrayList<>();
  // Boolean if game is done
  private static boolean done;

  // Sets non-static variables for new Bingo player
  public Bingo(String nam) {
    card = new int[5][5];
    cardBool = new boolean[5][5];
    // Middle space automatically considered true
    cardBool[2][2] = true;
    for (int i = 0; i < 5; i++) {
      assignNumbers(i);
    }
    name = nam;
    players.add(this);
    printCard();
  }

  // Assigns random numbers to player's card
    // First column (1-15), second (16-30), third (31-45), fourth (46-60), fifth (61-75)
    // That's a real thing, search it up
  public void assignNumbers(int column) {
    ArrayList<Integer> arr = new ArrayList<>();
    for (int i = 15 * column + 1; i < 15 * column + 16; i++) {
      arr.add(i);
    }
    Random rand = new Random();
    int num;
    for (int i = 0; i < 5; i++) {
      num = arr.get(rand.nextInt(arr.size()));
      card[i][column] = num;
      arr.remove((Integer) num);
    }
  }

  // Prints player's card
  public void printCard() {
    StringBuilder sb = new StringBuilder();
    sb.append(name);
    sb.append(":\n");
    for (int i = 0; i < 5; i++) {
      for (int j = 0; j < 5; j++) {
        sb.append((i == 2 && j == 2) ? " *" : String.format("%2d", card[i][j]));
        sb.append(" ");
      }
      sb.append("\n");
    }
    System.out.println(sb);
  }

  // Print winner(s)'s card, but with x's in place of pulled numbers
  public void printWinCard() {
    StringBuilder sb = new StringBuilder("\n");
    sb.append(name);
    sb.append(":\n");
    for (int i = 0; i < 5; i++) {
      for (int j = 0; j < 5; j++) {
        sb.append(cardBool[i][j] ? " X" : String.format("%2d", card[i][j]));
        sb.append(" ");
      }
      sb.append("\n");
    }
    System.out.print(sb);
  }

  // Updates cardBool when a new number is pulled
  public void updateBool(int pull) {
    int column = (pull-1)/15;
    for (int i = 0; i < 5; i++) {
      if (card[i][column] == pull) {
        cardBool[i][column] = true;
        break;
      }
    }
  }

  // Checks if player's card is a win
  public boolean checkForWin() {
    // Checks rows, columns, and diagonals starting at upper-right corner and going counter-clockwise
    int[][] check = new int[][] {
      // Row #, column #, change in row, and change in column respectively
      {0, 4, 1, 0},
      {0, 4, 1, -1},
      {0, 3, 1, 0},
      {0, 2, 1, 0},
      {0, 1, 1, 0},
      {0, 0, 1, 0},
      {0, 0, 1, 1},
      {0, 0, 0, 1},
      {1, 0, 0, 1},
      {2, 0, 0, 1},
      {3, 0, 0, 1},
      {4, 0, 0, 1},
    };
    int x;
    int y;
    int xd;
    int yd;
    in: for (int[] ints : check) {
      x = ints[0];
      y = ints[1];
      xd = ints[2];
      yd = ints[3];
      for (int j = 0; j < 5; j++) {
        // If there's a not true, continues checking
        if (!cardBool[x + xd * j][y + yd * j]) {
          continue in;
        }
      }
      // If there's 5 consecutive trues, someone wins
      System.out.println();
      System.out.println(name + " wins!");
      printWinCard();
      return true;
    }
    return false;
  }

  // Resets available numbers and done boolean
  public static void ready() {
    available = new ArrayList<>();
    for (int i = 1; i <= 75; i++) {
      available.add(i);
    }
    done = false;
  }

  // Pulls a number out of the ones available
  public static void pull() {
    Random rand = new Random();
    int pull = available.get(rand.nextInt(available.size()));
    // Removes pulled number from list of available numbers
      // Cast to Integer to prevent misinterpreting number as the index
    available.remove((Integer) pull);
     System.out.print(pull + " ");
    for (Bingo player : players) {
      player.updateBool(pull);
      if (player.checkForWin()) {
        done = true;
      }
    }
  }

  // 1,000,000 games with 1 person averages 41.379585 turns, make of that what you will
  public static void main(String[] args) {
    Bingo adr = new Bingo("Adrianna");
    Bingo ben = new Bingo("Benjamin");
    Bingo cyn = new Bingo("Cynthia");
    Bingo don = new Bingo("Donovan");
    Bingo ely = new Bingo("Elysia");
    ready();
    // Ends if someone wins or it gets past 32 turns... just because
    for (int i = 0; !done && i < 32; i++) {
      pull();
    }
  }
}
