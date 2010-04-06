//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.compiling;

import junit.framework.TestCase;
import limelight.styles.abstrstyling.InvalidStyleAttributeError;
import limelight.styles.styling.*;

public class FillStrategyAttributeCompilerTest extends TestCase
{
  private FillStrategyAttributeCompiler compiler;

  public void setUp() throws Exception
  {
    compiler = new FillStrategyAttributeCompiler();
    compiler.setName("fill strategy");
  }

  public void testValidValue() throws Exception
  {
    assertEquals(StaticFillStrategyAttribute.class, compiler.compile("static").getClass());
    assertEquals(RepeatFillStrategyAttribute.class, compiler.compile("repeat").getClass());
    assertEquals(RepeatXFillStrategyAttribute.class, compiler.compile("repeat_x").getClass());
    assertEquals(RepeatYFillStrategyAttribute.class, compiler.compile("repeat_y").getClass());
    assertEquals(RepeatXFillStrategyAttribute.class, compiler.compile("repeat-x").getClass());
    assertEquals(RepeatYFillStrategyAttribute.class, compiler.compile("repeat-y").getClass());
    assertEquals(ScaleFillStrategyAttribute.class, compiler.compile("scale").getClass());
    assertEquals(ScaleXFillStrategyAttribute.class, compiler.compile("scale_x").getClass());
    assertEquals(ScaleXFillStrategyAttribute.class, compiler.compile("scale-x").getClass());
    assertEquals(ScaleYFillStrategyAttribute.class, compiler.compile("scale_y").getClass());
    assertEquals(ScaleYFillStrategyAttribute.class, compiler.compile("scale-y").getClass());
  }

  public void testInvalidValue() throws Exception
  {
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
      assertEquals("Invalid value '" + value + "' for fill strategy style attribute.", e.getMessage());
    }
  }
}
