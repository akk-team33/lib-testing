package de.team33.test.testing.v1;

import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import static de.team33.libs.testing.v1.Runner.parallel;
import static de.team33.libs.testing.v1.Runner.sequential;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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

    @Test
    public final void parallelWeak() throws InterruptedException {
        final WeakInteger counter = new WeakInteger(0);
        parallel(COUNT, () -> {
            counter.incrementAndGet();
        });
        final String message = String.format("counter expected to be significant less than COUNT (%d) but was %d",
                                             COUNT, counter.get());
        assertTrue(message, COUNT > counter.get());
    }

    @Test(expected = IOException.class)
    public final void parallelFail() throws IOException {
        parallel(COUNT, () -> {
            throw new IOException();
        });
        fail("Expected: IOException");
    }

    @Test
    public final void sequentialAtomic() {
        final AtomicInteger counter = new AtomicInteger(0);
        sequential(COUNT, () -> {
            counter.incrementAndGet();
        });
        assertEquals(COUNT, counter.get());
    }

    @Test
    public final void sequentialWeak() throws InterruptedException {
        final WeakInteger counter = new WeakInteger(0);
        sequential(COUNT, () -> {
            counter.incrementAndGet();
        });
        assertEquals(COUNT, counter.get());
    }

    @Test//(expected = IOException.class)
    public final void sequentialFail() throws IOException {
        sequential(COUNT, () -> {
            throw new IOException();
        });
        fail("Expected: IOException");
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
