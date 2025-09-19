package de.team33.testing.sleeping.styx;

import java.util.concurrent.TimeUnit;

/**
 * Provides an alternative to Thread.sleep() for bridging definable periods of time during a test.
 * <p>
 * <b>Note</b>:
 * Should only be used with caution when there is no other option than to wait for a defined period of time.
 * <ul>
 *     <li>While it adheres to the specified time periods more accurately than Thread.sleep(),
 *     it is not absolutely accurate.</li>
 *     <li>Like Thread.sleep(), it also increases the runtime of a test.</li>
 *     <li>Unlike Thread.sleep(), other threads are not given priority during the period.</li>
 * </ul>
 */
public class Sleeper {

    /**
     * Bridges at least the specified time in <em>milliseconds</em>.
     * The bridged time can be longer than specified, but exceeding it by more than 100 microseconds is extremely
     * unlikely. Ultimately, however, this depends on the executing machine and its current workload.
     * <p>
     * The current thread actually doesn't sleep during the period but remains active.
     */
    public final void sleep(final long milliseconds) {
        sleep(milliseconds, TimeUnit.MILLISECONDS);
    }

    /**
     * Bridges at least the specified <em>time</em> in the given {@link TimeUnit}.
     * The bridged time can be longer than specified, but exceeding it by more than 100 microseconds is extremely
     * unlikely. Ultimately, however, this depends on the executing machine and its current workload.
     * <p>
     * The current thread actually doesn't sleep during the period but remains active.
     */
    public final void sleep(final long time, final TimeUnit unit) {
        final long t0 = System.nanoTime();
        final long target = unit.toNanos(time);
        for (long dt = System.nanoTime() - t0; dt < target; dt = System.nanoTime() - t0) {
            // nothing to do here!
        }
    }
}
