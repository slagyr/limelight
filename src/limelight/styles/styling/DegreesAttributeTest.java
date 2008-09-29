package limelight.styles.styling;

import junit.framework.TestCase;
import limelight.styles.StyleAttribute;

public class DegreesAttributeTest extends TestCase
{
  private DegreesAttribute attr;

  public void testCreation() throws Exception
  {
    attr = new DegreesAttribute(180);
    assertEquals(true, attr instanceof StyleAttribute);
    assertEquals(180, attr.getDegrees());
  }
}
