import java.io.*;
import java.util.ArrayList;
import java.util.Random;

// Called "Reminisce" b/c it's an incredibly clever pun of the prefix re- and Menace from: https://www.youtube.com/watch?v=R9c-_neaxeU
  // It's a TicTacToe bot
// I also made a ConnectFour bot
  // Reason I'm not putting that on Github is b/c it plays relatively unimpressively
  // Got it to play 256 games, which is great and all, but that's nothing compared to the maybe trillions of games possible
// This is my --first-- second successful attempt at working with files, I hope you like it :)

public class Reminisce {
  private final File fil;
  private FileWriter fw;
  private FileReader fr;
  private ArrayList<Integer> possibleMoves = new ArrayList<>();
  private final boolean x;
  private ArrayList<String> movesSoFar = new ArrayList<>();
  private int wins = 0;
  private int draws = 0;
  private int losses = 0;

  public Reminisce() throws IOException {
    this(".\\ConnectFour\\src\\memory.txt");
  }

  public Reminisce(String file) throws IOException {
    this(file, true);
  }

  public Reminisce(String file, boolean x) throws IOException {
    fil = new File(file);
    fw = new FileWriter(fil, true);
    fr = new FileReader(fil);
    for (int i = 1; i <= 9; i++) possibleMoves.add(i);
    this.x = x;
  }

  // Appends message to file
  public void append(String message) throws IOException {
    fw = new FileWriter(fil, true);
    fw.write(message);
    fw.close();
  }

  // Writes message to file
    // Difference btwn this and append is that this is for removing and replacing the "!"
      // "!" at the end of file signifies the end so readLine knows when to stop reading
  public void writeLine(String message) throws IOException {
    removeLastLine();
    fw = new FileWriter(fil, true);
    fw.write(message);
    fw.write("\n!");
    fw.close();
  }

  // Refills file with given lines
  public void refill(ArrayList<String> lines) throws IOException {
    fw = new FileWriter(fil);
    for (String line : lines) {
      fw.write(line);
      fw.write('\n');
    }
    fw.close();
  }

  // Removes message from file
    // Notably does so using binary search algorithm that I didn't make b/c lazy
  public void removeLine(String message) throws IOException {
    ArrayList<String> linesMod = readMostLines();
    int[] nums = ArrListStrToIntArr(linesMod);
    int index = GFG.binarySearch(nums, Integer.parseInt(message));
    if (index != -1) linesMod.remove(index);
    refill(linesMod);
    append("!");
  }

  // Removes line at given index from file
  public void removeLine(int index) throws IOException {
    ArrayList<String> linesMod = readMostLines();
    linesMod.remove(index);
    refill(linesMod);
    append("!");
  }

  // Removes very last line of file
  public void removeLastLine() throws IOException {
    ArrayList<String> linesMod = readLines();
    linesMod.remove(linesMod.size() - 1);
    refill(linesMod);
  }

  // Reads single line
  public String readLine() throws IOException {
    StringBuilder str = new StringBuilder();
    while (true) {
      char read = (char) fr.read();
      if (read == '\n') {
        break;
      }
      str.append(read);
      if (read == '!') {
        break;
      }
    }
    return str.toString();
  }

  // Reads all the lines including "!"
  public ArrayList<String> readLines() throws IOException {
    fr = new FileReader(fil);
    ArrayList<String> lines = new ArrayList<>();
    String line;
    while (true) {
      line = readLine();
      lines.add(line);
      if (line.contains("!")) {
        break;
      }
    }
    fr.close();
    return lines;
  }

  // Reads all the lines excluding "!"
    // This is important, I swear
  public ArrayList<String> readMostLines() throws IOException {
    fr = new FileReader(fil);
    ArrayList<String> lines = new ArrayList<>();
    String line;
    while (true) {
      line = readLine();
      if (line.contains("!")) {
        break;
      }
      lines.add(line);
    }
    fr.close();
    return lines;
  }

  // Decides next move given a String of the current game
  public int decideMove(String game) throws IOException {
    // Gets lines from txt.file
    ArrayList<String> lines = readMostLines();
    ArrayList<String> options = new ArrayList<>();
    String line;
    // Ex: game is 5327, what show it do next?
      // It checks all the lines that have 5327 and another digit
      // Let's say there's 53269, 53271, 53274, 53278, 53278, 53278, 53279, 53281
      // Note that not only are there duplicates, but that they're vital for this bot to "learn"
    // It starts at first index that has 5327 + digit, saves it into options, and goes until it's not 5327 + digit
    for (int i = findFirstIndex(game); i < lines.size(); i++) {
      line = lines.get(i);
      if (game.equals(line.substring(0, line.length() - 1))) {
        options.add(line);
      } else {
        break;
      }
    }
    // Finds all the possible moves
    ArrayList<Integer> possibleMoves = possibleMoves(game);
    String newGame;
    // If there are no lines, it makes 2 of all the ones currently possible
      // In the Menace video, they had 2 of every possible position
      // Won't do that since that's unnecessarily hard
      // Instead, this just makes them appear out of thin air whenever it needs to
    if (options.size() == 0) {
      for (int possibleMove : possibleMoves) {
        newGame = game + possibleMove;
        for (int i = 0; i < 2; i++) {
          writeLine(newGame);
          options.add(newGame);
        }
      }
    }
    // Choose random move from determined possible options
    Random rand = new Random();
    String moveToMake = options.get(rand.nextInt(options.size()));
    // Saves it to movesSoFar, so it knows what to do with them after the game
    movesSoFar.add(moveToMake);
    // If we got 53278 from earlier, this would return the last digit 8
    return moveToMake.charAt(moveToMake.length() - 1) - 48;
  }

  // Returns list of currently possible moves
  public ArrayList<Integer> possibleMoves(String game) {
    // Turns String of current game to int[]
    int[] numbers = new int[game.length()];
    for (int i = 0; i < game.length(); i++) {
      numbers[i] = Integer.parseInt(game.substring(i, i+1));
    }
    // Removes number from possibleMoves if it sees it's been played in the game
    int possibleMove;
    l1: for (int i = possibleMoves.size() - 1; i >= 0; i--) {
      possibleMove = possibleMoves.get(i);
      for (int number : numbers) {
        if (possibleMove == number) {
          possibleMoves.remove((Integer) possibleMove);
          continue l1;
        }
      }
    }
    return possibleMoves;
  }

  // If bot wins, it makes copies of the moves they made. Else it, forgets them
  public void contemplate(boolean win) throws IOException {
    if (win) {
      for (String move : movesSoFar) {
        // i < 2 makes 2 copies
        for (int i = 0; i < 2; i++) {
          writeLine(move);
        }
      }
    } else {
      // i < 1 discards 1 copy
        // Kept for-loop to quickly change how many copies to copy or discard if one so desires
      for (String move : movesSoFar) {
        for (int i = 0; i < 1; i++) {
          removeLine(move);
        }
      }
    }
    movesSoFar = new ArrayList<>();
  }

  // Same as last one, but a String for a win to account for draws
    // Also counts wins, draws, and losses
  public void contemplate(String win) throws IOException {
    if (win.equals("w")) {
      wins++;
      for (String move : movesSoFar) {
        for (int i = 0; i < 2; i++) {
          writeLine(move);
        }
      }
    } else if (win.equals("l")) {
      losses++;
      for (String move : movesSoFar) {
        for (int i = 0; i < 1; i++) {
          removeLine(move);
        }
      }
    } else {
      draws++;
      for (String move : movesSoFar) {
        for (int i = 0; i < 0; i++) {
          writeLine(move);
        }
      }
    }
    movesSoFar = new ArrayList<>();
  }

  // Bot recuperates after game, resetting possibleMoves and sorting out their memory
  public void recuperate() throws IOException {
    possibleMoves = new ArrayList<>();
    for (int i = 1; i <= 9; i++) {
      possibleMoves.add(i);
    }
    sort();
  }

  // Sorts file in increasing order using QuickSort
    // Took the QuickSort class from online b/c I'm lazy
  public void sort() throws IOException {
    ArrayList<String> lines = readMostLines();
    fw = new FileWriter(fil);
    int[] nums = ArrListStrToIntArr(lines);
    QuickSort.sort(nums, 0, lines.size() - 1);
    for (int num : nums) {
      fw.write(String.valueOf(num));
      fw.write('\n');
    }
    fw.write('!');
    fw.close();
  }

  // Turns ArrayList<String> to int[]
  public int[] ArrListStrToIntArr(ArrayList<String> arrList) {
    int[] arr = new int[arrList.size()];
    for (int i = 0; i < arrList.size(); i++) {
      arr[i] = Integer.parseInt(arrList.get(i));
    }
    return arr;
  }

  // Finds first index that has game + digit
  public int findFirstIndex(String target) throws IOException {
    ArrayList<String> lines = readMostLines();
    if (lines.size() == 0) return 0;
    int[] nums = ArrListStrToIntArr(lines);
    return GFG.binarySearch(nums, Integer.parseInt(target + "0"));
  }

  // Clears out bot's memory
  public void clear() throws IOException {
    fw = new FileWriter(fil);
    fw.write("!");
    fw.close();
  }

  // Prints out wins/draws/losses
  public void printRatio() {
    System.out.println(wins + "/" + draws + "/" + losses);
  }

  public static void main(String[] args) throws IOException {
    // Tested it up to 2048 games, and it works fine
      // Tested ConnectFour bot up to 256 games, and that worked fine too
      // Seeing a ConnectFour bot in action is left as an exercise to the reader
    Reminisce RemiX = new Reminisce(".\\TicTacToe\\src\\memoryX.txt", true);
    Reminisce RemiO = new Reminisce(".\\TicTacToe\\src\\memoryO.txt", false);
    RemiX.clear();
    RemiO.clear();
  }
}
