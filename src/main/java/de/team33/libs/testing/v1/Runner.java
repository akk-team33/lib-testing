package de.team33.libs.testing.v1;

import java.util.Collections;
import java.util.List;

/**
 * @deprecated use {@link Attempts} instead
 */
@Deprecated
public class Runner<X extends Exception> {

    private final int count;
    private final XRunnable<X> xRunnable;

    private Runner(final int count, final XRunnable<X> xRunnable) {
        this.count = count;
        this.xRunnable = xRunnable;
    }

    /**
     * @deprecated use {@link Attempts#tryParallel(int, XRunnable)} instead
     */
    @Deprecated
    public static <X extends Exception> Runner parallel(final int count, final XRunnable<X> xRunnable) throws X {
        return new Runner<>(count, xRunnable).runParallel();
    }

    /**
     * @deprecated use {@link Attempts#trySerial(int, XRunnable)} instead
     */
    @Deprecated
    public static <X extends Exception> Runner sequential(final int count, final XRunnable<X> xRunnable) throws X {
        return new Runner<>(count, xRunnable).runSequential();
    }

    private Runner<X> runSequential() throws X {
        Attempts.trySerial(count, xRunnable);
        return this;
    }

    private Runner<X> runParallel() throws X {
        Attempts.tryParallel(count, xRunnable);
        return this;
    }
}
