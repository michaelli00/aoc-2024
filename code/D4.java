package code;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class D4 implements Day {
  private static final String INPUT_FILE_PATH = "./inputs/day4.txt";
  private static final String XMAS_STRING = "XMAS";
  private static final String MAS_STRING = "MAS";

  private List<List<Character>> characterMatrix;
  private Set<Coordinate> xCoordinates;     // Location of 'X' on the matrix
  private Set<Coordinate> aCoordinates;     // Location of 'A' on the matrix

  public D4() {
    characterMatrix = new ArrayList<>();
    xCoordinates = new HashSet<>();
    aCoordinates = new HashSet<>();
    readInput();
  }

  public void runP1() {
    System.out.println("Day 4 Part 1 number of XMAS: " + countXmas());
  }

  public void runP2() {
    System.out.println("Day 4 Part 2 number of X-MAS: " + countCrossMas());
  }

  private void readInput() {
    try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE_PATH))) {
      String line;

      // Create 2D matrix
      int i = 0;
      while ((line = br.readLine()) != null) {
        List<Character> row = new ArrayList<>();
        int j = 0;
        for (char c : line.toCharArray()) {
          if (c == 'X') this.xCoordinates.add(new Coordinate(i, j));
          if (c == 'A') this.aCoordinates.add(new Coordinate(i, j));
          row.add(c);
          j++;
        }
        i++;
        this.characterMatrix.add(row);
      }
    } catch (IOException e) {
      System.out.println("Failed to read " + INPUT_FILE_PATH + " " + e);
    }
  }

  private int countXmas() {
    int count = 0;
    for (Coordinate coordinate : this.xCoordinates) {
      for (Direction direction : Direction.values()) {
        if (attemptToMatchTargetString(coordinate, direction, 0, XMAS_STRING)) count++;
      }
    }
    return count;
  }

  private int countCrossMas() {
    int count = 0;
    for (Coordinate coordinate : this.aCoordinates) {
      int xCoord = coordinate.getXCoord();
      int yCoord = coordinate.getYCoord();

      // Trying to see if we can build "MAS" string in various directions
      boolean northEastMas = attemptToMatchTargetString(new Coordinate(xCoord + 1, yCoord - 1), Direction.NORTH_EAST, 0, MAS_STRING);
      boolean southWestMas = attemptToMatchTargetString(new Coordinate(xCoord - 1, yCoord + 1), Direction.SOUTH_WEST, 0, MAS_STRING);

      boolean southEastMas = attemptToMatchTargetString(new Coordinate(xCoord - 1, yCoord - 1), Direction.SOUTH_EAST, 0, MAS_STRING);
      boolean northWestMas = attemptToMatchTargetString(new Coordinate(xCoord + 1, yCoord + 1), Direction.NORTH_WEST, 0, MAS_STRING);

      if ((northEastMas || southWestMas) && (southEastMas || northWestMas)) count++;
    }
    return count;
  }

  // Attempts to match string in a given direction. Return true if the targetString is matched
  private boolean attemptToMatchTargetString(Coordinate coordinate, Direction direction, int targetStringIndex, String targetString) {
    if (targetStringIndex >= targetString.length()) return true;

    int xCoord = coordinate.getXCoord();
    int yCoord = coordinate.getYCoord();

    // Boundary checks
    if (xCoord < 0 || xCoord >= this.characterMatrix.size() || yCoord < 0 || yCoord >= this.characterMatrix.get(xCoord).size()) return false;
    if (this.characterMatrix.get(xCoord).get(yCoord) != targetString.charAt(targetStringIndex)) return false;

    int newTargetStringIndex = targetStringIndex + 1;
    Coordinate newCoordinate;
    switch (direction) {
      case NORTH:
        newCoordinate = new Coordinate(xCoord - 1, yCoord);
        break;
      case EAST:
        newCoordinate = new Coordinate(xCoord, yCoord + 1);
        break;
      case SOUTH:
        newCoordinate = new Coordinate(xCoord + 1, yCoord);
        break;
      case WEST:
        newCoordinate = new Coordinate(xCoord, yCoord - 1);
        break;
      case NORTH_EAST:
        newCoordinate = new Coordinate(xCoord - 1, yCoord + 1);
        break;
      case NORTH_WEST:
        newCoordinate = new Coordinate(xCoord - 1, yCoord - 1);
        break;
      case SOUTH_EAST:
        newCoordinate = new Coordinate(xCoord + 1, yCoord + 1);
        break;
      case SOUTH_WEST:
      default:
        newCoordinate = new Coordinate(xCoord + 1, yCoord - 1);
    }
    return attemptToMatchTargetString(newCoordinate, direction, newTargetStringIndex, targetString);
  }
}
