//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.abstrstyling;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class StyleAttributeCompilerTest
{
  private TestableStyleAttributeCompiler compiler;

  private static class TestableStyleAttributeCompiler extends StyleCompiler
  {
    public StyleValue compile(Object value)
    {
      return null;
    }
  }

  @Before
  public void setUp() throws Exception
  {
    compiler = new TestableStyleAttributeCompiler();
  }

  @Test
  public void name() throws Exception
  {
    compiler.setName("My Attribute");
    assertEquals("My Attribute", compiler.getName());
  }

  @Test
  public void error() throws Exception
  {
    compiler.setName("My Attribute");
    InvalidStyleAttributeError error = compiler.makeError("blah");

    assertEquals("Invalid value 'blah' for My Attribute style attribute.", error.getMessage());
  }
}
