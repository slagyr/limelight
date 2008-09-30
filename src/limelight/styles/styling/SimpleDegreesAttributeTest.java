package limelight.styles.styling;

import junit.framework.TestCase;
import limelight.styles.abstrstyling.StyleAttribute;

public class SimpleDegreesAttributeTest extends TestCase
{
  private SimpleDegreesAttribute attr;

  public void testCreation() throws Exception
  {
    attr = new SimpleDegreesAttribute(180);
    assertEquals(true, attr instanceof StyleAttribute);
    assertEquals(180, attr.getDegrees());
  }
}
