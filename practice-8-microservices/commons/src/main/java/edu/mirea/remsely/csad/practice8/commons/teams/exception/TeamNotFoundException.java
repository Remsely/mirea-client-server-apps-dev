package edu.mirea.remsely.csad.practice8.commons.teams.exception;

public class TeamNotFoundException extends RuntimeException {
    public TeamNotFoundException(Long id) {
        super("Team with id " + id + " not found");
    }
}
