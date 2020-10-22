package de.team33.libs.testing.v1;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Runner<X extends Exception> {

    private final int count;
    private final XRunnable<X> xRunnable;

    private List<Throwable> caughtList = Collections.emptyList();

    private Runner(final int count, final XRunnable<X> xRunnable) {
        this.count = count;
        this.xRunnable = xRunnable;
    }

    public static <X extends Exception> Runner parallel(final int count, final XRunnable<X> xRunnable) throws X {
        return new Runner<>(count, xRunnable).runParallel()
                                             .reThrowCaughtIfPresent();
    }

    public static <X extends Exception> Runner sequential(final int count, final XRunnable<X> xRunnable) throws X {
        return new Runner<>(count, xRunnable).runSequential()
                                             .reThrowCaughtIfPresent();
    }

    private Runner<X> runSequential() throws X {
        caughtList = new LinkedList<>();
        for (int i = 0; i < count; ++i) {
            try {
                xRunnable.run();
            } catch (final Throwable caught) {
                caughtList.add(caught);
            }
        }
        return this;
    }

    private Runner<X> runParallel() {
        final Threads threads = new Threads(count, xRunnable);
        this.caughtList = threads.start()
                                 .join()
                                 .mapCaught(stream -> stream.collect(Collectors.toList()));
        return this;
    }

    private Runner<X> reThrowCaughtIfPresent() throws X {
        final Throwable caught = caughtList.stream().reduce(Runner::addSuppressed).orElse(null);
        if (caught instanceof Error) {
            throw (Error)caught;
        } else if (caught instanceof RuntimeException) {
            throw (RuntimeException)caught;
        } else if (caught != null) {
            // a checked exception must be of type <X> ...
            // noinspection unchecked
            throw (X)caught;
        }
        return this;
    }

    private static Throwable addSuppressed(final Throwable main, final Throwable suppressed) {
        main.addSuppressed(suppressed);
        return main;
    }
}
