package de.team33.test.testing.v1;

import de.team33.libs.testing.v1.XRunnable;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.*;

public class RunnerTest {

    public static final int COUNT = 100;

    @Test
    public final void parallelAtomic() {
        final AtomicInteger counter = new AtomicInteger(0);
        parallel(COUNT, () -> {
            counter.incrementAndGet();
        });
        assertEquals(COUNT, counter.get());
    }

    @Test//(expected = AssertionError.class)
    public final void parallelWeak() throws InterruptedException {
        final WeakInteger counter = new WeakInteger(0);
        parallel(COUNT, () -> {
            counter.incrementAndGet();
        });
        final String message = String.format("counter expected to be significant less than COUNT (%d) but was %d",
                                             COUNT, counter.get());
        assertTrue(message, COUNT > counter.get());
    }

    @Test
    public final void sequentialAtomic() {
        final AtomicInteger counter = new AtomicInteger(0);
        sequential(COUNT, () -> {
            counter.incrementAndGet();
        });
        assertEquals(COUNT, counter.get());
    }

    @Test//(expected = AssertionError.class)
    public final void sequentialWeak() throws InterruptedException {
        final WeakInteger counter = new WeakInteger(0);
        sequential(COUNT, () -> {
            counter.incrementAndGet();
        });
        assertEquals(COUNT, counter.get());
    }

    private <X extends Exception> void sequential(final int count, final XRunnable<X> xRunnable) throws X {
        for (int i = 0; i < count; ++i) {
            xRunnable.run();
        }
    }

    private <X extends Exception> void parallel(final int count, final XRunnable<X> xRunnable) throws X {
        final Brailer<X> caught = new Brailer<>();
        final List<Thread> threads = Stream.generate(() -> new Thread(() -> {
            try {
                xRunnable.run();
            } catch (final Throwable x) {
                caught.add(x);
            }
        }))
                                           .limit(count)
                                           .collect(Collectors.toList());
        for (final Thread thread : threads) {
            thread.start();
        }
        for (final Thread thread : threads) {
            try {
                thread.join();
            } catch (final InterruptedException e) {
                caught.add(new RuntimeException(e.getMessage(), e));
            }
        }

        caught.reThrowIfPresent();
    }

    private static class Brailer<X extends Exception> {

        private final List<Throwable> head = new ArrayList<>(1);

        private synchronized void add(final Throwable caught) {
            if (0 < head.size()) {
                head.get(0).addSuppressed(caught);
            } else {
                head.add(caught);
            }
        }

        private void reThrowIfPresent() throws X {
            final Throwable result = head.size() == 0 ? null : head.get(0);
            if (result instanceof Error)
                throw (Error) result;
            if (result instanceof RuntimeException)
                throw (RuntimeException) result;
            if (null != result)
                //noinspection unchecked
                throw (X) result;
        }
    }

    static final class WeakInteger {

        private int value;

        WeakInteger(final int value) {
            this.value = value;
        }

        int incrementAndGet() throws InterruptedException {
            final int result = value + 1;
            Thread.sleep(1);
            value = result;
            return result;
        }

        int get() {
            return value;
        }
    }
}
