package de.team33.libs.testing.v1;

import java.util.ArrayList;
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
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (final InterruptedException caught) {
                aggregator.add(ExType.ERROR, new Error(caught.getMessage(), caught));
            }
        }
        return this;
    }

    final <R> R mapCaught(final Function<Stream<Throwable>, R> method) {
        return aggregator.map(method);
    }

    private enum ExType {
        ERROR,
        UNCHECKED,
        CHECKED
    }
}
