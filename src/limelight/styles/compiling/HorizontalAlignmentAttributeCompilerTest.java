//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.compiling;

import limelight.styles.abstrstyling.InvalidStyleAttributeError;
import limelight.styles.HorizontalAlignment;
import limelight.styles.values.SimpleHorizontalAlignmentValue;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class HorizontalAlignmentAttributeCompilerTest
{
  private HorizontalAlignmentAttributeCompiler compiler;

  @Before
  public void setUp() throws Exception
  {
    compiler = new HorizontalAlignmentAttributeCompiler();
    compiler.setName("horizontal alignment");
  }

  @Test
  public void validValue() throws Exception
  {
    assertEquals(HorizontalAlignment.LEFT, ((SimpleHorizontalAlignmentValue) compiler.compile("left")).getAlignment());
    assertEquals(HorizontalAlignment.RIGHT, ((SimpleHorizontalAlignmentValue) compiler.compile("right")).getAlignment());
    assertEquals(HorizontalAlignment.CENTER, ((SimpleHorizontalAlignmentValue) compiler.compile("center")).getAlignment());
    assertEquals(HorizontalAlignment.CENTER, ((SimpleHorizontalAlignmentValue) compiler.compile("middle")).getAlignment());
  }

  @Test
  public void validValuesWithColon() throws Exception
  {
    assertEquals(HorizontalAlignment.LEFT, ((SimpleHorizontalAlignmentValue) compiler.compile(":left")).getAlignment());
    assertEquals(HorizontalAlignment.RIGHT, ((SimpleHorizontalAlignmentValue) compiler.compile(":right")).getAlignment());
    assertEquals(HorizontalAlignment.CENTER, ((SimpleHorizontalAlignmentValue) compiler.compile(":center")).getAlignment());
    assertEquals(HorizontalAlignment.CENTER, ((SimpleHorizontalAlignmentValue) compiler.compile(":middle")).getAlignment());
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
      assertEquals("Invalid value '" + value + "' for horizontal alignment style attribute.", e.getMessage());
    }
  }
}
