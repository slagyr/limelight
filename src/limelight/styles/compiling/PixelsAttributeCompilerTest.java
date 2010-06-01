//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.compiling;

import junit.framework.TestCase;
import limelight.styles.abstrstyling.InvalidStyleAttributeError;
import limelight.styles.values.StaticPixelsValue;
import limelight.styles.values.PercentagePixelsValue;

public class PixelsAttributeCompilerTest extends TestCase
{
  private PixelsAttributeCompiler compiler;

  public void setUp() throws Exception
  {
    compiler = new PixelsAttributeCompiler();
    compiler.setName("pixels");
  }

  public void testValidValue() throws Exception
  {
    assertEquals(StaticPixelsValue.class, compiler.compile("123").getClass());
    assertEquals(StaticPixelsValue.class, compiler.compile("123.567").getClass());
    assertEquals(PercentagePixelsValue.class, compiler.compile("50%").getClass());
    assertEquals(PercentagePixelsValue.class, compiler.compile("3.14%").getClass());

    assertEquals(123, ((StaticPixelsValue) compiler.compile("123")).getPixels());
    assertEquals(0, ((StaticPixelsValue) compiler.compile("0")).getPixels());
    assertEquals(50.0, ((PercentagePixelsValue) compiler.compile("50%")).getPercentage(), 0.01);
    assertEquals(3.14, ((PercentagePixelsValue) compiler.compile("3.14%")).getPercentage(), 0.01);
    assertEquals(0.0, ((PercentagePixelsValue) compiler.compile("0%")).getPercentage(), 0.01);
  }

  public void testInvalidValues() throws Exception
  {
    checkForError("-1");
    checkForError("200%");
    checkForError("blah");
  }

  private void checkForError(String value)
  {
    try
    {
      compiler.compile(value);
      fail("should have throw error");
    }
    catch(InvalidStyleAttributeError e)
    {
      assertEquals("Invalid value '" + value + "' for pixels style attribute.", e.getMessage());
    }
  }
}
