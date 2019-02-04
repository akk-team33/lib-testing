package de.team33.libs.testing.v1;


public class Assert
{

  public static <R, X extends Throwable> Assert that(final Supplier<R, X> supplier)
  {
    throw new UnsupportedOperationException("not yet implemented");
  }

  public interface Supplier<R, X extends Throwable>
  {
     R get() throws X;
  }
}
