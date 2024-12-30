package code;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class D2 {
  private static final String INPUT_FILE_PATH = "./inputs/day2.txt";
  private static final String REGEX = "\\s+";

  public void runP1() {
    System.out.println("Day 2 Part 1 number of safe reports: " + calculateNumberOfSafeReports(false));
  }

  public void runP2() {
    System.out.println("Day 2 Part 2 number of safe reports with Problem Dampener: " + calculateNumberOfSafeReports(true));
  }

  // Takes in boolean to determine if we should consider using Problem Dampener
  private int calculateNumberOfSafeReports(boolean withProblemDampener) {
    // We don't need to store data in a data structure so we can read from file and directly compute the results
    int numSafeReports = 0;
    try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE_PATH))) {
      Pattern pattern = Pattern.compile(REGEX);
      String line;
      while ((line = br.readLine()) != null) {
        int[] levels = Arrays.stream(line.split(REGEX)).mapToInt(Integer::parseInt).toArray();
        numSafeReports += processReport(levels, withProblemDampener);
      }
    } catch (IOException e) {
      System.out.println("Failed to read " + INPUT_FILE_PATH + " " + e);
    }
    return numSafeReports;
  }

  // Return 1 if safe report else return 0
  private int processReport(int[] levels, boolean withProblemDampener) {
    // We assume that a report of length zero is a safe report, vacuously
    if (levels.length < 2) {
      return 1;
    }

    if (withProblemDampener) {
      for (int i = -1; i < levels.length; i++) {
        if (1 == processReport(generateLevelsExcludingIndex(levels, i), false)) {
          return 1;
        }
      }
      return 0;
    }

    int slowPtr = 0;
    int fastPtr = 1;
    boolean isIncreasing = false;
    if (levels[fastPtr] > levels[slowPtr]) {
      isIncreasing = true;
    }

    while(fastPtr < levels.length) {
      int levelDifference = isIncreasing ? levels[fastPtr] - levels[slowPtr] : levels[slowPtr] - levels[fastPtr];
      if (levelDifference < 1 || levelDifference > 3) {
        return 0;
      }
      fastPtr++;
      slowPtr++;
    }
    return 1;
  }

	private int[] generateLevelsExcludingIndex(int[] levels, int index) {
		return IntStream.range(0, levels.length).filter(i -> i != index).map(i -> levels[i]).toArray();
	}
}
