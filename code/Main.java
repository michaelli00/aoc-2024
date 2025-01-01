package code;

import java.util.ArrayList;
import java.util.List;

public class Main {
  public static void main(String[] args) {
    List<Day> days = new ArrayList<>();
    days.add(new D1());
    days.add(new D2());
    days.add(new D3());
    days.add(new D4());
    days.add(new D5());
    days.add(new D6());

    for (Day day : days) {
      day.runP1();
      day.runP2();
    }
  }
}
