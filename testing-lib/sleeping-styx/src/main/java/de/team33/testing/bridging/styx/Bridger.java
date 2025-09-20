package de.team33.testing.bridging.styx;

import java.util.concurrent.TimeUnit;

/**
 * Provides methods to bridge short periods of time during a test.
 * <p>
 * <b>Note</b>:
 * Should only be used, and with caution, when there is no other option than to bridge a certain period of time.
 * <ul>
 *     <li>Unlike Thread.sleep(), other threads are not given priority during the period.</li>
 *     <li>Like Thread.sleep(), it also increases the runtime of a test.</li>
 *     <li>While it adheres to the specified time periods more accurately than Thread.sleep(),
 *     it is not absolutely accurate.</li>
 *     <li>The accuracy ultimately depends on the capabilities of the executing system.</li>
 * </ul>
 */
public class Bridger {

    /**
     * Bridges at least the specified period of time in <em>milliseconds</em>.
     * The actually bridged period may be longer than specified, but exceeding it by more than 100 microseconds
     * should be extremely unlikely.
     * Ultimately, however, this depends on the executing machine and its current workload.
     * <p>
     * The current thread doesn't sleep during the period but remains busy.
     *
     * @return The actually bridged period of time in nanoseconds.
     */
    public final long bridge(final int milliseconds) {
        return bridge(milliseconds, TimeUnit.MILLISECONDS);
    }

    /**
     * Bridges at least the specified <em>period</em> of time in the given {@link TimeUnit}.
     * The actually bridged period may be longer than specified, but exceeding it by more than 100 microseconds
     * should be extremely unlikely.
     * Ultimately, however, this depends on the executing machine and its current workload.
     * <p>
     * The current thread doesn't sleep during the period but remains busy.
     *
     * @return The actually bridged period of time in nanoseconds.
     */
    public final long bridge(final int period, final TimeUnit unit) {
        return bridgeNanos(unit.toNanos(period));
    }

    private long bridgeNanos(final long period) {
        final long t0 = System.nanoTime();
        long dt = System.nanoTime() - t0;
        while (dt < period) {
            dt = System.nanoTime() - t0;
        }
        return dt;
    }
}
