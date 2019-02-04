package de.team33.libs.testing.v1;


import java.util.Objects;


public class Assert<T>
{

  private final T subject;

  private Assert(final T subject)
  {
    this.subject = subject;
  }

  public static <T> Assert that(final T subject)
  {
    return new Assert<>(subject);
  }

  public void isEqualTo(final Object other)
  {
    if (!Objects.equals(subject, other))
    {
      throw new AssertionError(String.format("%n%n\tExpected: <%s>%n\t but was: <%s>%n%n", other, subject));
    }
  }
}
