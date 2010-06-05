package limelight.styles.attributes;

import org.junit.Before;
import org.junit.Test;

public class FontFaceAttributeTest extends AbstractStyleAttributeTest
{
  @Before
  public void setUp() throws Exception
  {
    attribute = new FontFaceAttribute();
  }

  @Test
  public void shouldCreation() throws Exception
  {
    assertEquals("Font Face", attribute.getName());
    assertEquals("string", attribute.getCompiler().type);
    assertEquals("Arial", attribute.getDefaultValue().toString());
  }

  @Test
  public void shouldApplyChanges() throws Exception
  {
    checkFontChange();
  }
}
