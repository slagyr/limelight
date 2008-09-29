package limelight.styles.styling;

import junit.framework.TestCase;
import limelight.styles.InvalidStyleAttributeError;

public class DimensionAttributeCompilerTest extends TestCase
{
  private DimensionAttributeCompiler compiler;

  public void setUp() throws Exception
  {
    compiler = new DimensionAttributeCompiler();
    compiler.setName("dimension");
  }

  public void testValidValue() throws Exception
  {
    assertEquals(StaticDimensionAttribute.class, compiler.compile("123").getClass());
    assertEquals(PercentageDimensionAttribute.class, compiler.compile("50%").getClass());
    assertEquals(AutoDimensionAttribute.class, compiler.compile("auto").getClass());


    assertEquals(123, ((StaticDimensionAttribute)compiler.compile("123")).getPixels());
    assertEquals(0, ((StaticDimensionAttribute)compiler.compile("0")).getPixels());
    assertEquals(50, ((PercentageDimensionAttribute)compiler.compile("50%")).getPercentage());
  }

  public void testInvalidValues() throws Exception
  {
    checkForError("-1");
    checkForError("200%");
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
      assertEquals("Invalid value '" + value + "' for dimension style attribute.", e.getMessage());
    }
  }
}
