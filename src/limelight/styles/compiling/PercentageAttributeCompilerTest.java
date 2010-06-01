//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.compiling;

import junit.framework.TestCase;
import junit.framework.Assert;
import limelight.styles.abstrstyling.InvalidStyleAttributeError;
import limelight.styles.values.SimplePercentageValue;

public class PercentageAttributeCompilerTest extends TestCase
{
  private PercentageAttributeCompiler compiler;

  public void setUp() throws Exception
  {
    compiler = new PercentageAttributeCompiler();
    compiler.setName("percentage");
  }

  public void testValidValue() throws Exception
  {
    Assert.assertEquals(0.0, ((SimplePercentageValue) compiler.compile("0")).getPercentage(), 0.01);
    assertEquals(50.0, ((SimplePercentageValue) compiler.compile("50")).getPercentage(), 0.01);
    assertEquals(100.0, ((SimplePercentageValue) compiler.compile("100")).getPercentage(), 0.01);
    assertEquals(100.0, ((SimplePercentageValue) compiler.compile("100%")).getPercentage(), 0.01);
    assertEquals(50.0, ((SimplePercentageValue) compiler.compile("50%")).getPercentage(), 0.01);
    assertEquals(50.5, ((SimplePercentageValue) compiler.compile("50.5%")).getPercentage(), 0.01);
    assertEquals(3.14, ((SimplePercentageValue) compiler.compile("3.14%")).getPercentage(), 0.01);
  }

  public void testInvalidValue() throws Exception
  {
    try
    {
      compiler.compile("blah");
      fail("Should have thrown exception");
    }
    catch(InvalidStyleAttributeError e)
    {
      assertEquals("Invalid value 'blah' for percentage style attribute.", e.getMessage());
    }
  }
  
  public void testCompilingFloatValues() throws Exception
  {
    assertEquals(99.0, ((SimplePercentageValue) compiler.compile(99)).getPercentage(), 0.01);
    assertEquals(3.14, ((SimplePercentageValue) compiler.compile(3.14)).getPercentage(), 0.01);
    assertEquals(45.0, ((SimplePercentageValue) compiler.compile(new Long(45))).getPercentage(), 0.01);
    assertEquals(5.25, ((SimplePercentageValue) compiler.compile(new Double(5.25))).getPercentage(), 0.01);
  }
}
