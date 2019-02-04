package de.team33.libs.testing.v1;


import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;


public class Assert<T>
{

  private final T subject;

  private Assert(final T subject)
  {
    this.subject = subject;
  }

  public static <T> Assert<T> that(final T subject)
  {
    return new Assert<>(subject);
  }

  public void is(final Function<? super T, Optional<String>> checker)
  {
    checker.apply(subject).ifPresent(message -> {
      throw new AssertionError(message);
    });
  }

  public void isEqualTo(final Object other)
  {
    is(subject -> Objects.equals(subject, other)
      ? Optional.empty()
      : Optional.of(String.format("%n%n\tExpected: <%s>%n\t but was: <%s>%n%n", other, subject)));
  }
}
