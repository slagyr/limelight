//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.values;

import junit.framework.TestCase;
import limelight.styles.abstrstyling.DimensionValue;
import limelight.styles.abstrstyling.NoneableValue;
import limelight.styles.abstrstyling.StyleValue;

public class PercentageDimensionValueTest extends TestCase
{
  private PercentageDimensionValue fiftyPercent;
  private PercentageDimensionValue hundredPercent;

  public void setUp() throws Exception
  {
    fiftyPercent = new PercentageDimensionValue(50);
    hundredPercent = new PercentageDimensionValue(100);
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
    assertEquals(true, fiftyPercent.equals(new PercentageDimensionValue(50)));
    assertEquals(false, fiftyPercent.equals(hundredPercent));
    assertEquals(false, fiftyPercent.equals(null));
  }
  
  public void testIsDynamic() throws Exception
  {
    assertEquals(true, fiftyPercent.isDynamic());
  }

  public void testIsAuto() throws Exception
  {
    assertEquals(false, fiftyPercent.isAuto());    
  }

  public void testCalculateDimensionWithNoMinOrMax() throws Exception
  {
    NoneableValue<DimensionValue> min = new NoneableValue<DimensionValue>(null);
    NoneableValue<DimensionValue> max = new NoneableValue<DimensionValue>(null);

    assertEquals(50, fiftyPercent.calculateDimension(100, min, max, 0));
    assertEquals(100, hundredPercent.calculateDimension(100, min, max, 0));
  }

  public void testCalculateDimensionWithMin() throws Exception
  {
    NoneableValue<DimensionValue> min = new NoneableValue<DimensionValue>(new StaticDimensionValue(75));
    NoneableValue<DimensionValue> max = new NoneableValue<DimensionValue>(null);

    assertEquals(75, fiftyPercent.calculateDimension(100, min, max, 0));
    assertEquals(100, hundredPercent.calculateDimension(100, min, max, 0));
  }

  public void testCalculateDimensionWithMax() throws Exception
  {
    NoneableValue<DimensionValue> min = new NoneableValue<DimensionValue>(null);
    NoneableValue<DimensionValue> max = new NoneableValue<DimensionValue>(new StaticDimensionValue(75));

    assertEquals(50, fiftyPercent.calculateDimension(100, min, max, 0));
    assertEquals(75, hundredPercent.calculateDimension(100, min, max, 0));
  }

  public void testCalculateDimensionWithMinAndMax() throws Exception
  {
    NoneableValue<DimensionValue> min = new NoneableValue<DimensionValue>(new StaticDimensionValue(60));
    NoneableValue<DimensionValue> max = new NoneableValue<DimensionValue>(new StaticDimensionValue(80));

    assertEquals(60, fiftyPercent.calculateDimension(100, min, max, 0));
    assertEquals(80, hundredPercent.calculateDimension(100, min, max, 0));
  }
  
  public void testCollapseExcessDoesNothing() throws Exception
  {
    assertEquals(200, fiftyPercent.collapseExcess(200, 50, null, null));
  }
}
