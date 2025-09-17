package de.team33.testing.sleeping.styx.publics;

import de.team33.testing.sleeping.styx.Sleeper;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class SleeperTest extends Sleeper {

    @Test
    final void sleep_MILLIS() {
        final long t0 = System.currentTimeMillis();
        sleep(17);
        final double result = System.currentTimeMillis() - t0;
        assertEquals(17.0, result, 1.0);
    }

    @Test
    final void sleep_NANOS() {
        final long t0 = System.nanoTime();
        sleep(29, TimeUnit.NANOSECONDS);
        final double result = System.nanoTime() - t0;
        assertEquals(29.0, result, 1.0);
    }
}