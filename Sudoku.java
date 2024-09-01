// Can solve Sudokus and generate new ones
// Blank spaces in grids are represented with 0s

import java.util.*;

public class Sudoku {
    // we define "val" or "value" such that arr[i][j] is associated with the value i*9+j
        // equivalently, val is associated with arr[val / 9][val % 9]
    // this maps values to the possible numbers that can be in the corresponding arr[i][j]
    private final Map<Integer, Set<Integer>> possGrid;
    // this gets initialized with grid, gets filled out as the sudoku is being solved
    private int[][] grid;
    // this gets initialized in Sudoku(grid, gridCopy) as a copy of grid, doesn't get filled out like grid does
    private int[][] gridCopy;
    // this maps values to the values of its row, column, and box neighbors
    private static Map<Integer, Set<Integer>> neighbors;

    // standard constructor for Sudoku
    public Sudoku(int[][] grid) {
        if (!validateGrid(grid)) {
            print(grid);
            throw new IllegalArgumentException();
        }
        possGrid = new TreeMap<>();
        this.grid = new int[9][9];
        neighbors = new TreeMap<>();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                this.grid[i][j] = grid[i][j];
                int val = i*9 + j;
                Set<Integer> possGridVal = new HashSet<>();
                Set<Integer> neighborsVal = new HashSet<>();

                possGrid.put(val, possGridVal);
                neighbors.put(val, neighborsVal);
                neighborsVal.addAll(getRowNeighbors(i));
                neighborsVal.addAll(getColNeighbors(j));
                neighborsVal.addAll(getBoxNeighbors(i, j));
                if (grid[i][j] != 0) {
                    continue;
                }
                for (int k = 1; k <= 9; k++) {
                    possGridVal.add(k);
                }
            }
        }
    }

    // gridCopy rarely utilized. if it is, this constructor is used
    private Sudoku(int[][] grid, int[][] gridCopy) {
        this(grid);
        this.gridCopy = new int[9][9];
        for (int i = 0; i < 9; i++) {
            System.arraycopy(gridCopy[i], 0, this.gridCopy[i], 0, 9);
        }
    }

    // returns the values for a given row
    private static Set<Integer> getRowNeighbors(int row) {
        Set<Integer> rowNeighbors = new HashSet<>();
        for (int col = 0; col < 9; col++) {
            rowNeighbors.add(row*9 + col);
        }
        return rowNeighbors;
    }

    // returns the values for a given column
    private static Set<Integer> getColNeighbors(int col) {
        Set<Integer> colNeighbors = new HashSet<>();
        for (int row = 0; row < 9; row++) {
            colNeighbors.add(row*9 + col);
        }
        return colNeighbors;
    }

    // returns the values for a box containing the space with the given row and column
    private static Set<Integer> getBoxNeighbors(int row, int col) {
        Set<Integer> boxNeighbors = new HashSet<>();
        for (int i = row/3*3; i < row/3*3+3; i++) {
            for (int j = col/3*3; j < col/3*3+3; j++) {
                boxNeighbors.add(i*9 + j);
            }
        }
        return boxNeighbors;
    }

    // returns the values in the intersection between a row and box
    private static Set<Integer> getRowCapBox(int row, int col) {
        Set<Integer> cap = new HashSet<>();
        for (int j = col/3*3; j < col/3*3+3; j++) {
            cap.add(row*9 + j);
        }
        return cap;
    }

    // returns the values in the intersection between a column and box
    private static Set<Integer> getColCapBox(int row, int col) {
        Set<Integer> cap = new HashSet<>();
        for (int i = row/3*3; i < row/3*3+3; i++) {
            cap.add(i*9 + col);
        }
        return cap;
    }

    // updates possGrid, done after grid[val / 9][val % 9] is assigned a number
    private void updatePossGrid(int val) {
        possGrid.get(val).clear();
        for (int nval : neighbors.get(val)) {
            if (grid[nval / 9][nval % 9] == 0) {
                possGrid.get(nval).remove(grid[val / 9][val % 9]);
            }
        }
    }

    // if possGrid maps vals to possibilities, this returns a map mapping possibilities to vals
    private Map<Integer, Set<Integer>> inversePossGrid(Set<Integer> vals) {
        Map<Integer, Set<Integer>> inverse = new TreeMap<>();
        for (int i = 1; i <= 9; i++) {
            inverse.put(i, new HashSet<>());
        }
        for (int val : vals) {
            for (int poss : possGrid.get(val)) {
                inverse.get(poss).add(val);
            }
        }
        return inverse;
    }

    // checks if possGrid only has 1 possible number for grid[val / 9][val % 9]
    private boolean checkOnes(int val) {
        if (possGrid.get(val).size() == 1) {
            grid[val / 9][val % 9] = possGrid.get(val).iterator().next();
            updatePossGrid(val);
            return true;
        }
        return false;
    }

    // checks if val is the only element in vals such that grid[val / 9][val % 9] contains some possibility
    private boolean checkOnlys(Set<Integer> vals, int val) {
        Map<Integer, Set<Integer>> inverse = inversePossGrid(vals);
        for (int poss : possGrid.get(val)) {
            if (inverse.get(poss).size() == 1) {
                grid[val / 9][val % 9] = poss;
                updatePossGrid(val);
                return true;
            }
        }
        return false;
    }

    // checkOnlys for a val with its row, column, or box neighbors
    private boolean checkOnlys(int val) {
        if (checkOnlys(getRowNeighbors(val / 9), val)) {
            return true;
        }
        if (checkOnlys(getColNeighbors(val % 9), val)) {
            return true;
        }
        return checkOnlys(getBoxNeighbors(val / 9, val % 9), val);
    }

    // checksOnes and checkOnlys for val
    private boolean check(int val) {
        if (checkOnes(val)) {
            return true;
        }
        return checkOnlys(val);
    }

    // returns arr[] where arr[i] is the number of times i+1 is a possible value for a spaces with the given vals
    private int[] getPossOccurs(Set<Integer> vals) {
        int[] occurs = new int[9];
        for (int val : vals) {
            for (int poss : possGrid.get(val)) {
                occurs[poss - 1]++;
            }
        }
        return occurs;
    }

    // helper method for checkCap(). if a row and box intersection contains the only possibilities for a number
    // in that row, then the intersection is onlyedVals, box is otherVals, said number is index+1
    private boolean checkCap(Set<Integer> onlyedVals, Set<Integer> otherVals, int index) {
        boolean changed = false;
        for (int val : otherVals) {
            if (onlyedVals.contains(val)) {
                continue;
            }
            changed = possGrid.get(val).remove(index + 1) || changed;
        }
        return changed;
    }

    // another helper method for checkCap(). checks the intersections containing the given row and column
    private boolean checkCap(int row, int col) {
        Set<Integer> rowVals = getRowNeighbors(row);
        Set<Integer> colVals = getColNeighbors(col);
        Set<Integer> boxVals = getBoxNeighbors(row, col);
        Set<Integer> rowCapBoxVals = getRowCapBox(row, col);
        Set<Integer> colCapBoxVals = getColCapBox(row, col);

        int[] rowPosses = getPossOccurs(rowVals);
        int[] colPosses = getPossOccurs(colVals);
        int[] boxPosses = getPossOccurs(boxVals);
        int[] rowCapBoxPosses = getPossOccurs(rowCapBoxVals);
        int[] colCapBoxPosses = getPossOccurs(colCapBoxVals);

        boolean changed = false;
        for (int index = 0; index < 9; index++) {
            if (rowCapBoxPosses[index] > 0) {
                if (rowCapBoxPosses[index] == rowPosses[index]) {
                    changed = checkCap(rowVals, boxVals, index) || changed;
                }
                if (rowCapBoxPosses[index] == boxPosses[index]) {
                    changed = checkCap(boxVals, rowVals, index) || changed;
                }
            }
            if (colCapBoxPosses[index] > 0) {
                if (colCapBoxPosses[index] == colPosses[index]) {
                    changed = checkCap(colVals, boxVals, index) || changed;
                }
                if (colCapBoxPosses[index] == boxPosses[index]) {
                    changed = checkCap(boxVals, colVals, index) || changed;
                }
            }
        }
        return changed;
    }

    // consider the row 0 0 1 2 3 4 5 6 7. you don't know which 0 is 8 or 9, but you know that 8 or 9 are both there
    // the box that contains both 0s thus can't have an 8 or 9 outside that intersection
    // this handles that logic by checking every intersection
    private boolean checkCap() {
        // hard coded indices [i+k][j+k] which uniquely cover every intersection between a row/column and box
        boolean changed = false;
        for (int i = 0; i < 9; i += 3) {
            for (int j = 0; j < 9; j += 3) {
                for (int k = 0; k < 3; k++) {
                    changed = checkCap(i + k, j + k) || changed;
                }
            }
        }
        return changed;
    }

    // if all previous checks weren't enough, guesses a number in grid and tries to solve from there
    // returns 0 if no solution, 1 if unique sol, 2 if multiple sols
    private int guess() {
        int minSizeVal = 0;
        int minSize = 10;
        for (int val = 0; val < 81; val++) {
            if (grid[val / 9][val % 9] != 0) {
                continue;
            }
            if (possGrid.get(val).size() < minSize) {
                minSize = possGrid.get(val).size();
                minSizeVal = val;
            }
        }
        boolean foundOne = false;
        int[][] savedGrid = grid;
        for (int poss : possGrid.get(minSizeVal)) {
            grid[minSizeVal / 9][minSizeVal % 9] = poss;
            Sudoku guess = new Sudoku(grid);
            int result = guess.solveHard();
            grid[minSizeVal / 9][minSizeVal % 9] = 0;
            if (result == 0) {
                continue;
            }
            if (result == 2) {
                return 2;
            }
            if (foundOne) {
                return 2;
            }
            foundOne = true;
            savedGrid = guess.grid;
        }
        if (foundOne) {
            grid = savedGrid;
            return 1;
        }
        return 0;
    }

    // solves using only checkOnes. used to determine difficulty
    // returns 0 if no sol, 1 if unique sol, -1 if undetermined
    private int solveEasy() {
        for (int val = 0; val < 81; val++) {
            if (grid[val / 9][val % 9] != 0) {
                updatePossGrid(val);
            }
        }
        boolean filledOut;
        while (true) {
            boolean added = false;
            filledOut = true;
            for (int val = 0; val < 81; val++) {
                if (grid[val / 9][val % 9] != 0) {
                    continue;
                }
                if (possGrid.get(val).isEmpty()) {
                    return 0;
                }
                added = checkOnes(val) || added;
                filledOut = false;
            }
            if (filledOut) {
                break;
            }
            if (!added) {
                break;
            }
        }
        if (!filledOut) {
            return -1;
        }
        return 1;
    }

    // solves using checkOnes, checkOnlys, and checkCaps. used to determine difficulty
    // returns 0 if no sol, 1 if unique sol, -1 if undetermined
    private int solveMedium() {
        for (int val = 0; val < 81; val++) {
            if (grid[val / 9][val % 9] != 0) {
                updatePossGrid(val);
            }
        }
        boolean filledOut;
        while (true) {
            boolean added = false;
            filledOut = true;
            for (int val = 0; val < 81; val++) {
                if (grid[val / 9][val % 9] != 0) {
                    continue;
                }
                if (possGrid.get(val).isEmpty()) {
                    return 0;
                }
                added = check(val) || added;
                filledOut = false;
            }
            if (filledOut) {
                break;
            }
            if (!added && !checkCap()) {
                break;
            }
        }
        if (!filledOut) {
            return -1;
        }
        return 1;
    }

    // solves using checkOnes, checkOnlys, checkCaps, and guesses (everything)
    // returns 0 if no sol, 1 if unique sol, 2 if multiple sols
    private int solveHard() {
        int result = solveMedium();
        if (result != -1) {
            return result;
        }
        int guess = guess();
        if (guess == 0) {
            return 0;
        }
        if (guess == 2) {
            return 2;
        }
        return 1;
    }

    // solves, returns solution, conditionally prints solvability
    private int[][] solve(boolean print) {
        int result = solveHard();
        if (print) {
            if (result == 0) {
                System.out.println("No solutions");
            } else if (result == 1) {
                System.out.println("Unique solution");
            } else if (result == 2) {
                System.out.println("No unique solution");
            }
        }
        return grid;
    }

    public int[][] solve() {
        return solve(true);
    }

    // checks if grid is a 9-by-9 matrix and contains numbers 0 to 9 inclusive, conditionally prints error messages
    private static boolean validateSize(int[][] grid, boolean print) {
        if (grid == null) {
            if (print) {
                System.out.println("Nothing to validate");
            }
            return false;
        }
        if (grid.length != 9) {
            if (print) {
                System.out.println("Incorrect size");
            }
            return false;
        }
        for (int i = 0; i < 9; i++) {
            if (grid[i].length != 9) {
                if (print) {
                    System.out.println("Incorrect size");
                }
                return false;
            }
            for (int j = 0; j < 9; j++) {
                if (grid[i][j] < 0 || grid[i][j] > 9) {
                    if (print) {
                        System.out.println("Invalid numbers");
                    }
                    return false;
                }
            }
        }
        return true;
    }
    private static boolean validateSize(int[][] grid) {
        return validateSize(grid, true);
    }

    // returns whether there is a duplicate number in grid over vals
    private static boolean checkGridOccurs(int[][] grid, Set<Integer> vals) {
        int[] times = new int[10];
        for (int val : vals) {
            times[grid[val / 9][val % 9]]++;
            if (grid[val / 9][val % 9] != 0 && times[grid[val / 9][val % 9]] > 1) {
                return false;
            }
        }
        return true;
    }

    // checks if grid is right size and no duplicates in rows/columns/boxes. conditionally prints error messages
    private static boolean validateGrid(int[][] grid, boolean print) {
        if (!validateSize(grid)) {
            return false;
        }
        for (int col = 0; col < 9; col++) {
            int row = (col*3 + col*3/9) % 9;
            Set<Integer> rowVals = getRowNeighbors(row);
            Set<Integer> colVals = getColNeighbors(col);
            Set<Integer> boxVals = getBoxNeighbors(row, col);

            if (!checkGridOccurs(grid, rowVals)) {
                if (print) {
                    System.out.println("row not uniques: row " + row);
                }
                return false;
            }
            if (!checkGridOccurs(grid, colVals)) {
                if (print) {
                    System.out.println("col not uniques: col " + col);
                }
                return false;
            }
            if (!checkGridOccurs(grid, boxVals)) {
                if (print) {
                    System.out.println("box not uniques: row, col " + row + ", " + col);
                }
                return false;
            }
        }
        return true;
    }

    private static boolean validateGrid(int[][] grid) {
        return validateGrid(grid, true);
    }

    // checks if answer is valid grid, contains no 0s, contains all nonzero numbers in original
    // condiitionally prints error messages
    private static boolean verifyAnswer(int[][] original, int[][] answer, boolean print) {
        if (!validateGrid(answer)) {
            return false;
        }
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (answer[i][j] == 0) {
                    if (print) {
                        System.out.println("Answer has a blank");
                    }
                    return false;
                }
                if (original[i][j] != 0 && original[i][j] != answer[i][j]) {
                    if (print) {
                        System.out.println("Replaced an original value");
                    }
                    return false;
                }
            }
        }
        return true;
    }
    
    public static boolean verifyAnswer(int[][] original, int[][] answer) {
        return verifyAnswer(original, answer, true);
    }

    // converts set to randomly ordered list
    private static List<Integer> shuffle(Set<Integer> set) {
        int[] arr = new int[set.size()];
        int index = 0;
        for (int num : set) {
            arr[index] = num;
            index++;
        }
        Arrays.sort(arr);
        List<Integer> indices = new LinkedList<>();
        for (int i = 0; i < arr.length; i++) {
            indices.add(i);
        }
        Random rand = new Random();
        List<Integer> shuffled = new LinkedList<>();
        for (int i = 0; i < arr.length; i++) {
            shuffled.add(arr[indices.remove(rand.nextInt(indices.size()))]);
        }
        return shuffled;
    }

    // guess but working with gridCopy
    // while solving, returns a grid with a unique sol. grid not used since solving completely fills it out
    private int modifiedGuess() {
        List<Integer> minSizeVals = new LinkedList<>();
        int minSize = 10;
        for (int val = 0; val < 81; val++) {
            if (grid[val / 9][val % 9] != 0) {
                continue;
            }
            if (possGrid.get(val).size() < minSize) {
                minSize = possGrid.get(val).size();
                minSizeVals.clear();
                minSizeVals.add(val);
            } else if (possGrid.get(val).size() == minSize) {
                minSizeVals.add(val);
            }
        }
        Random rand = new Random();
        int randVal = minSizeVals.remove(rand.nextInt(minSizeVals.size()));
        boolean foundOne = false;
        int[][] savedGrid = gridCopy;
        for (int poss : shuffle(possGrid.get(randVal))) {
            grid[randVal / 9][randVal % 9] = poss;
            gridCopy[randVal / 9][randVal % 9] = poss;
            Sudoku guess = new Sudoku(grid, gridCopy);
            int result = guess.modifiedSolve();
            grid[randVal / 9][randVal % 9] = 0;
            gridCopy[randVal / 9][randVal % 9] = 0;
            if (result == 0) {
                continue;
            }
            if (result == 2) {
                gridCopy = guess.gridCopy;
                gridCopy[randVal / 9][randVal % 9] = poss;
                return 2;
            }
            if (foundOne) {
                gridCopy[randVal / 9][randVal % 9] = poss;
                return 2;
            }
            foundOne = true;
            savedGrid = guess.gridCopy;
        }
        if (foundOne) {
            gridCopy = savedGrid;
            return 1;
        }
        return 0;
    }

    // solveHard but calls modifiedGuess instead of guess. used in modifiedGuess
    private int modifiedSolve() {
        int result = solveMedium();
        if (result != -1) {
            return result;
        }
        int guess = modifiedGuess();
        if (guess == 0) {
            return 0;
        }
        if (guess == 2) {
            return 2;
        }
        return 1;
    }

    // returns the vals of the 0s in grid
    private static List<Integer> getEmptyVals(int[][] grid) {
        List<Integer> emptyVals = new LinkedList<>();
        for (int val = 0; val < 81; val++) {
            if (grid[val / 9][val % 9] == 0) {
                emptyVals.add(val);
            }
        }
        return emptyVals;
    }

    // returns 1 for easy, 2 for medium, 3 for hard
    private int difficulty() {
        int easy = solveEasy();
        if (easy != -1) {
            return 1;
        }
        int medium = solveMedium();
        if (medium != -1) {
            return 2;
        }
        return 3;
    }

    // converts a medium grid to an easy one
    private static int[][] convertMediumToEasy(int[][] medium) {
        Sudoku newSudoku = new Sudoku(medium);
        int[][] solution = newSudoku.solve(false);
        newSudoku = new Sudoku(medium, medium);
        newSudoku.solveEasy();
        int[][] newGrid = newSudoku.gridCopy;
        Random rand = new Random();
        int added = 10;
        while (true) {
            List<Integer> emptyVals = getEmptyVals(newSudoku.grid);
            if (emptyVals.isEmpty()) {
                break;
            }
            int randVal = emptyVals.remove(rand.nextInt(emptyVals.size()));
            newGrid[randVal / 9][randVal % 9] = solution[randVal / 9][randVal % 9];
            added--;
            newSudoku = new Sudoku(newGrid);
            if (newSudoku.solveEasy() == 1) {
                break;
            }
        }
        List<Integer> emptyVals = getEmptyVals(newGrid);
        for (int i = 0; i < added; i++) {
            int randVal = emptyVals.remove(rand.nextInt(emptyVals.size()));
            newGrid[randVal / 9][randVal % 9] = solution[randVal / 9][randVal % 9];
        }
        return newGrid;
    }

    // converts a hard grid to a medium (possibly easy) one
    private static int[][] convertHardToMedium(int[][] hard) {
        Sudoku newSudoku = new Sudoku(hard);
        int[][] solution = newSudoku.solve(false);
        newSudoku = new Sudoku(hard, hard);
        newSudoku.solveMedium();
        int[][] newGrid = newSudoku.gridCopy;
        Random rand = new Random();
        while (true) {
            List<Integer> emptyVals = getEmptyVals(newSudoku.grid);
            if (emptyVals.isEmpty()) {
                break;
            }
            int randVal = emptyVals.remove(rand.nextInt(emptyVals.size()));
            newGrid[randVal / 9][randVal % 9] = solution[randVal / 9][randVal % 9];
            newSudoku = new Sudoku(newGrid);
            if (newSudoku.solveMedium() == 1) {
                break;
            }
        }
        return newGrid;
    }

    // generates a random sudoku with a unique sol. empirically has around 25 numbers filled
    private static int[][] generate() {
        int[][] initial = new int[9][9];
        Random rand = new Random();
        // fills initial 5 times with a number that keeps initial valid
        for (int i = 0; i < 5; i++) {
            int randVal = rand.nextInt(81);
            initial[randVal / 9][randVal % 9] = rand.nextInt(9) + 1;
            if (!validateGrid(initial, false)) {
                initial[randVal / 9][randVal % 9] = 0;
            }
        }
        Sudoku newSudoku = new Sudoku(initial, initial);
        int result = newSudoku.solveHard();
        newSudoku = new Sudoku(initial, initial);
        if (result == 0) {
            // IllegalArgumentExceptions just indicating that something terribly wrong has happened
            throw new IllegalArgumentException();
        }
        if (result == 1) {
            throw new IllegalArgumentException();
        }
        newSudoku.modifiedSolve();
        // newSudoku now has a unique solution but likely has a lot of redundant numbers filled
        // following loop removes some of those redundant numbers
        for (int i = 0; i < 20; i++) {
            List<Integer> filledVals = new LinkedList<>();
            for (int val = 0; val < 81; val++) {
                if (newSudoku.gridCopy[val / 9][val % 9] != 0) {
                    filledVals.add(val);
                }
            }
            boolean changed = false;
            while (!filledVals.isEmpty()) {
                int randVal = filledVals.remove(rand.nextInt(filledVals.size()));
                int sub = newSudoku.gridCopy[randVal / 9][randVal % 9];
                newSudoku.gridCopy[randVal / 9][randVal % 9] = 0;
                Sudoku newerSudoku = new Sudoku(newSudoku.gridCopy);
                result = newerSudoku.solveHard();
                if (result == 0) {
                    throw new IllegalArgumentException();
                }
                if (result == 1) {
                    changed = true;
                    break;
                }
                if (result == 2) {
                    newSudoku.gridCopy[randVal / 9][randVal % 9] = sub;
                }
            }
            if (!changed) {
                break;
            }
        }
        return newSudoku.gridCopy;
    }

    // generates an easy sudoku
    public static int[][] generateEasy() {
        int[][] generated = generate();
        int difficulty = new Sudoku(generated).difficulty();
        if (difficulty == 1) {
            return generated;
        }
        if (difficulty == 2) {
            return convertMediumToEasy(generated);
        }
        return convertMediumToEasy(convertHardToMedium(generated));
    }

    // generates a medium sudoku
    public static int[][] generateMedium() {
        int[][] generated;
        int difficulty;
        do {
            generated = generate();
            difficulty = new Sudoku(generated).difficulty();
        } while (difficulty == 1);
        if (difficulty == 2) {
            return generated;
        }
        generated = convertHardToMedium(generated);
        if (new Sudoku(generated).difficulty() == 1) {
            return generateMedium();
        }
        return generated;
    }

    // generates a hard sudoku
    public static int[][] generateHard() {
        int[][] generated;
        do {
            generated = generate();
        } while (new Sudoku(generated).difficulty() != 3);
        return generated;
    }

    // prints possGrid
    private void printPossGrid() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                int val = i*9 + j;
                System.out.println(possGrid.get(val));
            }
            System.out.println();
        }
    }

    // prints grid
    private void printGrid() {
        print(grid);
    }

    // prints a 2d int array
    private static void print(int[][] arr) {
        for (int[] nums : arr) {
            for (int num : nums) {
                System.out.print(num + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    // returns the number of nonzero elements in arr
    private static int countFilled(int[][] arr) {
        int count = 0;
        for (int[] ints : arr) {
            for (int anInt : ints) {
                if (anInt != 0) {
                    count++;
                }
            }
        }
        return count;
    }

    public static void main(String[] args) {
        int[][] easy = new int[][] {
            {3, 0, 0, 7, 8, 6, 0, 2, 4},
            {5, 0, 4, 0, 0, 2, 6, 0, 7},
            {0, 6, 2, 5, 0, 9, 0, 0, 8},
            {6, 0, 0, 0, 3, 0, 1, 8, 0},
            {0, 3, 0, 8, 2, 0, 7, 6, 0},
            {1, 2, 0, 0, 5, 0, 0, 4, 0},
            {0, 4, 6, 2, 0, 0, 0, 5, 0},
            {0, 0, 0, 0, 9, 0, 0, 0, 6},
            {0, 0, 5, 1, 0, 8, 0, 0, 0}
        };
        int[][] easy2 = new int[][] {
            {2, 4, 0, 5, 6, 0, 0, 9, 0},
            {0, 6, 3, 0, 9, 0, 0, 2, 5},
            {0, 0, 5, 3, 0, 0, 7, 6, 0},
            {0, 1, 2, 6, 0, 5, 9, 4, 0},
            {9, 0, 8, 0, 0, 0, 0, 0, 0},
            {6, 0, 0, 2, 0, 0, 0, 5, 3},
            {7, 0, 0, 9, 0, 6, 0, 0, 4},
            {0, 0, 6, 4, 7, 3, 1, 0, 0},
            {0, 0, 0, 0, 0, 1, 0, 7, 2}
        };
        int[][] medium = new int[][] {
            {0, 6, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 1, 0, 0, 0, 0, 0, 0},
            {0, 0, 9, 8, 2, 0, 1, 0, 5},
            {8, 0, 2, 0, 0, 4, 9, 0, 0},
            {0, 0, 6, 9, 0, 0, 3, 0, 0},
            {0, 0, 0, 0, 0, 0, 5, 0, 1},
            {0, 0, 4, 5, 0, 0, 2, 0, 0},
            {6, 0, 0, 0, 0, 2, 0, 0, 0},
            {0, 7, 0, 3, 1, 0, 0, 0, 0}
        };
        int[][] medium2 = new int[][] {
            {7, 0, 0, 6, 2, 0, 0, 5, 0},
            {0, 0, 5, 4, 0, 0, 2, 6, 0},
            {0, 0, 2, 0, 0, 0, 0, 0, 0},
            {0, 0, 7, 0, 0, 0, 3, 4, 8},
            {0, 0, 0, 0, 9, 1, 0, 0, 0},
            {0, 0, 0, 0, 4, 8, 0, 0, 0},
            {0, 8, 0, 0, 0, 2, 7, 3, 0},
            {0, 7, 0, 5, 0, 0, 0, 9, 0},
            {0, 0, 6, 1, 0, 0, 0, 0, 0}
        };
        int[][] hard = new int[][] {
            {0, 9, 0, 6, 8, 0, 4, 0, 0},
            {0, 0, 0, 2, 9, 5, 8, 0, 0},
            {0, 0, 0, 4, 0, 0, 0, 0, 0},
            {0, 2, 0, 0, 0, 0, 0, 0, 6},
            {0, 0, 0, 0, 0, 0, 0, 0, 1},
            {4, 6, 0, 0, 0, 0, 3, 0, 0},
            {0, 0, 9, 5, 0, 7, 0, 0, 0},
            {0, 5, 0, 0, 0, 2, 0, 0, 3},
            {8, 0, 3, 0, 0, 0, 0, 7, 0}
        };
        int[][] hard2 = new int[][] {
            {9, 0, 0, 0, 7, 0, 0, 0, 0},
            {0, 2, 0, 6, 0, 8, 0, 0, 9},
            {0, 0, 0, 3, 0, 0, 0, 2, 0},
            {0, 1, 8, 0, 0, 0, 4, 7, 0},
            {0, 0, 0, 0, 4, 0, 6, 0, 0},
            {0, 5, 0, 0, 0, 3, 0, 0, 0},
            {0, 0, 0, 8, 0, 1, 0, 3, 0},
            {0, 0, 0, 0, 0, 0, 1, 5, 0},
            {5, 0, 0, 0, 3, 0, 9, 0, 0}
        };
        // takes a really long time to confirm it has no solutions, a problem in general
        int[][] anomaly = new int[][] {
            {0, 9, 0, 0, 0, 0, 0, 0, 0},
            {7, 0, 0, 0, 0, 4, 0, 0, 1},
            {3, 0, 0, 6, 0, 1, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 3, 6, 0},
            {0, 7, 0, 0, 4, 5, 1, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 7, 0},
            {0, 0, 1, 4, 0, 0, 0, 0, 0},
            {5, 0, 0, 1, 0, 0, 0, 0, 0}
        };
        Sudoku sudoku = new Sudoku(medium);
        int[][] answer = sudoku.solve();
        print(answer);
        System.out.println();
        System.out.println();

        int[][] genEasy = generateEasy();
        System.out.println("Easy");
        print(genEasy);
        System.out.println("Solution");
        print(new Sudoku(genEasy).solve(false));
        System.out.println();
        System.out.println();

        int[][] genMed = generateMedium();
        System.out.println("Medium");
        print(genMed);
        System.out.println("Solution");
        print(new Sudoku(genMed).solve(false));
        System.out.println();
        System.out.println();

        int[][] genHard = generateHard();
        System.out.println("Hard");
        print(genHard);
        System.out.println("Solution");
        print(new Sudoku(genHard).solve(false));
    }
}
