package limelight.styles.styling;

import junit.framework.TestCase;
import limelight.styles.abstrstyling.InvalidStyleAttributeError;

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
    assertEquals(0.0, ((SimplePercentageAttribute) compiler.compile("0")).getPercentage(), 0.01);
    assertEquals(50.0, ((SimplePercentageAttribute) compiler.compile("50")).getPercentage(), 0.01);
    assertEquals(100.0, ((SimplePercentageAttribute) compiler.compile("100")).getPercentage(), 0.01);
    assertEquals(100.0, ((SimplePercentageAttribute) compiler.compile("100%")).getPercentage(), 0.01);
    assertEquals(50.0, ((SimplePercentageAttribute) compiler.compile("50%")).getPercentage(), 0.01);
    assertEquals(50.5, ((SimplePercentageAttribute) compiler.compile("50.5%")).getPercentage(), 0.01);
    assertEquals(3.14, ((SimplePercentageAttribute) compiler.compile("3.14%")).getPercentage(), 0.01);
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
    assertEquals(99.0, ((SimplePercentageAttribute) compiler.compile(99)).getPercentage(), 0.01);
    assertEquals(3.14, ((SimplePercentageAttribute) compiler.compile(3.14)).getPercentage(), 0.01);
    assertEquals(45.0, ((SimplePercentageAttribute) compiler.compile(new Long(45))).getPercentage(), 0.01);
    assertEquals(5.25, ((SimplePercentageAttribute) compiler.compile(new Double(5.25))).getPercentage(), 0.01);
  }
}
