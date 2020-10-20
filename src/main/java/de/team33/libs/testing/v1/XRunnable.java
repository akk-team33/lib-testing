package de.team33.libs.testing.v1;

/**
 * A kind of runnable that allows to throw a checked exception.
 *
 * @param <X> the type of exception that may be thrown by this runnable's method
 *
 * @see Runnable
 */
@FunctionalInterface
public interface XRunnable<X extends Exception> {

    /**
     * Runs the provided code and may throw a certain checked exception.
     *
     * @see Runnable#run()
     */
    void run() throws X;
}
