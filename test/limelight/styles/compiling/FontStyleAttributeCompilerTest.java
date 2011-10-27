//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.styles.compiling;

import limelight.styles.abstrstyling.InvalidStyleAttributeError;
import limelight.styles.values.SimpleFontStyleValue;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class FontStyleAttributeCompilerTest
{
  private FontStyleAttributeCompiler compiler;

  @Before
  public void setUp() throws Exception
  {
    compiler = new FontStyleAttributeCompiler();
    compiler.setName("font style");
  }

  @Test
  public void validValue() throws Exception
  {
    assertEquals(true, ((SimpleFontStyleValue) compiler.compile("bold")).isBold());
  }

  @Test
  public void valuesWithColon() throws Exception
  {
    assertEquals(true, ((SimpleFontStyleValue) compiler.compile(":bold")).isBold());
  }

  @Test
  public void invalidValue() throws Exception
  {
    checkForError("blah");
    checkForError("bold and italic");
    checkForError("bold or bold");
    checkForError("underlined");
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
      assertEquals("Invalid value '" + value + "' for font style style attribute.", e.getMessage());
    }
  }
}
