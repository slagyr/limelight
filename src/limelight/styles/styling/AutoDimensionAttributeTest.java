//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.styling;

import junit.framework.TestCase;
import limelight.styles.abstrstyling.NoneableAttribute;
import limelight.styles.abstrstyling.DimensionAttribute;

public class AutoDimensionAttributeTest extends TestCase
{
  private AutoDimensionAttribute auto;

  public void setUp() throws Exception
  {
    auto = new AutoDimensionAttribute();
  }

  public void testToString() throws Exception
  {
    assertEquals("auto", auto.toString());
  }
  
  public void testEquals() throws Exception
  {
    assertEquals(true, auto.equals(auto));
    assertEquals(true, auto.equals(new AutoDimensionAttribute()));
    assertEquals(false, auto.equals(null));
  }
  
  public void testIsDynamic() throws Exception
  {
    assertEquals(true, auto.isDynamic());
  }

  public void testIsAuto() throws Exception
  {
    assertEquals(true, auto.isAuto());
  }

  public void testCalculateDimensionWithNoMinOrMax() throws Exception
  {
    NoneableAttribute<DimensionAttribute> min = new NoneableAttribute<DimensionAttribute>(null);
    NoneableAttribute<DimensionAttribute> max = new NoneableAttribute<DimensionAttribute>(null);

    assertEquals(100, auto.calculateDimension(100, min, max, 0));
  }
  
  public void testCalculateDimensionWithMax() throws Exception
  {
    NoneableAttribute<DimensionAttribute> min = new NoneableAttribute<DimensionAttribute>(null);
    NoneableAttribute<DimensionAttribute> max = new NoneableAttribute<DimensionAttribute>(new StaticDimensionAttribute(50));

    assertEquals(50, auto.calculateDimension(100, min, max, 0));
  }

  public void testCollapseExcessWithNoMinOrMax() throws Exception
  {
    NoneableAttribute<DimensionAttribute> min = new NoneableAttribute<DimensionAttribute>(null);
    NoneableAttribute<DimensionAttribute> max = new NoneableAttribute<DimensionAttribute>(null);

    assertEquals(100, auto.collapseExcess(200, 100, min, max));
  }

  public void testCollapseExcessWithMin() throws Exception
  {
    NoneableAttribute<DimensionAttribute> min = new NoneableAttribute<DimensionAttribute>(new StaticDimensionAttribute(150));
    NoneableAttribute<DimensionAttribute> max = new NoneableAttribute<DimensionAttribute>(null);

    assertEquals(150, auto.collapseExcess(200, 100, min, max));
  }

  public void testCollapseExcessWithMax() throws Exception
  {
    NoneableAttribute<DimensionAttribute> min = new NoneableAttribute<DimensionAttribute>(null);
    NoneableAttribute<DimensionAttribute> max = new NoneableAttribute<DimensionAttribute>(new StaticDimensionAttribute(50));

    assertEquals(50, auto.collapseExcess(200, 100, min, max));
  }
}
