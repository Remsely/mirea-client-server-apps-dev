package edu.mirea.remsely.csad.practice8.commons.races.exception;

public class RaceNotFoundException extends RuntimeException {
    public RaceNotFoundException(Long id) {
        super("Race with id " + id + " not found");
    }
}
