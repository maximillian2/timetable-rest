package org.nau.timetable_rest.domain;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
public class TimetableDay {
  private String day;
  private int week;
  private int subgroup;
  private List<UniversityClass> classes = new ArrayList<>();

  public TimetableDay(String input, int week, int subgroup) {
    this.week = week;
    this.subgroup = subgroup;
    day = StringUtils.substringBetween(input,"Day", ".").trim();

    String[] strings = input.split("\n");
//    System.out.println("Timetable day: \n" + Arrays.toString(strings));
    for (int i = 1; i < strings.length; i++) {
      classes.add(new UniversityClass(strings[i]));
    }
  }
}
