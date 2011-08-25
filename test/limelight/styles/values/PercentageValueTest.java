//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.values;

import junit.framework.TestCase;
import limelight.styles.abstrstyling.StyleValue;

public class PercentageValueTest extends TestCase
{
  private SimplePercentageValue fiftyPercent;
  private SimplePercentageValue hundredPercent;

  public void setUp() throws Exception
  {
    fiftyPercent = new SimplePercentageValue(50);
    hundredPercent = new SimplePercentageValue(100);
  }

  public void testCreation() throws Exception
  {
    assertEquals(true, (fiftyPercent instanceof StyleValue));
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
    assertEquals(true, fiftyPercent.equals(new SimplePercentageValue(50)));
    assertEquals(false, fiftyPercent.equals(hundredPercent));
    assertEquals(false, fiftyPercent.equals(null));
  }

  public void testWithFloatValues() throws Exception
  {
    SimplePercentageValue floatPercent = new SimplePercentageValue(3.14);
    assertEquals(3.14, floatPercent.getPercentage(), 0.01);
    assertEquals("3.14%", floatPercent.toString());
    assertEquals(true, floatPercent.equals(new SimplePercentageValue(3.14)));
  }
}
