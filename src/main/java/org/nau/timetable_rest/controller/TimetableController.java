package org.nau.timetable_rest.controller;

import org.nau.timetable_rest.service.TimetableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/nau/timetable")
public class TimetableController {

  private final TimetableRepository repository;

  @Autowired
  public TimetableController(TimetableRepository repository) {
    this.repository = repository;
  }

  @RequestMapping("/groups/{group}/subgroups/{subgroup}")
  public ResponseEntity<Object> getSubgroupCompleteTimetable(@PathVariable String group,
                                                             @PathVariable int subgroup) {
    return new ResponseEntity<>(repository.getCompleteTimetableWith(group, subgroup), HttpStatus.ACCEPTED);
    // todo check if resulted if not - exception
  }

  @RequestMapping("/groups/{group}/subgroups/{subgroup}/weeks/{week}")
  public ResponseEntity<Object> getSubgroupWeekTimetable(@PathVariable String group,
                                                         @PathVariable int subgroup,
                                                         @PathVariable String week) {
    return new ResponseEntity<>(repository.getWeekTimetableWith(group, subgroup, week), HttpStatus.ACCEPTED);
  }

  @RequestMapping("/groups/{group}/subgroups/{subgroup}/weeks/{week:([1-2]|current)}/days/{day}")
  public ResponseEntity<Object> getSubgroupDayTimetable(@PathVariable String group,
                                                        @PathVariable int subgroup,
                                                        @PathVariable String week,
                                                        @PathVariable String day) {
    return new ResponseEntity<>(repository.getDayTimetableWith(group, subgroup, week, day), HttpStatus.ACCEPTED);
  }
}
