package de.team33.test.testing.v1;

import de.team33.libs.testing.v1.Assert;
import org.junit.Test;


public class AssertTest
{

  @Test
  public void that()
  {
    Assert.that(true).isEqualTo(false);
  }
}
