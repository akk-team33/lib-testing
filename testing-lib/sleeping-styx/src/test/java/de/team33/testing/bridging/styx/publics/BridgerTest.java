package de.team33.testing.bridging.styx.publics;

import de.team33.testing.bridging.styx.Bridger;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BridgerTest extends Bridger {

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 5, 8, 13, 21, 34, 55, 89, 144, 233, 377, 610, 987})
    final void bridge_MILLIS(final int millis) {
        final double result = bridge(millis) / 1000000.0;
        assertEquals((double) millis, result, 0.1);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 5, 8, 13, 21, 34, 55, 89, 144, 233, 377, 610, 987})
    final void bridge_MICROS(final int micros) {
        final double result = bridge(micros, TimeUnit.MICROSECONDS) / 1000.0;
        assertEquals((double) micros, result, 100.0);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 5, 8, 13, 21, 34, 55, 89, 144, 233, 377, 610, 987})
    final void bridge_NANOS(final int nanos) {
        final double result = bridge(nanos, TimeUnit.NANOSECONDS);
        assertEquals((double) nanos, result, 100000.0);
    }
}