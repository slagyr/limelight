//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

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
    assertEquals(50.0, fiftyPercent.getPercentage(), 0.01);
    assertEquals(100.0, hundredPercent.getPercentage(), 0.01);
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

  public void testWithFloatValues() throws Exception
  {
    SimplePercentageAttribute floatPercent = new SimplePercentageAttribute(3.14);
    assertEquals(3.14, floatPercent.getPercentage(), 0.01);
    assertEquals("3.14%", floatPercent.toString());
    assertEquals(true, floatPercent.equals(new SimplePercentageAttribute(3.14)));
  }
}
