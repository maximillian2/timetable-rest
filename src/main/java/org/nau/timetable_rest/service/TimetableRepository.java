package org.nau.timetable_rest.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.nau.timetable_rest.domain.TimetableDay;
import org.nau.timetable_rest.domain.TimetableWeek;
import org.nau.timetable_rest.error.IllegalDayException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.IsoFields;
import java.util.ArrayList;
import java.util.List;

@Service
public class TimetableRepository {
  public TimetableRepository() {
  }

  private static int getCurrentWeek() {
    return ZonedDateTime.now(ZoneId.of("Europe/Kiev")).get(IsoFields.WEEK_OF_WEEK_BASED_YEAR) % 2 == 0 ? 1 : 2;
  }

  private static int getCurrentDay() {
    return LocalDate.now(ZoneId.of("Europe/Kiev")).getDayOfWeek().getValue();
  }

  public List<TimetableWeek> getCompleteTimetableWith(String group, int subgroup) {
    List<TimetableWeek> weeks = new ArrayList<>();

    try {
      ArrayList<String> pairs = new ArrayList<>();

      // todo change this to url based query not hardcoded
      Document doc = Jsoup.connect("http://rozklad.nau.edu.ua/timetable/group/%D0%9D%D0%9D%D0%86%D0%9A%D0%86%D0%A2%20" + group.replaceAll("[A-Za-z]", "") + "/" + subgroup).get();
      Element table = doc.select("table").get(0); // gets first table (hope the only one)
      Elements rows = table.select("tr");

      for (Element row : rows) {
        Elements columns = row.select("td");

        for (int j = 0; j < columns.size(); j++) {
          if (columns.get(j).text().matches("(Пн.|Вв.|Ср.|Чт.|Пт.|Сб.)")) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Day ").append(columns.get(j).text()).append("\n");
            for (int k = 0; k < 6; k++) {
              stringBuilder.append(k + 1).append(". ").append(columns.get(j + k + 1)).append("\n");
            }
            pairs.add(stringBuilder.toString());
          }
        }
      }

      List<TimetableDay> firstWeekdays = new ArrayList<>();
      for (int i = 0; i < pairs.size() / 2; i++) {
        firstWeekdays.add(new TimetableDay(pairs.get(i), 1, subgroup));
      }

      List<TimetableDay> secondWeekDays = new ArrayList<>();
      for (int i = pairs.size() / 2; i < pairs.size(); i++) {
        secondWeekDays.add(new TimetableDay(pairs.get(i), 2, subgroup));
      }

      weeks.add(new TimetableWeek(firstWeekdays));
      weeks.add(new TimetableWeek(secondWeekDays));
    } catch (IOException e) {
      e.printStackTrace();
    }
    return weeks;
  }

  private int checkWeek(String week) throws IllegalArgumentException {
    int weekNumber;

    if (week.matches("(\\b[1-2]\\b|current)")) {
      weekNumber = (week.equals("current")) ? getCurrentWeek() : Integer.parseInt(week);
    } else {
      throw new IllegalArgumentException();
    }

    return weekNumber;
  }

  private int checkDay(String day) throws IllegalDayException {
    int weekDay;
    if (day.matches("(\\b[1-6]\\b|today|tomorrow)\n")) {
      switch (day) {
        case "current":
          weekDay = getCurrentDay();
          break;
        case "tomorrow":
          if (getCurrentDay() + 1 > 6) { // saturday
            throw new IllegalDayException("Day is not in valid range (1-6): " + day + ".");
          }
          weekDay = getCurrentDay() + 1;
          break;
        default:
          weekDay = Integer.parseInt(day);
          break;
      }
    } else {
      throw new IllegalDayException("Day is not in valid format (1-6/today/tomorrow).");
    }

    return weekDay;
  }

  public TimetableWeek getWeekTimetableWith(String group, int subgroup, String week) {
    return getCompleteTimetableWith(group, subgroup).get(checkWeek(week) - 1);
  }

  public TimetableDay getDayTimetableWith(String group, int subgroup, String week, String day) {
    return getCompleteTimetableWith(group, subgroup).get(checkWeek(week) - 1).getDays().get(checkDay(day) - 1);
  }
}
