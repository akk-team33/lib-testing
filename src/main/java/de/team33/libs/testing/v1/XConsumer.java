package de.team33.libs.testing.v1;

import java.util.function.Consumer;

/**
 * A kind of consumer that allows to throw a checked exception.
 *
 * @param <T> the type of the input to the consumer's method
 * @param <X> the type of exception that may be thrown by the consumer's method
 *
 * @see Consumer
 */
@FunctionalInterface
public interface XConsumer<T, X extends Exception> {

    /**
     * Performs this operation on the given argument.
     *
     * @param t the input argument
     *
     * @see Consumer#accept(Object)
     */
    void accept(T t) throws X;
}
