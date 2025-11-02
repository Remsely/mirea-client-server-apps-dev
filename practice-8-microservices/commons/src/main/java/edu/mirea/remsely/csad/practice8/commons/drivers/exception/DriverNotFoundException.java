package edu.mirea.remsely.csad.practice8.commons.drivers.exception;

public class DriverNotFoundException extends RuntimeException {
    public DriverNotFoundException(Long id) {
        super("Driver with id " + id + " not found");
    }
}
