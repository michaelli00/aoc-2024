package code;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class D5 implements Day {
  private static final String INPUT_FILE_PATH = "./inputs/day5.txt";
  private static final String PAGE_ORDERING_REGEX = "((\\d+)\\|(\\d+))";
  private static final String PAGES_REGEX = "((\\d+,{0,1})*)";
  private static final String COMMA_REGEX = ",";

  private Map<Integer, Set<Integer>> pageOrderings;

  public D5() {
    pageOrderings = new HashMap<>();
  }

  public void runP1() {
    System.out.println("Day 5 Part 1 sum of valid middle page numbers: " + sumOfValidOrInvalidPageNumbers(true));
  }

  public void runP2() {
    System.out.println("Day 5 Part 2 sum of invalid middle page numbers: " + sumOfValidOrInvalidPageNumbers(false));
  }

  private int sumOfValidOrInvalidPageNumbers(boolean useValidPages) {
    int sum = 0;
    try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE_PATH))) {
      Pattern pageOrderingPattern = Pattern.compile(PAGE_ORDERING_REGEX);
      Pattern pagesPattern = Pattern.compile(PAGES_REGEX);
      String line;
      while ((line = br.readLine()) != null) {
        Matcher pageOrderingMatcher = pageOrderingPattern.matcher(line);
        Matcher pagesMatcher = pagesPattern.matcher(line);

        // Ignore empty lines
        if ("".equals(line)) {
          continue;

        // Populate page orderings
        } else if (pageOrderingMatcher.find()) {
          int firstPageNumber = Integer.parseInt(pageOrderingMatcher.group(2));
          int secondPageNumber = Integer.parseInt(pageOrderingMatcher.group(3));
          if (!this.pageOrderings.containsKey(firstPageNumber)) {
            this.pageOrderings.put(firstPageNumber, new HashSet<Integer>());
          }
          this.pageOrderings.get(firstPageNumber).add(secondPageNumber);

        // Process line
        } else if (pagesMatcher.find()) {
          Integer[] pages = Arrays.stream(line.split(COMMA_REGEX)).map(Integer::parseInt).toArray(Integer[]::new);
          sum += useValidPages ? checkIfListOfPagesIsValid(pages) : checkIfListOfPagesIsInvalid(pages);
        } else {
          System.out.println("Malformatted line found: " + line + ". Ignoring it.");
        }
      }
    } catch (IOException e) {
      System.out.println("Failed to read " + INPUT_FILE_PATH + " " + e);
    }
    return sum;
  }

  // Check if list of numbers if valid. If it is, it returns the middle page
  private int checkIfListOfPagesIsValid(Integer[] pages) {
    // Traverse through each line backwards and populate a blocklist of pages we don't allow based on pageOrderings. If we see an illegal page, we know it's an invalid list of pages
    Set<Integer> blocklist = new HashSet<>();
    for (int i = pages.length - 1; i >= 0; i--) {
      int page = pages[i];
      if (blocklist.contains(page)) return 0;
      blocklist.addAll(this.pageOrderings.get(page));
    }
    return pages[pages.length / 2];
  }

  // Check if list of numbers if invalid. If it is, make it valid and then return the middle number. Otherwise, return 0.
  private int checkIfListOfPagesIsInvalid(Integer[] pages) {
    // Traverse through each line backwards and populate a blocklist of pages we don't allow based on pageOrderings. If we see an illegal page, we know it's an invalid list of pages
    Set<Integer> blocklist = new HashSet<>();
    for (int i = pages.length - 1; i >= 0; i--) {
      int page = pages[i];
      if (blocklist.contains(page)) {
        Arrays.sort(pages, new Comparator<Integer>() {
          @Override
          public int compare(Integer a, Integer b) {
            if (pageOrderings.containsKey(a) && pageOrderings.get(a).contains(b)) return 1;
            if (pageOrderings.containsKey(b) && pageOrderings.get(b).contains(a)) return -1;
            return 0;
          }
        });
        return pages[pages.length / 2];
      }
      blocklist.addAll(this.pageOrderings.get(page));
    }
    return 0;
  }
}
