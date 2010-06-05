//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.abstrstyling;

import junit.framework.TestCase;
import limelight.styles.abstrstyling.StyleValue;
import limelight.styles.abstrstyling.InvalidStyleAttributeError;

public class StyleAttributeCompilerTest extends TestCase
{
  private TestableStyleAttributeCompilerTest compiler;

  private static class TestableStyleAttributeCompilerTest extends StyleCompiler
  {
    public StyleValue compile(Object value)
    {
      return null;
    }
  }

  public void setUp() throws Exception
  {
    compiler = new TestableStyleAttributeCompilerTest();
  }
  
  public void testName() throws Exception
  {
    compiler.setName("My Attribute");
    assertEquals("My Attribute", compiler.getName());
  }

  public void testError() throws Exception
  {
    compiler.setName("My Attribute");
    InvalidStyleAttributeError error = compiler.makeError("blah");

    assertEquals("Invalid value 'blah' for My Attribute style attribute.", error.getMessage());
  }
}
