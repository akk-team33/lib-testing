package de.team33.test.testing.v1;

import de.team33.libs.testing.v1.Attempts;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class AttemptsTest {

    private static final int COUNT = 100;

    @Test
    public final void parallelSync() throws InterruptedException {
        final SynchInt counter = new SynchInt(0);
        Attempts.tryParallel(COUNT, () -> {
            counter.increment();
        });
        assertEquals(COUNT, counter.get());
    }

    @Test
    public final void parallelAsync() throws InterruptedException {
        final AsyncInt counter = new AsyncInt(0);
        Attempts.tryParallel(COUNT, () -> {
            counter.increment();
        });
        final String message = String.format("counter expected to be significant less than COUNT (%d) but was %d",
                                             COUNT, counter.get());
        assertTrue(message, COUNT > counter.get());
    }

    @Test(expected = IOException.class)
    public final void parallelFail() throws IOException {
        Attempts.tryParallel(COUNT, () -> {
            throw new IOException();
        });
        fail("Expected: IOException");
    }

    @Test
    public final void parallelNoFail() throws IOException {
        final List<Throwable> caught = Attempts.runParallel(COUNT, () -> {
            throw new IOException();
        }).getCaught();
        assertEquals(COUNT, caught.size());
        caught.forEach(x -> assertEquals(IOException.class, x.getClass()));
    }

    @Test
    public final void serialSync() throws InterruptedException {
        final SynchInt counter = new SynchInt(0);
        Attempts.trySerial(COUNT, () -> {
            counter.increment();
        });
        assertEquals(COUNT, counter.get());
    }

    @Test
    public final void serialAsync() throws InterruptedException {
        final AsyncInt counter = new AsyncInt(0);
        Attempts.trySerial(COUNT, () -> {
            counter.increment();
        });
        assertEquals(COUNT, counter.get());
    }

    @Test(expected = IOException.class)
    public final void serialFail() throws IOException {
        Attempts.trySerial(COUNT, () -> {
            throw new IOException();
        });
        fail("Expected: IOException");
    }

    @Test
    public final void serialNoFail() throws IOException {
        final List<Throwable> caught = Attempts.runSerial(COUNT, () -> {
            throw new IOException();
        }).getCaught();
        assertEquals(COUNT, caught.size());
        caught.forEach(x -> assertEquals(IOException.class, x.getClass()));
    }

    static final class AsyncInt {

        private int value;

        AsyncInt(final int value) {
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

    static final class SynchInt {

        private int value;

        SynchInt(final int value) {
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
