//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.compiling;

import junit.framework.TestCase;
import junit.framework.Assert;
import limelight.styles.abstrstyling.InvalidStyleAttributeError;
import limelight.styles.values.SimpleFontStyleValue;

public class FontStyleAttributeCompilerTest extends TestCase
{
  private FontStyleAttributeCompiler compiler;

  public void setUp() throws Exception
  {
    compiler = new FontStyleAttributeCompiler();
    compiler.setName("font style");
  }

  public void testValidValue() throws Exception
  {
    Assert.assertEquals(true, ((SimpleFontStyleValue) compiler.compile("bold")).isBold());
  }

  public void testInvalidValue() throws Exception
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
