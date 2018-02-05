package org.nau.timetable_rest.domain;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

@Data
class UniversityClass {
  private int number;
  private String name;
  private String location;
  private String lecturer;
  private boolean isLecture = false; // todo me

  UniversityClass(String input) {
    System.out.println("University class input: " + input);
    try {
      number = Integer.parseInt(input.split("")[0]);
      String[] brokenParts = input.split("<br />");
      if (brokenParts.length > 1) {
        System.out.println("Broken parts: " + Arrays.toString(brokenParts));
        if (brokenParts[0].contains("lecture")) {
          isLecture = true;
        }

        name = StringUtils.substringBetween(brokenParts[0], "<b>", "</b>");
        lecturer = brokenParts[1].trim();
        location = StringUtils.removeAll(brokenParts[2] + brokenParts[3], "<.*?>").trim();
      }
    } catch (NumberFormatException e) {
      number = -1;
    }
  }
}
