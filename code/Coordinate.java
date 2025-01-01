package code;

public class Coordinate {
  private int xCoord;
  private int yCoord;

  public Coordinate(int x, int y) {
    this.xCoord = x;
    this.yCoord = y;
  }

  public int getXCoord() {
    return xCoord;
  }

  public int getYCoord() {
    return yCoord;
  }

  @Override
  public String toString() {
    return xCoord + " " + yCoord;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null) return false;
    if (o.getClass() != this.getClass()) return false;
    final Coordinate coord = (Coordinate) o;
    return (this.xCoord == coord.getXCoord()) && (this.yCoord == coord.getYCoord());
  }

  @Override
  public int hashCode() {
    return this.toString().hashCode();
  }
}
