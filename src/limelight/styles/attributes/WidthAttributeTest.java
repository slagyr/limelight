package limelight.styles.attributes;

import org.junit.Before;
import org.junit.Test;

public class WidthAttributeTest extends AbstractStyleAttributeTest
{
  @Before
  public void setUp() throws Exception
  {
    attribute = new WidthAttribute();
  }

  @Test
  public void shouldCreation() throws Exception
  {
    assertEquals("Width", attribute.getName());
    assertEquals("dimension", attribute.getCompiler().type);
    assertEquals("auto", attribute.getDefaultValue().toString());
  }

  @Test
  public void shouldStyleChanged() throws Exception
  {
    checkDimensionChange();
  }
}
