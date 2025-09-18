package de.team33.testing.sleeping.styx;

import java.util.concurrent.TimeUnit;

public class Sleeper {

    public final void sleep(final long milliseconds) {
        sleep(milliseconds, TimeUnit.MILLISECONDS);
    }

    public final void sleep(final long time, final TimeUnit unit) {
        final long t0 = System.nanoTime();
        final long target = unit.toNanos(time);
        for (long dt = System.nanoTime() - t0; dt < target; dt = System.nanoTime() - t0) {
            // nothing to do here!
        }
    }
}
