package code;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class D6 implements Day {
  private static final String INPUT_FILE_PATH = "./inputs/day6.txt";
  private static final char OBSTRUCTION_CHARACTER = '#';
  private static final char VISITED_CHARACTER = 'X';

  private List<List<Character>> grid;    // Only contains obstructions
  private Map<Coordinate, int[]> visitedCellsToDirections;
  private Coordinate officerStartingCoordinate;

  public D6() {
    this.grid = new ArrayList<>();
    readInput();
  }

  public void runP1() {
    System.out.println("Day 6 Part 1 number of distinct officer positions: " + countVisitedCells());
  }

  public void runP2() {
    System.out.println("Day 6 Part 2 number of possible obstruction placements: " + countPossibleObstructionPlacements());
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
          if (c == '^') this.officerStartingCoordinate = new Coordinate(i, j);
          row.add(c);
          j++;
        }
        i++;
        this.grid.add(row);
      }
    } catch (IOException e) {
      System.out.println("Failed to read " + INPUT_FILE_PATH + " " + e);
    }
  }

  // Return true if a cycle is detected, else false
  private boolean traverseGrid(Coordinate officerCoordinate, Direction officerDirection) {
    int xCoord = officerCoordinate.getXCoord();
    int yCoord = officerCoordinate.getYCoord();

    // Officer is out of grid we can return now
    if (xCoord < 0 || xCoord > this.grid.size() - 1 || yCoord < 0 || (yCoord > this.grid.get(xCoord).size() - 1)) return false;

    // Find next coordinate
    int nextXCoord = xCoord;
    int nextYCoord = yCoord;
    Direction newDirection = officerDirection;
    switch (officerDirection) {
      case NORTH:
        if (xCoord >= 1 && this.grid.get(xCoord - 1).get(yCoord) == OBSTRUCTION_CHARACTER) {
          newDirection = Direction.EAST;
        } else {
          nextXCoord = xCoord - 1;
        }
        break;
      case EAST:
        if ((yCoord < this.grid.get(xCoord).size() - 1) && this.grid.get(xCoord).get(yCoord + 1) == OBSTRUCTION_CHARACTER) {
          newDirection = Direction.SOUTH;
        } else {
          nextYCoord = yCoord + 1;
        }
        break;
      case SOUTH:
        if ((xCoord < this.grid.size() - 1) && this.grid.get(xCoord + 1).get(yCoord) == OBSTRUCTION_CHARACTER) {
          newDirection = Direction.WEST;
        } else {
          nextXCoord = xCoord + 1;
        }
        break;
      case WEST:
      default:
        if ((yCoord >= 1 && this.grid.get(xCoord).get(yCoord - 1) == OBSTRUCTION_CHARACTER)) {
          newDirection = Direction.NORTH;
        } else {
          nextYCoord = yCoord - 1;
        }
    }
    Coordinate newOfficerCoordinate = new Coordinate(nextXCoord, nextYCoord);

    if (!visitedCellsToDirections.containsKey(officerCoordinate)) {
      visitedCellsToDirections.put(officerCoordinate, new int[4]);
    }
    int[] directionsSeenAtCoordinate = visitedCellsToDirections.get(officerCoordinate);
    int directionOrdinal = officerDirection.ordinal();

    // We have already seen this path meaning we hit a loop.
    if (directionsSeenAtCoordinate[directionOrdinal] == 1) {
      return true;
    }

    directionsSeenAtCoordinate[directionOrdinal] = 1;
    return traverseGrid(newOfficerCoordinate, newDirection);
  }

  private int countVisitedCells() {
    this.visitedCellsToDirections = new HashMap<>();
    traverseGrid(this.officerStartingCoordinate, Direction.NORTH);
    return visitedCellsToDirections.size();
  }

  private int countPossibleObstructionPlacements() {
    int count = 0;

    // First traverse to get the cells the officer visited
    this.visitedCellsToDirections = new HashMap<>();
    traverseGrid(this.officerStartingCoordinate, Direction.NORTH);

    // Remove officer start position from visited cells because we can't place an obstruction in the officer starting position
    Set<Coordinate> visitedCoordinates = visitedCellsToDirections.keySet();
    visitedCoordinates.remove(this.officerStartingCoordinate);

    // For each visited cell, place an obstruction and try to traverse through it. The traverse algorithm has a loop detection check
    for (Coordinate coordinate : visitedCoordinates) {
      int xCoord = coordinate.getXCoord();
      int yCoord = coordinate.getYCoord();

      // Retrieve current cell value so we can put it back after temporarily putting an obstruction there
      char cellValueAtCoord = this.grid.get(xCoord).get(yCoord);
      this.grid.get(xCoord).set(yCoord, OBSTRUCTION_CHARACTER);
      this.visitedCellsToDirections = new HashMap<>();
      if (traverseGrid(this.officerStartingCoordinate, Direction.NORTH)) {
        count++;
      }
      this.grid.get(xCoord).set(yCoord, cellValueAtCoord);
    }

    return count;
  }
}
