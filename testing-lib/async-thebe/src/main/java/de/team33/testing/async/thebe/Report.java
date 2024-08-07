package de.team33.testing.async.thebe;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Collections.synchronizedList;
import static java.util.Collections.unmodifiableList;

/**
 * A report of multiple executions of a method.
 *
 * @param <R> The type of result of the method to be run.
 */
public final class Report<R> {

  private final List<R> results;
  private final List<Throwable> caught;

  @SuppressWarnings("WeakerAccess")
  private Report(final Builder<R> builder) {
    this.results = unmodifiableList(new ArrayList<>(builder.results));
    this.caught = unmodifiableList(new ArrayList<>(builder.caught));
  }

  /**
   * Returns a {@link List} of all results that have accumulated during reporting.
   */
  public final List<R> list() {
    return results;
  }

  /**
   * Returns a limited {@link Stream} of all results that have accumulated during reporting.
   */
  public Stream<R> stream() {
    return results.stream();
  }

  /**
   * Returns a {@link List} of all {@linkplain Throwable exceptions} that occurred during reporting.
   */
  public final List<Throwable> getCaught() {
    return caught;
  }

  /**
   * Returns a {@link List} of all {@linkplain Throwable exceptions} of a certain type that occurred during
   * reporting. Certain derived types can be excluded from the result.
   *
   * @param <X> The type of {@linkplain Throwable exceptions} to be listed.
   */
  @SuppressWarnings("OverloadedVarargsMethod")
  @SafeVarargs
  public final <X extends Throwable> List<X> getCaught(final Class<X> xClass,
                                                       final Class<? extends X>... ignorable) {
    return streamCaught(xClass, ignorable).collect(Collectors.toList());
  }

  @SafeVarargs
  private final <X extends Throwable> Stream<X> streamCaught(final Class<X> xClass,
                                                             final Class<? extends X>... ignorable) {
    return caught.stream()
                 .filter(xClass::isInstance)
                 .map(xClass::cast)
                 .filter(throwable -> Stream.of(ignorable)
                                                .noneMatch(iClass -> iClass.isInstance(throwable)));
  }

  /**
   * Re-throws the first {@linkplain Throwable exception} of a certain type that occurred during
   * reporting after all further {@linkplain Throwable exceptions} of that type have been
   * {@linkplain Throwable#addSuppressed(Throwable) added as suppressed}.
   * Certain derived types can be excluded from processing.
   *
   * @param <X> The type of {@linkplain Throwable exceptions} to be processed.
   */
  @SafeVarargs
  public final <X extends Throwable> Report<R> reThrow(final Class<X> xClass,
                                                       final Class<? extends X>... ignorable) throws X {
    // First ...
    final X caught = streamCaught(xClass, ignorable).findAny().orElse(null);
    if (null != caught) {
      // Add the rest ...
      streamCaught(Throwable.class).filter(more -> more != caught)
                                   .forEach(caught::addSuppressed);
      throw caught;
    }
    return this;
  }

  @SuppressWarnings("ProhibitedExceptionDeclared")
  public final Report<R> reThrowAny() throws Exception {
    return reThrow(Error.class).reThrow(Exception.class);
  }

  public final int size() {
    return results.size();
  }

  @SuppressWarnings("UnusedReturnValue")
  static class Builder<R> {

    final List<Throwable> caught = synchronizedList(new LinkedList<>());
    final List<R> results = synchronizedList(new LinkedList<>());

    final Report<R> build() {
      return new Report<>(this);
    }

    final Builder<R> add(final Throwable caught) {
      this.caught.add(caught);
      return this;
    }

    final Builder<R> add(final R result) {
      results.add(result);
      return this;
    }
  }
}
