//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.values;

import limelight.styles.abstrstyling.NoneableValue;
import limelight.styles.abstrstyling.DimensionValue;
import junit.framework.TestCase;
import limelight.styles.abstrstyling.StyleValue;

public class StaticDimensionValueTest extends TestCase
{
  private StaticDimensionValue fifty;
  private StaticDimensionValue hundred;
  private StaticDimensionValue thousand;

  public void setUp() throws Exception
  {
    fifty = new StaticDimensionValue(50);
    hundred = new StaticDimensionValue(100);
    thousand = new StaticDimensionValue(1000);
  }

  public void testCreation() throws Exception
  {
    assertEquals(true, (fifty instanceof StyleValue));
    assertEquals(50, fifty.getValue());
  }

  public void testToString() throws Exception
  {
    assertEquals("50", fifty.toString());
    assertEquals("100", hundred.toString());
    assertEquals("1000", thousand.toString());
  }

  public void testEquality() throws Exception
  {
    assertEquals(true, fifty.equals(fifty));
    assertEquals(true, fifty.equals(new StaticDimensionValue(50)));
    assertEquals(false, fifty.equals(hundred));
    assertEquals(false, fifty.equals(null));
  }
  
  public void testIsDynamic() throws Exception
  {
    assertEquals(false, fifty.isDynamic());
  }

  public void testIsAuto() throws Exception
  {
    assertEquals(false, fifty.isAuto());
  }

  public void testCalculateDimensionWithNoMinOrMax() throws Exception
  {
    NoneableValue<DimensionValue> min = new NoneableValue<DimensionValue>(null);
    NoneableValue<DimensionValue> max = new NoneableValue<DimensionValue>(null);

    assertEquals(50, fifty.calculateDimension(100, min, max, 0));
    assertEquals(100, hundred.calculateDimension(100, min, max, 0));
  }

  public void testCalculateDimensionWithMinAndMax() throws Exception
  {
    NoneableValue<DimensionValue> min = new NoneableValue<DimensionValue>(new StaticDimensionValue(60));
    NoneableValue<DimensionValue> max = new NoneableValue<DimensionValue>(new StaticDimensionValue(80));

    assertEquals(50, fifty.calculateDimension(100, min, max, 0));
    assertEquals(100, hundred.calculateDimension(100, min, max, 0));
  }

  public void testCollapseExcessDoesNothing() throws Exception
  {
    assertEquals(200, fifty.collapseExcess(200, 50, null, null));
  }
}
