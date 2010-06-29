//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.compiling;

import junit.framework.TestCase;
import junit.framework.Assert;
import limelight.styles.abstrstyling.InvalidStyleAttributeError;
import limelight.styles.VerticalAlignment;
import limelight.styles.values.SimpleVerticalAlignmentValue;

public class VerticalAlignmentAttributeCompilerTest extends TestCase
{
  private VerticalAlignmentAttributeCompiler compiler;

  public void setUp() throws Exception
  {
    compiler = new VerticalAlignmentAttributeCompiler();
    compiler.setName("Vertical alignment");
  }

  public void testValidValue() throws Exception
  {
    Assert.assertEquals(VerticalAlignment.TOP, ((SimpleVerticalAlignmentValue) compiler.compile("top")).getAlignment());
    assertEquals(VerticalAlignment.BOTTOM, ((SimpleVerticalAlignmentValue) compiler.compile("bottom")).getAlignment());
    assertEquals(VerticalAlignment.CENTER, ((SimpleVerticalAlignmentValue) compiler.compile("center")).getAlignment());
    assertEquals(VerticalAlignment.CENTER, ((SimpleVerticalAlignmentValue) compiler.compile("middle")).getAlignment());
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
      assertEquals("Invalid value '" + value + "' for Vertical alignment style attribute.", e.getMessage());
    }
  }
}
