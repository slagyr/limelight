package limelight.styles.styling;

import junit.framework.TestCase;
import limelight.styles.abstrstyling.StyleAttribute;

public class PercentageAttributeTest extends TestCase
{
  private SimplePercentageAttribute fiftyPercent;
  private SimplePercentageAttribute hundredPercent;

  public void setUp() throws Exception
  {
    fiftyPercent = new SimplePercentageAttribute(50);
    hundredPercent = new SimplePercentageAttribute(100);
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
    assertEquals(true, fiftyPercent.equals(new SimplePercentageAttribute(50)));
    assertEquals(false, fiftyPercent.equals(hundredPercent));
    assertEquals(false, fiftyPercent.equals(null));
  }
}
