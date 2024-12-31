package code;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class D3 implements Day {
  private static final String INPUT_FILE_PATH = "./inputs/day3.txt";

  // Index 1: Entire mul(d,d) String
  // Index 2: First number in mul String
  // Index 3: Second number in mul String
  // Index 4: Don't() String
  // Index 5: Do() String
  private static final String DO_DONT_MULTPLICATION_REGEX = "(mul\\((\\d+),(\\d+)\\))|(don't\\(\\))|(do\\(\\))";

  // Index 1: Entire mul(d,d) String
  // Index 2: First number in mul String
  // Index 3: Second number in mul String
  private static final String MULTIPLICATION_REGEX = "(mul\\((\\d+),(\\d+)\\))";

  public void runP1() {
    System.out.println("Day 3 Part 1 sum of valid multiplications: " + calculateSumOfValidMultiplications(false));
  }

  public void runP2() {
    System.out.println("Day 3 Part 2 sum of valid do-don't multiplications: " + calculateSumOfValidMultiplications(true));
  }

  private int calculateSumOfValidMultiplications(boolean canDisable) {
    // We don't need to store data in a data structure so we can read from file and directly compute the results
    boolean multIsEnabled = true;
    int sum = 0;
    try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE_PATH))) {
      String regex = canDisable ? DO_DONT_MULTPLICATION_REGEX : MULTIPLICATION_REGEX;
      Pattern pattern = Pattern.compile(regex);
      String line;
      while ((line = br.readLine()) != null) {
        Matcher matcher = pattern.matcher(line);
        while (matcher.find()) {
          if (matcher.group(1) != null && multIsEnabled) {
            sum += Integer.parseInt(matcher.group(2)) * Integer.parseInt(matcher.group(3));
          } else if (matcher.group(4) != null) {
            multIsEnabled = false;
          } else if (matcher.group(5) != null) {
            multIsEnabled = true;
          }
        }
      }
    } catch (IOException e) {
      System.out.println("Failed to read " + INPUT_FILE_PATH + " " + e);
    }
    return sum;
  }
}
