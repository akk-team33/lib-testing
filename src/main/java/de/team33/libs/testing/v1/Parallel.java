package de.team33.libs.testing.v1;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

class Parallel<X extends Exception> extends AttemptsBase<X> {

    private final List<Thread> threads;
    private final AtomicInteger index = new AtomicInteger(0);

    Parallel(final int count, final XConsumer<Integer, X> xConsumer) {
        this.threads = new ArrayList<>(count);
        for (int i = 0; i < count; ++i) {
            threads.add(new Thread(() -> {
                try {
                    xConsumer.accept(index.getAndIncrement());
                } catch (final Error caught) {
                    addCaught(Category.ERROR, caught);
                } catch (final RuntimeException caught) {
                    addCaught(Category.UNCHECKED, caught);
                } catch (final Throwable caught) {
                    // can only be of type <X> ...
                    addCaught(Category.CHECKED, caught);
                }
            }));
        }
    }

    final Parallel<X> run() {
        return startThreads().joinThreads();
    }

    private Parallel<X> startThreads() {
        for (Thread thread : threads) {
            thread.start();
        }
        return this;
    }

    private Parallel<X> joinThreads() {
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                addCaught(Category.ADDITIONAL, new JoinException(e));
            }
        }
        return this;
    }

    private static class JoinException extends RuntimeException {

        private JoinException(final InterruptedException cause) {
            super(cause.getMessage(), cause);
        }
    }
}
