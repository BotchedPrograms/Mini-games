import java.util.ArrayList;
import java.util.Scanner;

// Run to play watered down-snake
// For the record, I have made more but this is all you get. You're not missing that much though
public class Snake {
  // Places apple randomly where snake isn't in
  public static String apple(boolean[][] passed, int available, ArrayList<Integer> appleX, ArrayList<Integer> appleY) {
    int random = (int) (Math.random()*available);
    int count = -1;
    for (int i = 0; i < passed.length; i++) {
      l1: for (int j = 0; j < passed[0].length; j++) {
        if (!passed[i][j]) {
          for (int k = 0; k < appleX.size(); k++) {
            // Skips iteration if there's already an apple at the spot
            if (i == appleX.get(k) && passed[0].length-j-1 == appleY.get(k)) {
               continue l1;
            }
          }
          count++;
        }
        if (count == random) {
          // couldn't do void method because it doesn't return values of appleX and appleY
          return i + " " + (passed[0].length-j-1);
        }
      }
    }
    // Satisfies String method, shouldn't actually be returned
    return "err";
  }

  public static void main(String[] args) {
    // Saves a few milliseconds, if even, to initialize everything now rather than repeatedly in the while loop
    Scanner scan = new Scanner(System.in);
    int x_length = 9;
    int y_length = 10;
    boolean[][] passed;
    String move;
    String lastMove;
    int x;
    int y;
    boolean nothing;
    boolean first;
    // made in the vain hope that checking if true is faster than if !false
    boolean notFirst2 = false;
    int snake_length;
    ArrayList<Integer> xs = new ArrayList<>();
    ArrayList<Integer> ys = new ArrayList<>();
    int appleBI;
    String appleS;
    String[] appleSs;
    String retry;
    int score;
    int highScore = 0;
    char appleC = '\u00F3';
    char snakeC = '\u25FC';
    char boardC = '\u25FB';
    int lastXLength = 9;
    int lastYLength = 10;
    char lastAppleC = '\u00F3';
    char lastSnakeC = '\u25FC';
    char lastBoardC = '\u25FB';
    String inputS;
    StringBuilder screen;
    String scoreS;
    boolean death;
    int xshort;
    int yshort;
    int appleI = 1;
    int lastAppleI = 1;
    ArrayList<Integer> appleX = new ArrayList<>();
    ArrayList<Integer> appleY = new ArrayList<>();

    // To implement at a later date
    String[] lastMoves;   // for twins mode
      // A way to stop the damn screen from shaking
      // It's much better than it used to be but still bad
      // It might just be b/c Replit's too laggy

    // Flashy title screen
    try {
      System.out.println("\n----------------------------------------------------------------"); Thread.sleep(100);
      System.out.println("     SSSSSS    NN     NN        AA        KK   KK    EEEEEEEE   "); Thread.sleep(100);
      System.out.println("   SSS    SS   NNNN   NN       AAAA       KK  KK     EE         "); Thread.sleep(100);
      System.out.println("    SSSS       NN NN  NN      AA  AA      KKKK       EEEEEEEE   "); Thread.sleep(100);
      System.out.println("        SSS    NN  NN NN     AAAAAAAA     KK  KK     EE         "); Thread.sleep(100);
      System.out.println("   SSS    SS   NN   NNNN    AA      AA    KK   KK    EE         "); Thread.sleep(100);
      System.out.println("     SSSSSS    NN     NN   AA        AA   KK    KK   EEEEEEEE   "); Thread.sleep(100);
      System.out.println("----------------------------------------------------------------"); Thread.sleep(100);
    } catch (InterruptedException ex) {
      Thread.currentThread().interrupt();
    }
    System.out.print("Play, Settings, or Quit: ");
    retry = scan.nextLine();

    while (true) {
      // Customization!
      if (retry.equalsIgnoreCase("set") || retry.equalsIgnoreCase("settings")) {
        // Customizes size
        System.out.print("Board size   (small, medium, large, or custom): ");
        inputS = scan.nextLine();
        switch (inputS.toLowerCase()) {
          // Dimensions of board, placement of snake, & placement of apple are faithful to Google's version
          case "s":
            x_length = 9;
            y_length = 10;
            break;
          case "small":
            x_length = 9;
            y_length = 10;
            break;
          case "m":
            x_length = 15;
            y_length = 17;
            break;
          case "medium":
            x_length = 15;
            y_length = 17;
            break;
          case "l":
            x_length = 21;
            y_length = 24;
            break;
          case "large":
            x_length = 21;
            y_length = 24;
            break;
          case "c":
            // To make sure height's odd
            System.out.print("Enter distance from snake to top (min 1): ");
            // monument to my first practical do-while loop
            do {
              x_length = scan.nextInt()*2+1;
              scan.nextLine();
              if (x_length < 3) {
                System.out.print("Enter valid distance: ");
              }
            } while (x_length < 3);
            System.out.print("Enter length (min 3): ");
            do {
              y_length = scan.nextInt();
              scan.nextLine();
              if (y_length < 3) {
                System.out.print("Enter valid length: ");
              }
            } while (y_length < 3);
            break;
          case "custom":
            System.out.print("Enter distance from snake to top (min 1): ");
            do {
              x_length = scan.nextInt()*2+1;
              scan.nextLine();
              if (x_length < 3) {
                System.out.print("Enter valid distance: ");
              }
            } while (x_length < 3);
            System.out.print("Enter length (min 3): ");
            do {
              y_length = scan.nextInt();
              scan.nextLine();
              if (y_length < 3) {
                System.out.print("Enter valid length: ");
              }
            } while (y_length < 3);
            break;
          default:
            x_length = lastXLength;
            y_length = lastYLength;
        }

        // Customizes number of apples
        System.out.print("Number of apples   (1, 3, or 5): ");
        inputS = scan.nextLine();
        switch (inputS) {
          case "1":
            appleI = 1;
            break;
          case "3":
            if (x_length < 5 || y_length < 5) {
              // Bugs I don't intend to fix for now exist if too many apples are in a small board
              System.out.print("Don't put so many apples in such a tiny basket!\nWould you like to change to a 1, please? (^-^): ");
              do {
                inputS = scan.nextLine();
                if (inputS.equalsIgnoreCase("yes") || inputS.equalsIgnoreCase("y")) {
                  appleI = 1;
                  break;
                } else if (inputS.equalsIgnoreCase("no") || inputS.equalsIgnoreCase("n")) {
                  appleI = 3;
                  break;
                }
              } while (!(inputS.equalsIgnoreCase("yes") || inputS.equalsIgnoreCase("y") || inputS.equalsIgnoreCase("no") || inputS.equalsIgnoreCase("n")));
            } else {
              appleI = 3;
            }
            break;
          case "5":
            if (x_length < 5 || y_length < 5) {
              System.out.print("Don't put so many apples in such a tiny basket!\nWould you like to change to a 1, please? (^-^): ");
              do {
                inputS = scan.nextLine();
                if (inputS.equalsIgnoreCase("yes") || inputS.equalsIgnoreCase("y")) {
                  appleI = 1;
                  break;
                } else if (inputS.equalsIgnoreCase("no") || inputS.equalsIgnoreCase("n")) {
                  appleI = 5;
                  break;
                }
              } while (!(inputS.equalsIgnoreCase("yes") || inputS.equalsIgnoreCase("y") || inputS.equalsIgnoreCase("no") || inputS.equalsIgnoreCase("n")));
            } else {
              appleI = 5;
            }
            break;
          default:
            appleI = lastAppleI;
        }

        // Customizes apples
        System.out.print("Apple   (1 = ó, 2 = J, 3 = Ó, 4 = ◯ , 5 = custom): ");
        inputS = scan.nextLine();
        switch (inputS) {
          case "1":
            appleC = '\u00F3';
            break;
          case "2":
            appleC = 'J';
            break;
          case "3":
            appleC = '\u00D3';
            break;
          case "4":
            appleC = '\u25EF';
            break;
          case "5":
            // Just doesn't work for unicode w/ more than 4 digits
            System.out.print("Enter character or the last 4 digits of your unicode number: ");
            do {
              inputS = scan.nextLine();
              if (inputS.length() == 1) {
                appleC = inputS.charAt(0);
              } else if (inputS.length() == 4) {
                // String to hexadecimal int, to unicode character
                appleC = Character.toChars(Integer.parseInt(inputS, 16))[0];
              }
            } while (inputS.length() != 1 && inputS.length() != 4);
            break;
          default:
            appleC = lastAppleC;
        }

        // Customizes snake
        System.out.print("Snake   (1 = ◼, 2 = ◻, 3 = ◯ , 4 = 1, 5 = custom): ");
        // '\u25A0' is also an option ■
        // So is '*'
        inputS = scan.nextLine();
        switch (inputS) {
          case "1":
            snakeC = '\u25FC';
            break;
          case "2":
            snakeC = '\u25FB';
            break;
          case "3":
            snakeC = '\u25EF';
            break;
          case "4":
            snakeC = '1';
            break;
          case "5":
            System.out.print("Enter character or the last 4 digits of your unicode number: ");
            do {
              inputS = scan.nextLine();
              if (inputS.length() == 1) {
                appleC = inputS.charAt(0);
              } else if (inputS.length() == 4) {
                // String to hexadecimal int, to unicode character
                appleC = Character.toChars(Integer.parseInt(inputS, 16))[0];
              }
            } while (inputS.length() != 1 && inputS.length() != 4);
            break;
          default:
            snakeC = lastSnakeC;
        }

        // Customizes board
        System.out.print("Board   (1 = ◻, 2 = ◼, 3 = ., 4 = 0, 5 = custom): ");
        // '\u25A1' is also an option □
        inputS = scan.nextLine();
        switch (inputS) {
          case "1":
            boardC = '\u25FB';
            break;
          case "2":
            boardC = '\u25FC';
            break;
          case "3":
            boardC = '.';
            break;
          case "4":
            boardC = '0';
            break;
          case "5":
            System.out.print("Enter character or the last 4 digits of your unicode number: ");
            do {
              inputS = scan.nextLine();
              if (inputS.length() == 1) {
                appleC = inputS.charAt(0);
              } else if (inputS.length() == 4) {
                // String to hexadecimal int, to unicode character
                appleC = Character.toChars(Integer.parseInt(inputS, 16))[0];
              }
            } while (inputS.length() != 1 && inputS.length() != 4);
            break;
          default:
            boardC = lastBoardC;
        }

          // Literal filler; doesn't even check if it's yes; just gets player mentally ready to play game
          System.out.print("Done?   (yes or yes): ");
          scan.nextLine();
        } else if (retry.equalsIgnoreCase("q") || retry.equalsIgnoreCase("quit")) {
          break;
        }

        // Declares all variables that need to be reset
        passed = new boolean[x_length][y_length];
        move = "d";                           // Defaults very first move to "d"
        x = (x_length-1)/2;
        y = (6*y_length-4)/7;                 // length of 10 -> 2, 17 -> 3, 24 -> 4      Derived from y - (y-3)/7 - 1
        passed[x][y] = true;
        first = true;
        snake_length = 4;
        xs.clear();
        ys.clear();
        xs.add(x);
        ys.add(y);
        appleX.clear();
        appleY.clear();
        xshort = (x_length-1)/2;
        // length of 10 -> 2, 17 -> 4, 24 -> 5 -> second # = y_length - 1 b/c arrays are weird        Derived from y - ( ( (y-3)/7 + 1 ) + y/14 )       17 makes sense but 14 chosen b/c equation is relatively simpler
        yshort = (11*y_length-8)/14;
        if (appleI == 1) {
          appleX.add(xshort);
          appleY.add(yshort);
        } else if (appleI == 3) {
          appleX.add(xshort);
          appleX.add(xshort-2);
          appleX.add(xshort+2);
          appleY.add(yshort);
          appleY.add(yshort-2);
          appleY.add(yshort-2);
        } else {
          appleX.add(xshort);
          appleX.add(xshort-2);
          appleX.add(xshort+2);
          appleX.add(xshort-2);
          appleX.add(xshort+2);
          if (x_length >= 21 && y >= 24) {
            appleY.add(yshort-3);
            appleY.add(yshort-5);
            appleY.add(yshort-5);
            appleY.add(yshort-1);
            appleY.add(yshort-1);
          } else {
            appleY.add(yshort-1);
            appleY.add(yshort-3);
            appleY.add(yshort-3);
            appleY.add(yshort+1);
            appleY.add(yshort+1);
          }
        }
        appleBI = -1;
        lastMove = "";
        retry = "";
        score = 0;
        death = true;

        // The actual game
        while (true) {
          nothing = false;
          if (move.equalsIgnoreCase("w")) {
            // Does nothing if last move was in the opposite direction
            if (lastMove.equalsIgnoreCase("s")) {
              nothing = true;
            // Checks if out of bounds, then checks if it's been passed
            } else if (x < 1 || passed[x - 1][y]) {
              // Checks if next spot is the very end of snake tail, which should move out of the way by the time snake moves to it
              if (y == ys.get(0) && x - 1 == xs.get(0)) {
                x--;
                xs.add(x);
                ys.add(y);
                xs.remove(0);
                ys.remove(0);
              } else {
                break;
              }
            // Moves in direction
            } else {
              x--;
              passed[x][y] = true;
              xs.add(x);
              ys.add(y);
            }
          // Code above recycled to work for each direction
          } else if (move.equalsIgnoreCase("d")) {
            if (lastMove.equalsIgnoreCase("a")) {
              nothing = true;
            } else if (y < 1 || passed[x][y - 1]) {
              if (x == xs.get(0) && y - 1 == ys.get(0)) {
                  y--;
                  xs.add(x);
                  ys.add(y);
                  xs.remove(0);
                  ys.remove(0);
              } else {
                  break;
              }
            } else {
              y--;
              passed[x][y] = true;
              xs.add(x);
              ys.add(y);
            }
          } else if (move.equalsIgnoreCase("s")) {
            if (lastMove.equalsIgnoreCase("w")) {
              nothing = true;
            } else if (x > x_length - 2 || passed[x + 1][y]) {
              // Reordered from   x + 1 == xs.get(0) && y == ys.get(0)    b/c the +1 might waste time
              if (y == ys.get(0) && x + 1 == xs.get(0)) {
                x++;
                xs.add(x);
                ys.add(y);
                xs.remove(0);
                ys.remove(0);
              } else {
                break;
              }
            } else {
              x++;
              passed[x][y] = true;
              xs.add(x);
              ys.add(y);
            }
          } else if (move.equalsIgnoreCase("a")) {
            if (lastMove.equalsIgnoreCase("d")) {
              nothing = true;
            } else if (y > y_length - 2 || passed[x][y + 1]) {
              if (x == xs.get(0) && y + 1 == ys.get(0)) {
                y++;
                xs.add(x);
                ys.add(y);
                xs.remove(0);
                ys.remove(0);
              } else {
                break;
              }
            } else {
              y++;
              passed[x][y] = true;
              xs.add(x);
              ys.add(y);
            }
          } else {
            nothing = true;
          }
          // Moves forward if input is not "w, a, s, or d" or if snake is moved in opposite direction
          if (nothing) {
            if (lastMove.equalsIgnoreCase("w")) {
              if (x < 1 || passed[x - 1][y]) {
                if (y == ys.get(0) && x - 1 == xs.get(0)) {
                  x--;
                  xs.add(x);
                  ys.add(y);
                  xs.remove(0);
                  ys.remove(0);
                } else {
                  break;
                }
              } else {
                x--;
                passed[x][y] = true;
                xs.add(x);
                ys.add(y);
              }
            } else if (lastMove.equalsIgnoreCase("d")) {
              if (y < 1 || passed[x][y - 1]) {
                if (x == xs.get(0) && y - 1 == ys.get(0)) {
                  y--;
                  xs.add(x);
                  ys.add(y);
                  xs.remove(0);
                  ys.remove(0);
                } else {
                  break;
                }
              } else {
                y--;
                passed[x][y] = true;
                xs.add(x);
                ys.add(y);
              }
            } else if (lastMove.equalsIgnoreCase("s")) {
              if (x > x_length - 2 || passed[x + 1][y]) {
                if (y == ys.get(0) && x + 1 == xs.get(0)) {
                  x++;
                  xs.add(x);
                  ys.add(y);
                  xs.remove(0);
                  ys.remove(0);
                } else {
                  break;
                }
              } else {
                x++;
                passed[x][y] = true;
                xs.add(x);
                ys.add(y);
              }
            } else if (lastMove.equalsIgnoreCase("a")) {
              if (y > y_length - 2 || passed[x][y + 1]) {
                if (x == xs.get(0) && y + 1 == ys.get(0)) {
                  y++;
                  xs.add(x);
                  ys.add(y);
                  xs.remove(0);
                  ys.remove(0);
                } else {
                  break;
                }
              } else {
                y++;
                passed[x][y] = true;
                xs.add(x);
                ys.add(y);
              }
            }
          } else {
            lastMove = move;
          }

          // Maintains snake length
          if (xs.size() > snake_length) {
            passed[xs.get(0)][ys.get(0)] = false;
            xs.remove(0);
            ys.remove(0);
          }
          
          // Tests if snake is over apple
          for (int i = 0; i < appleX.size(); i++) {
            if (x == appleX.get(i) && y == y_length-appleY.get(i)-1) {
              appleBI = i;
              snake_length++;
              // Sets score and high score
              score = snake_length - 4;
              if (score > highScore) {
                highScore = score;
              }
            }
          }


          // Places apple
          if (appleBI >= 0) {
            if (x_length * y_length - snake_length - appleI >= -1) {
              appleS = apple(passed, x_length * y_length - snake_length - appleI, appleX, appleY);
              appleSs = appleS.split(" ");
              appleX.set(appleBI, Integer.parseInt(appleSs[0]));
              appleY.set(appleBI, Integer.parseInt(appleSs[1]));
              appleBI = -1;
            } else if (x_length * y_length - snake_length >= 0) {
              // for last few remaining spaces
              appleX.remove(appleBI);
              appleY.remove(appleBI);
              appleBI = -1;
            } else {
              // Filling up board leads to victory screen
              screen = new StringBuilder("\n");
              screen.append(score);
              if (notFirst2) {
                screen.append(" | ");
                screen.append(highScore); 
              }
              screen.append("\n");
              for (int i = 0; i < x_length; i++) {
                for (int j = 0; j < y_length; j++) {
                  if (passed[i][y_length-j-1]) {
                    screen.append(snakeC);
                    screen.append(" ");
                  } else {
                    screen.append(boardC);
                    screen.append(" ");
                  }
                }
                screen.append("\n");
              }
              System.out.print(screen);
              death = false;
              break;
            }
          }
          
          // Combines whole screen to one String instead of each line individually to avoid flickering
          screen = new StringBuilder("\n");
          screen.append(score);
          if (notFirst2) {
            screen.append(" | ");
            screen.append(highScore);
          }
          screen.append("\n");
          for (int i = 0; i < x_length; i++) {
            l2: for (int j = 0; j < y_length; j++) {
              for (int k = 0; k < appleX.size(); k++) {
                if (i == appleX.get(k) && j == appleY.get(k)) {
                  screen.append(appleC);
                  screen.append(" ");
                  continue l2;
                }
              }
              if (passed[i][y_length-j-1]) {
                screen.append(snakeC);
                screen.append(" ");
              } else {
                screen.append(boardC);
                screen.append(" ");
              }
            }
            screen.append("\n");
          }
          System.out.print(screen);

          if (first && !notFirst2) {
            System.out.print(screen);
            System.out.print("\n\'Look here!\'");
            System.out.print(screen);
            System.out.print("Press wasd to change direction.\nPress enter to move: ");
            first = false;
          }

          move = scan.nextLine();
        }

        if (notFirst2) {
          scoreS = "\n" + score + " | " + highScore;
        } else {
          scoreS = "\n" + score;
        }

        // Plays death animation
        if (death) {
          for (int k = xs.size()-1; k > -1; k--) {
            // Snake body removed one by one
            passed[xs.get(k)][ys.get(k)] = false;
            xs.remove(k);
            ys.remove(k);
            try {
              Thread.sleep(Math.round((Math.log(Math.pow(Math.E, 4) + snake_length) - 3.8)/(double) snake_length * 1000));
            } catch (InterruptedException ex) {
              Thread.currentThread().interrupt();
            }
            // Shows screen
            screen = new StringBuilder(scoreS);
            screen.append("\n");
            for (int i = 0; i < x_length; i++) {
              l3: for (int j = 0; j < y_length; j++) {
                for (int l = 0; l < appleX.size(); l++) {
                  if (i == appleX.get(l) && j == appleY.get(l)) {
                    screen.append(appleC);
                    screen.append(" ");
                    continue l3;
                  }
                }
                if (passed[i][y_length-j-1]) {
                  screen.append(snakeC);
                  screen.append(" ");
                } else {
                  screen.append(boardC);
                  screen.append(" ");
                }
              }
              screen.append("\n");
            }
            System.out.println(screen);
          }
        } else {
          System.out.println("VICTORY!!!");
        }

        // Displays final score and high score
        System.out.print("Score = ");
        System.out.print(score);
        if (score == 69) {
          System.out.print(" nice");
        }
        if (notFirst2) {
          System.out.print("\tHigh score = ");
          System.out.print(highScore);
        }

        // Asks for retry, quit, or settings
        System.out.print("\nRetry?   (yes, no, or settings): ");
        while (!(retry.equalsIgnoreCase("y") || retry.equalsIgnoreCase("yes") || retry.equalsIgnoreCase("n") || retry.equalsIgnoreCase("no") || retry.equalsIgnoreCase("set") ||retry.equalsIgnoreCase("settings"))) {    // "settings" not shortened to "s" because it could be confused with a wasd move
          retry = scan.nextLine();
        }

        if ((retry.equalsIgnoreCase("n") || retry.equalsIgnoreCase("no"))) {
          break;
        }

        // Saves previous settings and if it's not the first game
        notFirst2 = true;
        lastXLength = x_length;
        lastYLength = y_length;
        lastAppleC = appleC;
        lastSnakeC = snakeC;
        lastBoardC = boardC;
        lastAppleI = appleI;
    }
  }
}
