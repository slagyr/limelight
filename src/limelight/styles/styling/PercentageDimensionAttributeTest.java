//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.styling;

import junit.framework.TestCase;
import limelight.styles.abstrstyling.StyleAttribute;
import limelight.styles.abstrstyling.IntegerAttribute;
import limelight.styles.abstrstyling.NoneableAttribute;

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

  public void testIsAuto() throws Exception
  {
    assertEquals(false, fiftyPercent.isAuto());    
  }

  public void testIsPercentage() throws Exception
  {
    assertEquals(true, fiftyPercent.isPercentage());
  }

  public void testCalculateDimensionWithNoMinOrMax() throws Exception
  {
    NoneableAttribute<IntegerAttribute> min = new NoneableAttribute<IntegerAttribute>(null);
    NoneableAttribute<IntegerAttribute> max = new NoneableAttribute<IntegerAttribute>(null);

    assertEquals(50, fiftyPercent.calculateDimension(100, min, max));
    assertEquals(100, hundredPercent.calculateDimension(100, min, max));
  }

  public void testCalculateDimensionWithMin() throws Exception
  {
    NoneableAttribute<IntegerAttribute> min = new NoneableAttribute<IntegerAttribute>(new SimpleIntegerAttribute(75));
    NoneableAttribute<IntegerAttribute> max = new NoneableAttribute<IntegerAttribute>(null);

    assertEquals(75, fiftyPercent.calculateDimension(100, min, max));
    assertEquals(100, hundredPercent.calculateDimension(100, min, max));
  }

  public void testCalculateDimensionWithMax() throws Exception
  {
    NoneableAttribute<IntegerAttribute> min = new NoneableAttribute<IntegerAttribute>(null);
    NoneableAttribute<IntegerAttribute> max = new NoneableAttribute<IntegerAttribute>(new SimpleIntegerAttribute(75));

    assertEquals(50, fiftyPercent.calculateDimension(100, min, max));
    assertEquals(75, hundredPercent.calculateDimension(100, min, max));
  }

  public void testCalculateDimensionWithMinAndMax() throws Exception
  {
    NoneableAttribute<IntegerAttribute> min = new NoneableAttribute<IntegerAttribute>(new SimpleIntegerAttribute(60));
    NoneableAttribute<IntegerAttribute> max = new NoneableAttribute<IntegerAttribute>(new SimpleIntegerAttribute(80));

    assertEquals(60, fiftyPercent.calculateDimension(100, min, max));
    assertEquals(80, hundredPercent.calculateDimension(100, min, max));
  }
  
  public void testCollapseExcessDoesNothing() throws Exception
  {
    assertEquals(200, fiftyPercent.collapseExcess(200, 50, null, null));
  }
}
