//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.styling;

import junit.framework.TestCase;
import limelight.styles.abstrstyling.IntegerAttribute;
import limelight.styles.abstrstyling.NoneableAttribute;

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

  public void testIsAuto() throws Exception
  {
    assertEquals(true, auto.isAuto());
  }  

  public void testIsPercentage() throws Exception
  {
    assertEquals(false, auto.isPercentage());
  }

  public void testCalculateDimensionWithNoMinOrMax() throws Exception
  {
    NoneableAttribute<IntegerAttribute> min = new NoneableAttribute<IntegerAttribute>(null);
    NoneableAttribute<IntegerAttribute> max = new NoneableAttribute<IntegerAttribute>(null);

    assertEquals(100, auto.calculateDimension(100, min, max));
  }
  
  public void testCalculateDimensionWithMax() throws Exception
  {
    NoneableAttribute<IntegerAttribute> min = new NoneableAttribute<IntegerAttribute>(null);
    NoneableAttribute<IntegerAttribute> max = new NoneableAttribute<IntegerAttribute>(new SimpleIntegerAttribute(50));

    assertEquals(50, auto.calculateDimension(100, min, max));
  }

  public void testCollapseExcessWithNoMinOrMax() throws Exception
  {
    NoneableAttribute<IntegerAttribute> min = new NoneableAttribute<IntegerAttribute>(null);
    NoneableAttribute<IntegerAttribute> max = new NoneableAttribute<IntegerAttribute>(null);

    assertEquals(100, auto.collapseExcess(200, 100, min, max));
  }

  public void testCollapseExcessWithMin() throws Exception
  {
    NoneableAttribute<IntegerAttribute> min = new NoneableAttribute<IntegerAttribute>(new SimpleIntegerAttribute(150));
    NoneableAttribute<IntegerAttribute> max = new NoneableAttribute<IntegerAttribute>(null);

    assertEquals(150, auto.collapseExcess(200, 100, min, max));
  }

  public void testCollapseExcessWithMax() throws Exception
  {
    NoneableAttribute<IntegerAttribute> min = new NoneableAttribute<IntegerAttribute>(null);
    NoneableAttribute<IntegerAttribute> max = new NoneableAttribute<IntegerAttribute>(new SimpleIntegerAttribute(50));

    assertEquals(50, auto.collapseExcess(200, 100, min, max));
  }
}
