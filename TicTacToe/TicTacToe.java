import java.io.IOException;
import java.util.Scanner;

// TicTacToe
public class TicTacToe {
  public static int[][] grid;
  public static int xStart;
  public static int turns;
  public static boolean putPiece;

  // Plays turn
  public static char playTurn(int move) {
    putPiece(grid, move);
    boolean win = win(grid);
    printBoard(win);
    if (win) {
      return (turns + xStart) % 2 == 1 ? 'x' : 'o';
    }
    if (turns == 9) {
      return 'd';
    }
    return 'n';
  }

  // Puts piece down
  public static void putPiece(int[][] grid, int num) {
    num--;
    int row = num / 3;
    int column = num % 3;
    putPiece = grid[row][column] == 0;
    if (putPiece) {
      grid[row][column] = (turns + xStart) % 2 == 0 ? 1 : 2;
      turns++;
    }
  }

  // Checks if anyone has won the game yet
  public static boolean win(int[][] grid) {
    // Numbers corresponds with row#, column#, change in row, and change in column respectively
      // Goes through top-left corner counter-clockwise to check for a win
    int[][] arr = new int[][] {
      {0,  0,  1,  0},
      {0,  0,  1,  1},
      {0,  0,  0,  1},
      {1,  0,  0,  1},
      {2,  0,  0,  1},
      {2,  0, -1,  1},
      {2,  0, -1,  0},
      {2,  1, -1,  0},
      {2,  2,  0, -1},
      {2,  2, -1, -1},
      {2,  2, -1,  0},
      {1,  2,  0, -1},
      {0,  2,  0, -1},
      {0,  2,  1, -1},
      {0,  2,  1,  0},
      {0,  1,  1,  0}
    };
    int x;
    int y;
    int xd;
    int yd;
    l1: for (int i = 0; i < arr.length; i++) {
      x = arr[i][0];
      y = arr[i][1];
      if (grid[x][y] == 0) {
        if (x + y % 2 == 0) {
          i += 2;
        }
        continue;
      }
      xd = arr[i][2];
      yd = arr[i][3];
      for (int m = 0; m < 2; m++) {
        if (grid[x + xd * m][y + yd * m] != grid[x + xd * (m + 1)][y + yd * (m + 1)]) {
          continue l1;
        }
      }
      return true;
    }
    // Returns false after checking there's no win
    return false;
  }

  // Prints board
  public static void printBoard(boolean win) {
    StringBuilder board = new StringBuilder();
    if (!(win || turns == 9)) {
      board.append((turns + xStart) % 2 == 0 ? "X" : "O");
      board.append("'s Turn:\n");
    }
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        switch (grid[i][j]) {
          case 0 -> board.append(". ");
          case 1 -> board.append("X ");
          case 2 -> board.append("O ");
        }
      }
      board.append("\n");
    }
    System.out.println(board);
  }

  // Play a game of TicTacToe by yourself :(
  public static void playGame() {
    Scanner scan = new Scanner(System.in);
    char result;
    String input;
    String retry = "y";
    while (retry.equals("y") || retry.equals("yes")) {
      grid = new int[3][3];
      int move;
      System.out.print("X or O first?: ");
      input = "";
      while (!(input.equals("x") || input.equals("o"))) {
        input = scan.nextLine().toLowerCase();
      }
      if (input.equals("x")) {
        xStart = 0;
      } else {
        xStart = 1;
      }

      System.out.println("1 2 3");
      System.out.println("4 5 6");
      System.out.println("7 8 9");
      System.out.print("Enter move (number from 1 to 9 inclusive): ");
      result = 'n';
      turns = 0;
      while (turns < 9) {
        input = "";
        while (!input.matches("-?\\d+(\\d+)?") || !(Integer.parseInt(input) >= 1 && Integer.parseInt(input) <= 9)) {
          input = scan.nextLine().toLowerCase();
        }
        move = Integer.parseInt(input);
        result = playTurn(move);
        if (result != 'n') {
          break;
        }
      }

      // Prints win or draw
      switch (result) {
        case 'x' -> System.out.println("X wins!");
        case 'o' -> System.out.println("O wins!");
        case 'd' -> System.out.println("Draw");
      }

      retry = "";
      System.out.print("Retry? (yes or no): ");
      while (!(retry.equals("y") || retry.equals("yes") || retry.equals("n") || retry.equals("no"))) {
        retry = scan.nextLine().toLowerCase();
      }
    }
    scan.close();
  }

  // Plays out a game given a String of the game
    // 12457 is a win for the first player, which in this case (and every case besides single-player) is default to X
  public static char playGame(String moves) {
    grid = new int[3][3];
    int move;
    xStart = 0;
    char result;
    for (int i = 0; i < moves.length(); i++) {
      move = Integer.parseInt(moves.substring(i, i + 1));
      result = playTurn(move);
      if (result != 'n') {
        return result;
      }
    }
    return 'n';
  }

  // Bots play it out for given number of games
  public static void playGame(int times) throws IOException {
    Scanner scan = new Scanner(System.in);

    Reminisce RemiX = new Reminisce("memoryX.txt", true);
    Reminisce RemiO = new Reminisce("memoryO.txt", false);
    int count = 0;
    char result;
    StringBuilder game;
    while (count < times) {
      grid = new int[3][3];
      int move;
      xStart = 0;
      result = 'n';
      turns = 0;
      game = new StringBuilder();
      while (turns < 9) {
        if (turns % 2 == 0) {
          move = RemiX.decideMove(game.toString());
        } else {
          move = RemiO.decideMove(game.toString());
        }
        result = playTurn(move);
        if (result != 'n') {
          break;
        }
        game.append(move);
      }

      // Prints win or draw
      switch (result) {
        case 'x' -> {
          System.out.println("X wins!");
          RemiX.contemplate(true);
          RemiO.contemplate(false);
        }
        case 'o' -> {
          System.out.println("O wins!");
          RemiX.contemplate(false);
          RemiO.contemplate(true);
        }
        case 'd' -> System.out.println("Draw");
      }
      RemiX.recuperate();
      RemiO.recuperate();
      count++;
    }
    scan.close();
  }

  // Play one game against bot
    // true for bot to start with X, false for O
  public static void playGame(boolean x) throws IOException {
    playGame(x, 1);
  }

  // Play game against bot for given number of games
  public static void playGame(boolean x, int times) throws IOException {
    Scanner scan = new Scanner(System.in);

    Reminisce RemiX = new Reminisce("memoryX.txt", true);
    Reminisce RemiO = new Reminisce("memoryO.txt", false);
    int count = 0;
    char result;
    StringBuilder game;
    String input;
    while (count < times) {
      grid = new int[3][3];
      int move;
      xStart = 0;
      result = 'n';
      turns = 0;
      game = new StringBuilder();
      while (turns < 9) {
        if (turns % 2 == 0 && x) {
          move = RemiX.decideMove(game.toString());
        } else if (turns % 2 == 1 && !x){
          move = RemiO.decideMove(game.toString());
        } else {
          input = "";
          while (!input.matches("-?\\d+(\\d+)?") || !(Integer.parseInt(input) >= 1 && Integer.parseInt(input) <= 9)) {
            input = scan.nextLine().toLowerCase();
          }
          move = Integer.parseInt(input);
        }
        result = playTurn(move);
        if (result != 'n') {
          break;
        }
        if (putPiece) {
          game.append(move);
        }
      }

      // Prints win or draw
      switch (result) {
        case 'x' -> {
          System.out.println("X wins!");
          if (x) {
            RemiX.contemplate("w");
          } else {
            RemiO.contemplate("l");
          }
        }
        case 'o' -> {
          System.out.println("O wins!");
          if (x) {
            RemiX.contemplate("l");
          } else {
            RemiO.contemplate("w");
          }
        }
        case 'd' -> {
          System.out.println("Draw");
          if (x) {
            RemiX.contemplate("d");
          } else {
            RemiO.contemplate("d");
          }
        }
      }
      if (x) {
        RemiX.recuperate();
      } else {
        RemiO.recuperate();
      }
      count++;
      if (count % 10 == 0) {
        if (x) {
          RemiX.printRatio();
        } else {
          RemiO.printRatio();
        }
      }
    }
    scan.close();
  }

  public static void main(String[] args) throws IOException {
    playGame();
  }
}
