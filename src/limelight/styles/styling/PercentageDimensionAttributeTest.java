//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.styling;

import junit.framework.TestCase;
import limelight.styles.abstrstyling.StyleAttribute;
import limelight.styles.abstrstyling.NoneableAttribute;
import limelight.styles.abstrstyling.DimensionAttribute;

public class PercentageDimensionAttributeTest extends TestCase
{
  private PercentageDimensionAttribute fiftyPercent;
  private PercentageDimensionAttribute hundredPercent;

  public void setUp() throws Exception
  {
    fiftyPercent = new PercentageDimensionAttribute(50);
    hundredPercent = new PercentageDimensionAttribute(100);
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
    assertEquals(true, fiftyPercent.equals(new PercentageDimensionAttribute(50)));
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
    NoneableAttribute<DimensionAttribute> min = new NoneableAttribute<DimensionAttribute>(null);
    NoneableAttribute<DimensionAttribute> max = new NoneableAttribute<DimensionAttribute>(null);

    assertEquals(50, fiftyPercent.calculateDimension(100, min, max, 0));
    assertEquals(100, hundredPercent.calculateDimension(100, min, max, 0));
  }

  public void testCalculateDimensionWithMin() throws Exception
  {
    NoneableAttribute<DimensionAttribute> min = new NoneableAttribute<DimensionAttribute>(new StaticDimensionAttribute(75));
    NoneableAttribute<DimensionAttribute> max = new NoneableAttribute<DimensionAttribute>(null);

    assertEquals(75, fiftyPercent.calculateDimension(100, min, max, 0));
    assertEquals(100, hundredPercent.calculateDimension(100, min, max, 0));
  }

  public void testCalculateDimensionWithMax() throws Exception
  {
    NoneableAttribute<DimensionAttribute> min = new NoneableAttribute<DimensionAttribute>(null);
    NoneableAttribute<DimensionAttribute> max = new NoneableAttribute<DimensionAttribute>(new StaticDimensionAttribute(75));

    assertEquals(50, fiftyPercent.calculateDimension(100, min, max, 0));
    assertEquals(75, hundredPercent.calculateDimension(100, min, max, 0));
  }

  public void testCalculateDimensionWithMinAndMax() throws Exception
  {
    NoneableAttribute<DimensionAttribute> min = new NoneableAttribute<DimensionAttribute>(new StaticDimensionAttribute(60));
    NoneableAttribute<DimensionAttribute> max = new NoneableAttribute<DimensionAttribute>(new StaticDimensionAttribute(80));

    assertEquals(60, fiftyPercent.calculateDimension(100, min, max, 0));
    assertEquals(80, hundredPercent.calculateDimension(100, min, max, 0));
  }
  
  public void testCollapseExcessDoesNothing() throws Exception
  {
    assertEquals(200, fiftyPercent.collapseExcess(200, 50, null, null));
  }
}
