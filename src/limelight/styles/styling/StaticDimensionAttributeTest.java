//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.styling;

import limelight.styles.abstrstyling.StyleAttribute;
import limelight.styles.abstrstyling.NoneableAttribute;
import limelight.styles.abstrstyling.DimensionAttribute;
import junit.framework.TestCase;

public class StaticDimensionAttributeTest extends TestCase
{
  private StaticDimensionAttribute fifty;
  private StaticDimensionAttribute hundred;
  private StaticDimensionAttribute thousand;

  public void setUp() throws Exception
  {
    fifty = new StaticDimensionAttribute(50);
    hundred = new StaticDimensionAttribute(100);
    thousand = new StaticDimensionAttribute(1000);
  }

  public void testCreation() throws Exception
  {
    assertEquals(true, (fifty instanceof StyleAttribute));
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
    assertEquals(true, fifty.equals(new StaticDimensionAttribute(50)));
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
    NoneableAttribute<DimensionAttribute> min = new NoneableAttribute<DimensionAttribute>(null);
    NoneableAttribute<DimensionAttribute> max = new NoneableAttribute<DimensionAttribute>(null);

    assertEquals(50, fifty.calculateDimension(100, min, max, 0));
    assertEquals(100, hundred.calculateDimension(100, min, max, 0));
  }

  public void testCalculateDimensionWithMinAndMax() throws Exception
  {
    NoneableAttribute<DimensionAttribute> min = new NoneableAttribute<DimensionAttribute>(new StaticDimensionAttribute(60));
    NoneableAttribute<DimensionAttribute> max = new NoneableAttribute<DimensionAttribute>(new StaticDimensionAttribute(80));

    assertEquals(50, fifty.calculateDimension(100, min, max, 0));
    assertEquals(100, hundred.calculateDimension(100, min, max, 0));
  }

  public void testCollapseExcessDoesNothing() throws Exception
  {
    assertEquals(200, fifty.collapseExcess(200, 50, null, null));
  }
}
