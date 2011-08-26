//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.styles.compiling;

import limelight.styles.abstrstyling.InvalidStyleAttributeError;
import limelight.styles.VerticalAlignment;
import limelight.styles.values.SimpleVerticalAlignmentValue;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class VerticalAlignmentAttributeCompilerTest
{
  private VerticalAlignmentAttributeCompiler compiler;

  @Before
  public void setUp() throws Exception
  {
    compiler = new VerticalAlignmentAttributeCompiler();
    compiler.setName("Vertical alignment");
  }

  @Test
  public void validValue() throws Exception
  {
    assertEquals(VerticalAlignment.TOP, ((SimpleVerticalAlignmentValue) compiler.compile("top")).getAlignment());
    assertEquals(VerticalAlignment.BOTTOM, ((SimpleVerticalAlignmentValue) compiler.compile("bottom")).getAlignment());
    assertEquals(VerticalAlignment.CENTER, ((SimpleVerticalAlignmentValue) compiler.compile("center")).getAlignment());
    assertEquals(VerticalAlignment.CENTER, ((SimpleVerticalAlignmentValue) compiler.compile("middle")).getAlignment());
  }

  @Test
  public void validValuesWithColon() throws Exception
  {
    assertEquals(VerticalAlignment.TOP, ((SimpleVerticalAlignmentValue) compiler.compile(":top")).getAlignment());
    assertEquals(VerticalAlignment.BOTTOM, ((SimpleVerticalAlignmentValue) compiler.compile(":bottom")).getAlignment());
    assertEquals(VerticalAlignment.CENTER, ((SimpleVerticalAlignmentValue) compiler.compile(":center")).getAlignment());
    assertEquals(VerticalAlignment.CENTER, ((SimpleVerticalAlignmentValue) compiler.compile(":middle")).getAlignment());
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
      assertEquals("Invalid value '" + value + "' for Vertical alignment style attribute.", e.getMessage());
    }
  }
}
