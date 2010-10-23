//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.compiling;

import limelight.styles.abstrstyling.InvalidStyleAttributeError;
import limelight.styles.values.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class FillStrategyAttributeCompilerTest
{
  private FillStrategyAttributeCompiler compiler;

  @Before
  public void setUp() throws Exception
  {
    compiler = new FillStrategyAttributeCompiler();
    compiler.setName("fill strategy");
  }

  @Test
  public void validValue() throws Exception
  {
    assertEquals(StaticFillStrategyValue.class, compiler.compile("static").getClass());
    assertEquals(RepeatFillStrategyValue.class, compiler.compile("repeat").getClass());
    assertEquals(RepeatXFillStrategyValue.class, compiler.compile("repeat_x").getClass());
    assertEquals(RepeatYFillStrategyValue.class, compiler.compile("repeat_y").getClass());
    assertEquals(RepeatXFillStrategyValue.class, compiler.compile("repeat-x").getClass());
    assertEquals(RepeatYFillStrategyValue.class, compiler.compile("repeat-y").getClass());
    assertEquals(ScaleFillStrategyValue.class, compiler.compile("scale").getClass());
    assertEquals(ScaleXFillStrategyValue.class, compiler.compile("scale_x").getClass());
    assertEquals(ScaleXFillStrategyValue.class, compiler.compile("scale-x").getClass());
    assertEquals(ScaleYFillStrategyValue.class, compiler.compile("scale_y").getClass());
    assertEquals(ScaleYFillStrategyValue.class, compiler.compile("scale-y").getClass());
  }

  @Test
  public void keywordValues() throws Exception
  {
    assertEquals(StaticFillStrategyValue.class, compiler.compile(":static").getClass());
    assertEquals(RepeatFillStrategyValue.class, compiler.compile(":repeat").getClass());
  }

  @Test
  public void invalidValue() throws Exception
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
