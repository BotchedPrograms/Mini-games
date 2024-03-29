import java.util.Scanner;

// Connect Four
public class ConnectFour {
  public static int[][] grid;
  public static int redStart;
  public static int turns;
  public static boolean putPiece;

  // Plays match
  public static String playTurn(int move) {
    putPiece(grid, move);
    boolean win = win(grid);
    printBoard(win);
    if (win) {
      return (turns + redStart) % 2 == 1 ? "Red" : "Yellow";
    }
    if (turns == 42) {
      return "Draw";
    }
    return "none";
  }

  // Puts piece down
  public static void putPiece(int[][] grid, int column) {
    column += 2;
    putPiece = false;
    int i;
    for (i = 8; i > 2; i--) {
      if (grid[i][column] == 0) {
        break;
      }
    }
    if (grid[3][column] == 0) {
      putPiece = true;
    }
    if (putPiece) {
      grid[i][column] = (turns + redStart) % 2 == 0 ? 1 : 2;
      turns++;
    }
  }

  // Checks if anyone has won the game yet
  public static boolean win(int[][] grid) {
    // Classic quintuple loop
    for (int i = 8; i > 2; i--) {
      for (int j = 3; j < 10; j++) {
        // Board is 12 by 13 aka 6 by 7 main board with 3-tile border
          // Advantage is that one can condense the check to a few for loops
          // Instead of double-digit if statements to avoid out-of-bounds-exceptions
        // Two loops above go through every tile in main board
        // If statement below prevents saying there's a win b/c there's 4 consecutive blank tiles
        if (grid[i][j] != 0) {
          for (int k = -1; k < 2; k++) {
            l4: for (int l = -1; l < 2; l++) {
              // Two loops above both go through each 8 adjacent neighbors
              // If statement below ensures it checks its neighbors and not itself
              if (!(k == 0 && l == 0)) {
                // Stuff below checks if the 4 tiles in certain direction are the same
                  // Restarts previous loop if not, returns true if so
                for (int m = 0; m < 3; m++) {
                  if (grid[i+k*m][j+l*m] != grid[i+k*(m+1)][j+l*(m+1)]) {
                    continue l4;
                  }
                }
                return true;
              }
            }
          }
        }
      }
    }
    // Returns false after checking there's no win
    return false;
  }

  // Prints board
  public static void printBoard(boolean win) {
    StringBuilder board = new StringBuilder();
    if (!(win || turns == 42)) {
      board.append((turns + redStart) % 2 == 0 ? "Red" : "Yellow");
      board.append("'s Turn:\n");
    }
    for (int i = 3; i < 9; i++) {
      for (int j = 3; j < 10; j++) {
        switch (grid[i][j]) {
          case 0 -> board.append(". ");
          case 1 -> board.append("R ");
          case 2 -> board.append("Y ");
        }
      }
      board.append("\n");
    }
    for (int i = 1; i <= 7; i++) {
      board.append(i);
      board.append(" ");
    }
    board.append("\n");
    System.out.println(board);
  }

  // Plays game
  public static void main(String[] args) {
    Scanner scan = new Scanner(System.in);
    String retry = "y";
    while (retry.equals("y") || retry.equals("yes")) {
      grid = new int[12][13];

      // Asks if red or yellow is first
      System.out.print("Red or Yellow first?: ");
      String move = "";
      while (!(move.equals("r") || move.equals("red") || move.equals("y") || move.equals("yellow"))) {
        move = scan.nextLine().toLowerCase();
      }
      if (move.equals("r") || move.equals("red")) {
        redStart = 0;
      } else {
        redStart = 1;
      }

      // Plays move
      System.out.print("Enter move (number from 1 to 7 inclusive): ");
      String result = "";
      turns = 0;
      while (turns < 42) {
        move = "";
        while (!move.matches("-?\\d+") || Integer.parseInt(move) < 1 || Integer.parseInt(move) > 7) {
          move = scan.nextLine().toLowerCase();
        }
        result = playTurn(Integer.parseInt(move));
        if (!result.equals("none")) {
          break;
        }
      }

      // Prints win or draw
      switch (result) {
        case "Red" -> System.out.println("Red wins!");
        case "Yellow" -> System.out.println("Yellow wins!");
        case "Draw" -> System.out.println("Draw");
      }

      // Asks for retry
      retry = "";
      System.out.print("Play again?: ");
      while (!(retry.equals("y") || retry.equals("yes") || retry.equals("n") || retry.equals("no"))) {
        retry = scan.nextLine().toLowerCase();
      }
    }
    scan.close();
  }
}
