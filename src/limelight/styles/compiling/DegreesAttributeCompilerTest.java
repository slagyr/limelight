//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.compiling;

import junit.framework.TestCase;
import junit.framework.Assert;
import limelight.styles.abstrstyling.StyleValue;
import limelight.styles.abstrstyling.InvalidStyleAttributeError;
import limelight.styles.values.SimpleDegreesValue;

public class DegreesAttributeCompilerTest extends TestCase
{
  private DegreesAttributeCompiler compiler;

  public void setUp() throws Exception
  {
    compiler = new DegreesAttributeCompiler();
    compiler.setName("degrees");
  }

  public void testValidValue() throws Exception
  {
    StyleValue attr = compiler.compile("123");

    Assert.assertEquals(123, ((SimpleDegreesValue)attr).getDegrees());
  }

  public void testInvalidValue() throws Exception
  {
    checkForError("blah");
    checkForError("-1");
    checkForError("361");
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
      assertEquals("Invalid value '" + value + "' for degrees style attribute.", e.getMessage());
    }
  }
}
