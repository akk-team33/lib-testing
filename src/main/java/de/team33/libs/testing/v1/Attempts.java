package de.team33.libs.testing.v1;

import java.util.List;

/**
 * Abstracts a tool for performing a certain operation repeatedly. Both parallel and serial execution are possible.
 *
 * @param <X> A type of (checked) exception that may occur while performing the operation.
 */
public interface Attempts<X extends Exception> {

    /**
     * Repeatedly performs a specific operation in parallel threads.
     *
     * @param count     the number of parallel threads
     * @param xRunnable the operation
     * @param <X>       A type of (checked) exception that may occur while performing the operation.
     * @throws Error            if an Error occurred while performing the operation.
     * @throws RuntimeException if a RuntimeException occurred while performing the operation.
     * @throws X                if an Exception of type {@code <X>} occurred while performing the operation.
     * @see #trySerial(int, XRunnable)
     * @see #tryParallel(int, XConsumer)
     * @see #runParallel(int, XRunnable)
     */
    static <X extends Exception> void tryParallel(final int count, final XRunnable<X> xRunnable) throws X {
        runParallel(count, xRunnable).reThrowCaught();
    }

    /**
     * Repeatedly performs a specific operation in parallel threads.
     * In this variant, the operation receives a unique consecutive number as a parameter
     *
     * @param count     the number of parallel threads
     * @param xConsumer the operation
     * @param <X>       A type of (checked) exception that may occur while performing the operation.
     * @throws Error            if an Error occurred while performing the operation.
     * @throws RuntimeException if a RuntimeException occurred while performing the operation.
     * @throws X                if an Exception of type {@code <X>} occurred while performing the operation.
     * @see #trySerial(int, XConsumer)
     * @see #tryParallel(int, XRunnable)
     * @see #runParallel(int, XConsumer)
     */
    static <X extends Exception> void tryParallel(final int count, final XConsumer<Integer, X> xConsumer) throws X {
        runParallel(count, xConsumer).reThrowCaught();
    }

    /**
     * Repeatedly performs a specific operation in parallel threads.
     *
     * @param count     the number of parallel threads
     * @param xRunnable the operation
     * @param <X>       A type of (checked) exception that may occur while performing the operation.
     * @return An instance of {@link Attempts}.
     * This contains the exceptions that actually occurred during the execution of the operation.
     * @see #runSerial(int, XRunnable)
     * @see #runParallel(int, XConsumer)
     * @see #tryParallel(int, XRunnable)
     */
    static <X extends Exception> Attempts<X> runParallel(final int count, final XRunnable<X> xRunnable) {
        return runParallel(count, i -> xRunnable.run());
    }

    /**
     * Repeatedly performs a specific operation in parallel threads.
     * In this variant, the operation receives a unique consecutive number as a parameter
     *
     * @param count     the number of parallel threads
     * @param xConsumer the operation
     * @param <X>       A type of (checked) exception that may occur while performing the operation.
     * @return An instance of {@link Attempts}.
     * This contains the exceptions that actually occurred during the execution of the operation.
     * @see #runSerial(int, XConsumer)
     * @see #runParallel(int, XRunnable)
     * @see #tryParallel(int, XConsumer)
     */
    static <X extends Exception> Attempts<X> runParallel(final int count, final XConsumer<Integer, X> xConsumer) {
        return new Parallel<>(count, xConsumer).run();
    }

    /**
     * Repeatedly performs a specific operation in the current thread.
     *
     * @param count     the number of parallel threads
     * @param xRunnable the operation
     * @param <X>       A type of (checked) exception that may occur while performing the operation.
     * @throws Error            if an Error occurred while performing the operation.
     * @throws RuntimeException if a RuntimeException occurred while performing the operation.
     * @throws X                if an Exception of type {@code <X>} occurred while performing the operation.
     * @see #tryParallel(int, XRunnable)
     * @see #trySerial(int, XConsumer)
     * @see #runSerial(int, XRunnable)
     */
    static <X extends Exception> void trySerial(final int count, final XRunnable<X> xRunnable) throws X {
        runSerial(count, xRunnable).reThrowCaught();
    }

    /**
     * Repeatedly performs a specific operation in the current thread.
     * In this variant, the operation receives a unique consecutive number as a parameter
     *
     * @param count     the number of parallel threads
     * @param xConsumer the operation
     * @param <X>       A type of (checked) exception that may occur while performing the operation.
     * @throws Error            if an Error occurred while performing the operation.
     * @throws RuntimeException if a RuntimeException occurred while performing the operation.
     * @throws X                if an Exception of type {@code <X>} occurred while performing the operation.
     * @see #tryParallel(int, XRunnable)
     * @see #trySerial(int, XRunnable)
     * @see #runSerial(int, XConsumer)
     */
    static <X extends Exception> void trySerial(final int count, final XConsumer<Integer, X> xConsumer) throws X {
        runSerial(count, xConsumer).reThrowCaught();
    }

    /**
     * Repeatedly performs a specific operation in the current thread.
     * In this variant, the operation receives a unique consecutive number as a parameter
     *
     * @param count     the number of parallel threads
     * @param xRunnable the operation
     * @param <X>       A type of (checked) exception that may occur while performing the operation.
     * @return An instance of {@link Attempts}.
     * This contains the exceptions that actually occurred during the execution of the operation.
     * @see #runParallel(int, XRunnable)
     * @see #runSerial(int, XConsumer)
     * @see #trySerial(int, XRunnable)
     */
    static <X extends Exception> Attempts<X> runSerial(final int count, final XRunnable<X> xRunnable) {
        return runSerial(count, i -> xRunnable.run());
    }

    /**
     * Repeatedly performs a specific operation in the current thread.
     * In this variant, the operation receives a unique consecutive number as a parameter
     *
     * @param count     the number of parallel threads
     * @param xConsumer the operation
     * @param <X>       A type of (checked) exception that may occur while performing the operation.
     * @return An instance of {@link Attempts}.
     * This contains the exceptions that actually occurred during the execution of the operation.
     * @see #runParallel(int, XConsumer)
     * @see #runSerial(int, XRunnable)
     * @see #trySerial(int, XConsumer)
     */
    static <X extends Exception> Attempts<X> runSerial(final int count, final XConsumer<Integer, X> xConsumer) {
        return new Serial<>(count, xConsumer).run();
    }

    /**
     * Re-throws one of the exceptions that may have occurred while executing the associated operation.
     *
     * @throws Error            if an Error occurred while performing the operation.
     * @throws RuntimeException if a RuntimeException occurred while performing the operation.
     * @throws X                if an Exception of type {@code <X>} occurred while performing the operation.
     */
    void reThrowCaught() throws X;

    /**
     * Returns any exceptions that might have occurred while performing the associated operation.
     */
    List<Throwable> getCaught();
}
