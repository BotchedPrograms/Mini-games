import java.util.Scanner;
import java.util.ArrayList;
import java.util.Random;

// Minesweeper
public class Minesweeper {
  public static void placeBombs(ArrayList<Integer> availableSpots, int[] mines, int[][] mineNumbers, int x, int y) {
    int rows = mineNumbers.length;
    int columns = mineNumbers[0].length;
    // Removes unavailable spaces from placing bomb on
    for (int i = 0; i < 5; i++) {
      for (int j = 0; j < 5; j++) {
        // The 5 by 5 square centered at the initial point
        availableSpots.remove((Integer) ((x-2+i)*columns+y-2+j));
      }
    }
    // The edge of the board
    for (int i = 0; i < columns; i++) {
      availableSpots.remove((Integer) i);
      availableSpots.remove((Integer) ((rows-1)*columns+i));
    }
    for (int i = 1; i < rows-1; i++) {
      availableSpots.remove((Integer) (i*columns));
      availableSpots.remove((Integer) ((i+1)*columns-1));
    }
    // Chooses random spaces among the available ones to place bombs
      // Uses Random class b/c it's better than Math.random()
    Random rand = new Random();
    int randNum;
    for (int i = 0; i < mines.length; i++) {
      randNum = rand.nextInt(availableSpots.size());
      mines[i] = availableSpots.get(randNum);
      // Removes the space where new bomb's placed to avoid duplicates
      availableSpots.remove(randNum);
    }

    int mineX;
    int mineY;
    // Goes through every mine and puts numbers around it
      // Instead of using conditionals to avoid indexOutOfBoundsExceptions, the board has an extra outer layer of tiles that's just not displayed
    for (int i = 0; i < mines.length; i++) {
      mineX = mines[i] / columns;
      mineY = mines[i] % columns;
      for (int j = 0; j < 3; j++) {
        for (int k = 0; k < 3; k++) {
          mineNumbers[mineX+j-1][mineY+k-1]++;
        }
      }
    }
  }
  
  public static void spread(boolean[][] revealed, int[] mines, int[][] mineNumbers, boolean[][] flagged, int x, int y) {
    if (!flagged[x][y]) {
      revealed[x][y] = true;
      // Reveals tiles around tile if it doesn't have a number on it
      if (mineNumbers[x][y] == 0) {
        // Reveals if tile's not on border b/c indexOutOfBoundsExceptions
        if ((x >= 1 && x <= mineNumbers.length-2) && (y >= 1 && y <= mineNumbers[0].length-2)) {
          for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
              // Reveals tiles around tile if they're not revealed so it doesn't go back and forth ad infinitum
              if (!revealed[x+i-1][y+j-1]) {
                spread(revealed, mines, mineNumbers, flagged, x+i-1, y+j-1);
              }
            }
          }
        }
      }
    }
  }

  public static void displayScreen(int[] mines, int[][] mineNumbers, boolean[][] revealed, boolean[][] flagged) {
    StringBuilder screen = new StringBuilder();
    int rows = mineNumbers.length;
    int columns = mineNumbers[0].length;
    for (int i = 1; i < columns-1; i++) {
      if (i % 5 == 0) {
        // Dashes and apostrophes to indicate intervals of 5
        screen.append(" \'");
      } else {
        screen.append("  ");
      }
    }
    screen.append("\n");
    for (int i = 1; i < rows-1; i++) {
      if ((rows - i - 1) % 5 == 0) {
        screen.append("-");
      } else {
        screen.append(" ");
      }
      for (int j = 1; j < columns-1; j++) {
        if (revealed[i][j]) {
          if (mineNumbers[i][j] == 0) {
            screen.append(". ");
          } else {
            screen.append(mineNumbers[i][j] + " ");
          }
        } else {
          if (flagged[i][j]) {
            screen.append("* ");
            // System.out.print("\u2691 ");   Flag character
          } else {
            screen.append("\u25a0 ");
          }
        }
      }
      if ((rows - i - 1) % 5 == 0) {
        screen.append("-\n");
      } else {
        screen.append(" \n");
      }
    }
    for (int i = 1; i < columns-1; i++) {
      if (i % 5 == 0) {
        screen.append(" \'");
      } else {
        screen.append("  ");
      }
    }
    System.out.println(screen);
  }
  
  public static int bound(int num, int min, int max) {
    // Bounds num to be between min and max
    if (num < min) {
      return min;
    } else if (num > max) {
      return max;
    } else {
      return num;
    }
  }

  public static boolean contains(int[] arr, int target) {
    // True if array contains target, false otherwise
    for (int i = 0; i < arr.length; i++) {
      if (arr[i] == target) {
        return true;
      }
    }
    return false;
  }

  public static String toNormalTime(long time) {
    // converts from milliseconds to days, hours, minutes, and/or seconds
    time /= 1000;
    int days = 0; // 86400
    int hours = 0; // 3600
    int minutes = 0; // 60
    int seconds = 0; // 1
    
    if (time / 86400 > 0) {
      days = (int) time / 86400;
    }
    time %= 86400;
    if (time / 3600 > 0) {
      hours = (int) time / 3600;
    }
    time %= 3600;
    if (time / 60 > 0) {
      minutes = (int) time / 60;
    }
    time %= 60;
    seconds = (int) time;
    if (days > 0) {
      return days + "d " + hours + "h " + minutes + "m " + seconds + "s";
    } else if (hours > 0) {
      return hours + "h " + minutes + "m " + seconds + "s";
    } else if (minutes > 0) {
      return minutes + "m " + seconds + "s";
    } else {
      return seconds + "s";
    }
  }
  
  public static void main(String[] args) {
    // Initializes everything here instead of in the while loop to save a little bit of time
      // Not even sure if this noticably helps performance, it's practically just a bad habit of mine
    Scanner scan = new Scanner(System.in);
    boolean flag;
    boolean[][] flagged;
    int numFlags = 0;
    String[] inputs;
    int x;
    int y;
    int t;
    String input;
    int[] mines;
    boolean[][] revealed;
    boolean start;
    int numRevealed;
    ArrayList<Integer> availableSpots = new ArrayList<>();
    int[][] board;
    int[][] mineNumbers;
    int rows;
    int columns;
    int numOfMines;
    String retry = "y";
    long time1 = -1;
    long time2 = -1;
    int size;
    long[] bestTime = {Long.MAX_VALUE, Long.MAX_VALUE, Long.MAX_VALUE};
    boolean win = false;
    while (retry.equals("y") || retry.equals("yes")) {
      System.out.print("Enter difficulty (easy, medium, or hard): ");
      input = "";
      while (!(input.equals("e") || input.equals("easy") || input.equals("m") || input.equals("medium") || input.equals("h") || input.equals("hard"))) {
        input = scan.nextLine().toLowerCase();
      }
      // Highly recommend easy mode for this; doing medium or god forbid hard mode here is miserable b/c each input takes so long
      if (input.equals("e") || input.equals("easy")) {
        rows = 8;
        columns = 10;
        numOfMines = 10;
        // size for bestTime array
        size = 0;
      } else if (input.equals("m") || input.equals("medium")) {
        rows = 14;
        columns = 18;
        numOfMines = 40;
        size = 1;
      } else {
        rows = 20;
        columns = 24;
        numOfMines = 99;
        size = 2;
      }
      // Could just change rows and columns values from above but kept b/c to show original dimensions
      rows += 2;
      columns += 2;
      revealed = new boolean[rows][columns];
      board = new int[rows][columns];
      mineNumbers = new int[rows][columns];
      availableSpots.clear();
      for (int i = 0; i < board.length*board[0].length; i++) {
        availableSpots.add(i);
      }
      System.out.print("Enter tile (x-coord y-coord f to flag): ");
      flagged = new boolean[rows][columns];
      mines = new int[numOfMines];
      start = true;
      game: while (true) {
        while (true) {
          input = scan.nextLine().toLowerCase();
          // Entering r restarts the game
            // For if you look at a board and just say screw this
          if (input.equals("r")) {
            win = false;
            break game;
          }
          inputs = input.split(" ");
          // Checks if input array is 2 or 3 long
          // More notably, second half checks if first and second part is an int
            // I don't know how it works
          if ((inputs.length == 2 || inputs.length == 3) && inputs[0].matches("-?\\d+(\\d+)?") && inputs[1].matches("-?\\d+(\\d+)?")) {
            x = Integer.parseInt(inputs[0]);
            y = Integer.parseInt(inputs[1]);
            if (inputs.length == 3 && (inputs[2].equalsIgnoreCase("f") || inputs[2].equalsIgnoreCase("flag"))) {
              flag = true;
            } else {
              flag = false;
            }
            t = x;
            x = y;
            y = t;
            x = bound(x, 1, rows-2);
            y = bound(y, 1, columns-2);
            x = rows - x - 1;
            break;
          }
        }
        
        if (flag) {
          // Unflags
          if (flagged[x][y]) {
            flagged[x][y] = false;
          } else if (!revealed[x][y]) {
            flagged[x][y] = true;
          }
        } else {
          if (start) {
            // Places bombs, removes current flags, and reveals spots when game starts
            placeBombs(availableSpots, mines, mineNumbers, x, y);
            flagged = new boolean[rows][columns];
            start = false;
            time1 = System.currentTimeMillis();
  
  
            // If not flagged, spreads revealing
          } else if (!flagged[x][y] && contains(mines, x*columns + y)) {
            StringBuilder screen = new StringBuilder();
            time2 = System.currentTimeMillis();
            System.out.println(numFlags + " flags" + "     " + toNormalTime(time2 - time1));
            for (int i = 1; i < columns-1; i++) {
              if (i % 5 == 0) {
                screen.append(" \'");
              } else {
                screen.append("  ");
              }
            }
            screen.append("\n");
            for (int i = 1; i < rows-1; i++) {
              if ((rows - i - 1) % 5 == 0) {
                screen.append("-");
              } else {
                screen.append(" ");
              }
              for (int j = 1; j < columns-1; j++) {
                if (contains(mines, i*columns+j)) {
                  screen.append("\u25a1 ");
                } else if (revealed[i][j]) {
                  if (mineNumbers[i][j] == 0) {
                    screen.append(". ");
                  } else {
                    screen.append(mineNumbers[i][j] + " ");
                  }
                } else {
                  if (flagged[i][j]) {
                    screen.append("* ");
                    // screen.append("\u2691 ");   Flag character
                  } else {
                    screen.append("\u25a0 ");
                  }
                }
              }
              if ((rows - i - 1) % 5 == 0) {
                screen.append("-");
              } else {
                screen.append(" ");
              }
              screen.append("\n");
            }
            for (int i = 1; i < columns-1; i++) {
              if (i % 5 == 0) {
                screen.append(" \'");
              } else {
                screen.append("  ");
              }
            }
            System.out.println(screen);
            System.out.println("You lose");
            win = false;
            break;
          }
          spread(revealed, mines, mineNumbers, flagged, x, y);
        }

        
        numFlags = numOfMines;
        numRevealed = 0;
        for (int i = 1; i < rows - 1; i++) {
          for (int j = 1; j < columns - 1; j++) {
            if (flagged[i][j]) {
              numFlags--;
            }
            if (!revealed[i][j]) {
              numRevealed++;
            }
          }
        }
        if (start) {
          time1 = System.currentTimeMillis();
        }
        time2 = System.currentTimeMillis();
        System.out.println(numFlags + " flags" + "     " + toNormalTime(time2 - time1));
        displayScreen(mines, mineNumbers, revealed, flagged);
        if (numRevealed == numOfMines) {
          System.out.println("You win!");
          win = true;
          break;
        }
      }
      if (win) {
        if (time2 - time1 < bestTime[size]) {
          bestTime[size] = time2-time1;
        }
      }
      if (bestTime[size] == Long.MAX_VALUE) {
        System.out.println("Best time: ---");
        System.out.println("Time: ---");
      } else {
        if (win) {
          System.out.println("Best time: " + toNormalTime(bestTime[size]));
          System.out.println("Time: " + toNormalTime(time2-time1));
        } else {
          System.out.println("Best time: " + toNormalTime(bestTime[size]));
          System.out.println("Time: ---");
        }
      }
      System.out.print("Retry?: ");
      retry = scan.nextLine().toLowerCase();
    }
    // Replit wants me to close Scanner, and they're right b/c apparently there's memory leaks and other stuff if you don't
    scan.close();
  }
}
