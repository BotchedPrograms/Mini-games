// Binary search algorithm
  // Taken from the "Another Iterative Approach to Binary Search" found here: https://www.geeksforgeeks.org/binary-search/

class GFG {

  static int binarySearch(int[] v, int To_Find)
  {
    int lo = 0, hi = v.length - 1;
    // This below check covers all cases , so need to check
    // for mid=lo-(hi-lo)/2
      // Edit: Changed > 1 to > 0 and made it return lo when not found, so it gives index of where number would be if it was in the list
      // So in {3, 7, 10, 11, 63}, 4 would return an index of 1, in between 3 and 7
      // Since game + digit has digits ranging from 1-9, one can find index of game + 0 to find first index
        // Such optimizations found and made not for TicTacToe bot, but for ConnectFour bot, which demands much more computation work

    while (hi - lo > 0) {
      int mid = (hi + lo) / 2;
      if (v[mid] < To_Find) {
        lo = mid + 1;
      }
      else {
        hi = mid;
      }
    }
    if (v[lo] == To_Find) {
      return lo;
    }
    else if (v[hi] == To_Find) {
      return hi;
    }
    else {
      return lo;
    }
  }

  public static void main(String[] args) {
    int[] arr = {3, 7, 10, 11, 63};
    for (int i = 0; i <= 20; i++) {
      System.out.println(i + " " + binarySearch(arr, i));
    }
  }
}
// contributed by akashish__
