package limelight.styles.abstrstyling;

import limelight.util.FakeKeyword;
import org.junit.Test;
import static org.junit.Assert.*;

public class StyleCompilerTest
{
  
  @Test
  public void stringifyingObjects() throws Exception
  {
    assertEquals("foo", StyleCompiler.stringify("foo"));
    assertEquals("42", StyleCompiler.stringify(42));
    assertEquals("foo", StyleCompiler.stringify(":foo"));
    assertEquals("foo", StyleCompiler.stringify(new FakeKeyword("foo")));
  }
}
