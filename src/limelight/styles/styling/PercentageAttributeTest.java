package limelight.styles.styling;

import junit.framework.TestCase;
import limelight.styles.StyleAttribute;

public class PercentageAttributeTest extends TestCase
{
  private PercentageAttribute fiftyPercent;
  private PercentageAttribute hundredPercent;

  public void setUp() throws Exception
  {
    fiftyPercent = new PercentageAttribute(50);
    hundredPercent = new PercentageAttribute(100);
  }

  public void testCreation() throws Exception
  {
    assertEquals(true, (fiftyPercent instanceof StyleAttribute));
    assertEquals(50, fiftyPercent.getPercentage());
    assertEquals(100, hundredPercent.getPercentage());
  }

  public void testToString() throws Exception
  {
    assertEquals("50%", fiftyPercent.toString());
    assertEquals("100%", hundredPercent.toString());
  }

  public void testEquality() throws Exception
  {
    assertEquals(true, fiftyPercent.equals(fiftyPercent));
    assertEquals(true, fiftyPercent.equals(new PercentageAttribute(50)));
    assertEquals(false, fiftyPercent.equals(hundredPercent));
    assertEquals(false, fiftyPercent.equals(null));
  }
}
