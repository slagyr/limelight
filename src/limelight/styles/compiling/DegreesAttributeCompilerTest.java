//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.compiling;

import limelight.styles.abstrstyling.StyleValue;
import limelight.styles.abstrstyling.InvalidStyleAttributeError;
import limelight.styles.values.SimpleDegreesValue;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DegreesAttributeCompilerTest
{
  private DegreesAttributeCompiler compiler;

  @Before
  public void setUp() throws Exception
  {
    compiler = new DegreesAttributeCompiler();
    compiler.setName("degrees");
  }

  @Test
  public void validValue() throws Exception
  {
    StyleValue attr = compiler.compile("123");
    assertEquals(123, ((SimpleDegreesValue)attr).getDegrees());

    attr = compiler.compile(":123");
    assertEquals(123, ((SimpleDegreesValue)attr).getDegrees());
  }

  @Test
  public void invalidValue() throws Exception
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
