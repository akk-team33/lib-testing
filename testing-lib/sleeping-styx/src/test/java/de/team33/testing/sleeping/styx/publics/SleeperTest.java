package de.team33.testing.sleeping.styx.publics;

import de.team33.testing.sleeping.styx.Sleeper;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SleeperTest extends Sleeper {

    @ParameterizedTest
    @ValueSource(longs = {1, 2, 3, 5, 8, 13, 21, 34, 55, 89, 144, 233, 377, 610, 987})
    final void sleep_MILLIS(final long millis) {
        final long t0 = System.nanoTime();
        sleep(millis);
        final double duration = (System.nanoTime() - t0) / 1000000.0;
        assertEquals((double) millis, duration, 0.1);
    }

    @ParameterizedTest
    @ValueSource(longs = {1, 2, 3, 5, 8, 13, 21, 34, 55, 89, 144, 233, 377, 610, 987})
    final void sleep_MICROS(final long micros) {
        final long t0 = System.nanoTime();
        sleep(micros, TimeUnit.MICROSECONDS);
        final double duration = (System.nanoTime() - t0) / 1000.0;
        assertEquals((double) micros, duration, 100.0);
    }

    @ParameterizedTest
    @ValueSource(longs = {1, 2, 3, 5, 8, 13, 21, 34, 55, 89, 144, 233, 377, 610, 987})
    final void sleep_NANOS(final long nanos) {
        final long t0 = System.nanoTime();
        sleep(nanos, TimeUnit.NANOSECONDS);
        final double duration = (System.nanoTime() - t0);
        assertEquals((double) nanos, duration, 100000.0);
    }
}