package org.nau.timetable_rest.domain;

import lombok.Data;

import java.util.List;

@Data
public class TimetableWeek {
  private List<TimetableDay> days;

  public TimetableWeek(List<TimetableDay> days) {
    this.days = days;
  }
}
