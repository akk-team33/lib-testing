package de.team33.libs.testing.v1;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

class Threads {

    private final List<Thread> threads;
    private final Aggregator<ExType, Throwable> aggregator = new Aggregator<>(Enum::compareTo);

    Threads(final int count, final XRunnable<?> xRunnable) {
        this.threads = new ArrayList<>(count);
        for (int i = 0; i < count; ++i) {
            threads.add(new Thread(() -> {
                try {
                    xRunnable.run();
                } catch (final Error caught) {
                    aggregator.add(ExType.ERROR, caught);
                } catch (final RuntimeException caught) {
                    aggregator.add(ExType.UNCHECKED, caught);
                } catch (final Throwable caught) {
                    aggregator.add(ExType.CHECKED, caught);
                }
            }));
        }
    }

    final Threads start() {
        for (Thread thread : threads) {
            thread.start();
        }
        return this;
    }

    final Threads join() {
        final List<Throwable> caughtList = new LinkedList<>();
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (final InterruptedException caught) {
                caughtList.add(caught);
            }
        }
        if (0 < caughtList.size()) {
            final JoinError error = new JoinError(caughtList);
            throw aggregator.map(stream -> {
                stream.forEach(error::addSuppressed);
                return error;
            });
        }
        return this;
    }

    final <R> R mapCaught(final Function<Stream<Throwable>, R> method) {
        return aggregator.map(method);
    }

    private static class JoinError extends Error {

        private JoinError(final List<Throwable> caughtList) {
            super("Unexpected: " + caughtList.get(0).getMessage());
            caughtList.forEach(this::addSuppressed);
        }
    }

    private enum ExType {
        ERROR,
        UNCHECKED,
        CHECKED
    }
}
