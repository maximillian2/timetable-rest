package org.nau.timetable_rest.error;

public class IllegalDayException extends RuntimeException {
  public IllegalDayException() {
    super();
  }

  public IllegalDayException(String message) {
    super(message);
  }

  public IllegalDayException(String message, Throwable cause) {
    super(message, cause);
  }

  public IllegalDayException(Throwable cause) {
    super(cause);
  }
}
