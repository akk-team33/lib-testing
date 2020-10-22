package de.team33.test.testing.v1;

import org.junit.Test;

import java.io.IOException;

import static de.team33.libs.testing.v1.Runner.parallel;
import static de.team33.libs.testing.v1.Runner.sequential;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class RunnerTest {

    public static final int COUNT = 100;

    @Test
    public final void parallelSync() throws InterruptedException {
        final Synchronous counter = new Synchronous(0);
        parallel(COUNT, () -> {
            counter.increment();
        });
        assertEquals(COUNT, counter.get());
    }

    @Test
    public final void parallelAsync() throws InterruptedException {
        final Asynchronous counter = new Asynchronous(0);
        parallel(COUNT, () -> {
            counter.increment();
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
    public final void sequentialSync() throws InterruptedException {
        final Synchronous counter = new Synchronous(0);
        sequential(COUNT, () -> {
            counter.increment();
        });
        assertEquals(COUNT, counter.get());
    }

    @Test
    public final void sequentialAsync() throws InterruptedException {
        final Asynchronous counter = new Asynchronous(0);
        sequential(COUNT, () -> {
            counter.increment();
        });
        assertEquals(COUNT, counter.get());
    }

    @Test(expected = IOException.class)
    public final void sequentialFail() throws IOException {
        sequential(COUNT, () -> {
            throw new IOException();
        });
        fail("Expected: IOException");
    }

    static final class Asynchronous {

        private int value;

        Asynchronous(final int value) {
            this.value = value;
        }

        void increment() throws InterruptedException {
            final int result = value + 1;
            Thread.sleep(1);
            value = result;
        }

        int get() {
            return value;
        }
    }

    static final class Synchronous {

        private int value;

        Synchronous(final int value) {
            this.value = value;
        }

        synchronized void increment() throws InterruptedException {
            final int result = value + 1;
            Thread.sleep(1);
            value = result;
        }

        int get() {
            return value;
        }
    }
}
