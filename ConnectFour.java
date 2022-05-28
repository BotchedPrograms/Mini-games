import java.util.Scanner;

// Connect Four
  // Explaining is overrated; i'ma nap
public class ConnectFour {
  public static int[][] grid;
  public static int redStart;

  // Plays match
  public static String playGame(String move, int turns) {
    String column = move.substring(0, 1);
    putPiece(grid, column, turns, true);
    boolean win = win(grid);
    printBoard(turns, win);
    if (win) {
      return (turns + redStart) % 2 == 0 ? "Red" : "Yellow";
    }
    if (turns == 41) {
      return "Draw";
    }
    return "none";
  }

  // Puts piece down
  public static boolean putPiece(int[][] grid, String column, int turns, boolean place) {
    String columns = "abc1234567";
    int col = columns.indexOf(column);
    int i;
    for (i = 8; i > 2; i--) {
      if (grid[i][col] == 0) {
        break;
      }
    }
    if (place) {
      grid[i][col] = (turns + redStart) % 2 == 0 ? 1 : 2;
    }
    if (grid[i][col] != 0) {
      return true;
    }
    return false;
  }

  // Checks if anyone has won the game yet
  public static boolean win(int[][] grid) {
    // Classic quintuple loop
    for (int i = 8; i > 2; i--) {
      for (int j = 3; j < 10; j++) {
        if (grid[i][j] != 0) {
          for (int k = -1; k < 2; k++) {
            l4: for (int l = -1; l < 2; l++) {
              if (!(k == 0 && l == 0)) {
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
    return false;
  }

  // Prints board
  public static void printBoard(int turns, boolean win) {
    StringBuilder board = new StringBuilder();
    if (!win) {
      board.append((turns + redStart) % 2 == 1 ? "Red" : "Yellow");
      board.append("\'s Turn:\n");
    }
    for (int i = 3; i < 9; i++) {
      for (int j = 3; j < 10; j++) {
        switch (grid[i][j]) {
          case 0:
            board.append(". ");
            break;
          case 1:
            board.append("R ");
            break;
          case 2:
            board.append("Y ");
            break;
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
      int turns = 0;
      while (turns < 42) {
        move = "";
        while (!move.matches("-?\\d+") || Integer.parseInt(move) < 1 || Integer.parseInt(move) > 7) {
          move = scan.nextLine().toLowerCase();
        }
        result = playGame(move, turns);
        if (!result.equals("none")) {
          break;
        }
        if (!putPiece(grid, move.substring(0, 1), turns, false)) {
          turns++;
        }
      }

      // Prints win or draw
      switch (result) {
        case "Red":
          System.out.println("Red wins!");
          break;
        case "Yellow":
          System.out.println("Yellow wins!");
          break;
        case "Draw":
          System.out.println("Draw");
          break;
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
