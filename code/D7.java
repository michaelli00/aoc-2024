package code;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class D7 implements Day {
  private static final String INPUT_FILE_PATH = "./inputs/day7.txt";
  private static final String EQUATION_REGEX = "(\\d+)\\: (.*)";
  private static final String OPERAND_DELIMITER_REGEX = " ";

  public void runP1() {
    System.out.println("Day 7 Part 1 total calibration result: " + calculateCalibration(false));
  }

  public void runP2() {
    System.out.println("Day 1 Part 2 Similarity score: " + calculateCalibration(true));
  }

  private BigInteger calculateCalibration(boolean includeConcat) {
    BigInteger sum = BigInteger.ZERO;
    try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE_PATH))) {
      Pattern pattern = Pattern.compile(EQUATION_REGEX);
      String line;
      while ((line = br.readLine()) != null) {
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
          BigInteger testValue = new BigInteger(matcher.group(1));
          String[] operands = matcher.group(2).split(OPERAND_DELIMITER_REGEX);
          if (operands.length == 0) continue;

          // Pull first operand and get subarray to pass into helper fxn
          BigInteger firstOperandValue = new BigInteger(operands[0]);
          String[] restOfOperands = Arrays.copyOfRange(operands, 1, operands.length);
          if (isValidCalibration(testValue, restOfOperands, firstOperandValue, includeConcat)) {
            sum = sum.add(testValue);
          }
        } else {
          System.out.println("Malformatted line found: " + line + ". Ignoring it.");
        }
      }
    } catch (IOException e) {
      System.out.println("Failed to read " + INPUT_FILE_PATH + " " + e);
    }
    return sum;
  }

  private boolean isValidCalibration(BigInteger testValue, String[] operands, BigInteger currentValue, boolean includeConcat) {
    if (operands.length == 0) {
      if (currentValue.compareTo(testValue) != 0) return false;
      return true;
    }
    BigInteger firstOperandValue = new BigInteger(operands[0]);
    BigInteger testMult = currentValue.multiply(firstOperandValue);
    BigInteger testAdd = currentValue.add(firstOperandValue);
    BigInteger testConcat = new BigInteger(currentValue.toString() + operands[0]);
    String[] restOfOperands = Arrays.copyOfRange(operands, 1, operands.length);
    return isValidCalibration(testValue, restOfOperands, testMult, includeConcat)
        || isValidCalibration(testValue, restOfOperands, testAdd, includeConcat)
        || (includeConcat && isValidCalibration(testValue, restOfOperands, testConcat, includeConcat));
  }
}
