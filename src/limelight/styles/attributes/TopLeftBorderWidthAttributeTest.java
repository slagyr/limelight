package limelight.styles.attributes;

import org.junit.Before;
import org.junit.Test;

public class TopLeftBorderWidthAttributeTest extends AbstractStyleAttributeTest
{
  @Before
  public void setUp() throws Exception
  {
    attribute = new TopLeftBorderWidthAttribute();
  }

  @Test
  public void shouldCreation() throws Exception
  {
    assertEquals("Top Left Border Width", attribute.getName());
    assertEquals("pixels", attribute.getCompiler().type);
    assertEquals("0", attribute.getDefaultValue().toString());
  }

  @Test
  public void shouldStyleChanged() throws Exception
  {
    checkBorderChange();
  }
}
