package code;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class D1 {
  private static final String INPUT_FILE_PATH = "./inputs/day1.txt";
  private static final String REGEX = "(\\d+)\\s*(\\d+)";

  private List<Integer> leftIds;
  private List<Integer> rightIds;

  public D1() {
    this.leftIds = new ArrayList<>();
    this.rightIds = new ArrayList<>();
    readInput();
  }

  public void runP1() {
    System.out.println("Day 1 Part 1 total distance: " + calculateDistance());
  }

  public void runP2() {
    System.out.println("Day 1 Part 2 imilarity score: " + calculateSimilarity());
  }

  private int calculateDistance() {
    leftIds.sort(null);
    rightIds.sort(null);

    int totalDistance = 0;
    for (int i = 0; i < leftIds.size(); i++) {
      totalDistance += Math.abs(leftIds.get(i) - rightIds.get(i));
    }

    return totalDistance;
  }

  private int calculateSimilarity() {
    Map<Integer, Integer> idFrequencies = new HashMap<>();
    for (Integer id : rightIds) {
      idFrequencies.put(id, idFrequencies.getOrDefault(id, 0) + 1);
    }

    int similarityScore = 0;
    for (Integer id : leftIds) {
      similarityScore += (id * idFrequencies.getOrDefault(id, 0));
    }
    return similarityScore;
  }

  private void readInput() {
		try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE_PATH))) {
      Pattern pattern = Pattern.compile(REGEX);
			String line;
			while ((line = br.readLine()) != null) {
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
          this.leftIds.add(Integer.parseInt(matcher.group(1)));
          this.rightIds.add(Integer.parseInt(matcher.group(2)));
        } else {
          System.out.println("Malformatted line found: " + line + ". Ignoring it.");
        }
			}
		} catch (IOException e) {
			System.out.println("Failed to read " + INPUT_FILE_PATH + " " + e);
		}
  }
}
