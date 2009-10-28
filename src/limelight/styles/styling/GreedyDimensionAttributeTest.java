//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.styling;

import junit.framework.TestCase;
import limelight.styles.abstrstyling.NoneableAttribute;
import limelight.styles.abstrstyling.DimensionAttribute;

public class GreedyDimensionAttributeTest extends TestCase
{
  private GreedyDimensionAttribute attribute;

  public void setUp() throws Exception
  {
    attribute = new GreedyDimensionAttribute();
  }

  public void testIsDynamic() throws Exception
  {
    assertEquals(true, attribute.isDynamic());
  }

  public void testCalculateDimension() throws Exception
  {
    assertEquals(25, attribute.calculateDimension(100, size(25), size(200), 0));
    assertEquals(50, attribute.calculateDimension(100, size(50), size(200), 0));
    assertEquals(200, attribute.calculateDimension(100, size(200), size(400), 0));
    assertEquals(0, attribute.calculateDimension(100, new NoneableAttribute<DimensionAttribute>(null), size(400), 0));
  }

  private NoneableAttribute<DimensionAttribute> size(int size)
  {
    return new NoneableAttribute<DimensionAttribute>(new StaticDimensionAttribute(size));
  }
  
  public void testCollapseExcess() throws Exception
  {
    assertEquals(25, attribute.collapseExcess(25, 10, size(0), size(100)));
  }
}
