//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.compiling;

import junit.framework.TestCase;
import limelight.styles.abstrstyling.InvalidStyleAttributeError;
import limelight.styles.styling.StaticPixelsAttribute;
import limelight.styles.styling.PercentagePixelsAttribute;

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
    assertEquals(StaticPixelsAttribute.class, compiler.compile("123").getClass());
    assertEquals(StaticPixelsAttribute.class, compiler.compile("123.567").getClass());
    assertEquals(PercentagePixelsAttribute.class, compiler.compile("50%").getClass());
    assertEquals(PercentagePixelsAttribute.class, compiler.compile("3.14%").getClass());

    assertEquals(123, ((StaticPixelsAttribute) compiler.compile("123")).getPixels());
    assertEquals(0, ((StaticPixelsAttribute) compiler.compile("0")).getPixels());
    assertEquals(50.0, ((PercentagePixelsAttribute) compiler.compile("50%")).getPercentage(), 0.01);
    assertEquals(3.14, ((PercentagePixelsAttribute) compiler.compile("3.14%")).getPercentage(), 0.01);
    assertEquals(0.0, ((PercentagePixelsAttribute) compiler.compile("0%")).getPercentage(), 0.01);
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
