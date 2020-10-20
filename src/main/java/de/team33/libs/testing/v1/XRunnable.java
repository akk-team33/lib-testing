package de.team33.libs.testing.v1;

@FunctionalInterface
public interface XRunnable<X extends Exception> {

    void run() throws X;
}
