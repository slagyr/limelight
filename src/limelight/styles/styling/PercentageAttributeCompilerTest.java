package limelight.styles.styling;

import junit.framework.TestCase;
import limelight.styles.InvalidStyleAttributeError;

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
    assertEquals(0, ((PercentageAttribute) compiler.compile("0")).getPercentage());
    assertEquals(50, ((PercentageAttribute) compiler.compile("50")).getPercentage());
    assertEquals(100, ((PercentageAttribute) compiler.compile("100")).getPercentage());
    assertEquals(100, ((PercentageAttribute) compiler.compile("100%")).getPercentage());
    assertEquals(50, ((PercentageAttribute) compiler.compile("50%")).getPercentage());
    assertEquals(25, ((PercentageAttribute) compiler.compile("0.25")).getPercentage());
    assertEquals(50, ((PercentageAttribute) compiler.compile(".5")).getPercentage());
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
}
