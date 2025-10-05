package edu.mirea.remsely.csad.semester7.practice3.task1;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class SensorsAlarmService {
    private static final int TEMP_THRESHOLD = 25;
    private static final int CO2_THRESHOLD = 70;

    public static void main(String[] args) {
        Random random = new Random();

        Observable<Integer> temperatureStream = Observable.interval(0, 1, TimeUnit.SECONDS)
                .map(tick -> 15 + random.nextInt(16))
                .doOnNext(v -> System.out.printf("[Sensor:Temp] %d%n", v))
                .subscribeOn(Schedulers.computation());

        Observable<Integer> co2Stream = Observable.interval(0, 1, TimeUnit.SECONDS)
                .map(tick -> 30 + random.nextInt(71))
                .doOnNext(v -> System.out.printf("[Sensor:CO2] %d%n", v))
                .subscribeOn(Schedulers.computation());

        Observable<String> alarmStream = Observable.combineLatest(
                temperatureStream,
                co2Stream,
                (temp, co2) -> {
                    if (temp > TEMP_THRESHOLD && co2 > CO2_THRESHOLD) {
                        return String.format("ALARM: Temp=%d CO2=%d", temp, co2);
                    } else if (temp > TEMP_THRESHOLD) {
                        return String.format("WARNING: Temperature exceeded (%d)", temp);
                    } else if (co2 > CO2_THRESHOLD) {
                        return String.format("WARNING: CO2 exceeded (%d)", co2);
                    } else {
                        return String.format("OK: Temp=%d CO2=%d", temp, co2);
                    }
                }
        );

        Disposable d = alarmStream.observeOn(Schedulers.computation())
                .subscribe(
                        msg -> System.out.println("[Alarm] " + msg),
                        err -> System.err.println("[Alarm] Error: " + err),
                        () -> System.out.println("[Alarm] Completed")
                );

        try {
            Thread.sleep(10000L);
        } catch (InterruptedException e) {
            System.err.println("Process interrupted");
        } finally {
            d.dispose();
            System.out.println("Process stopped (disposed)");
        }
    }
}
