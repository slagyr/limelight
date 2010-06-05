package limelight.styles.attributes;

import org.junit.Before;
import org.junit.Test;

public class TopLeftRoundedCornerRadiusAttributeTest extends AbstractStyleAttributeTest
{
  @Before
  public void setUp() throws Exception
  {
    attribute = new TopLeftRoundedCornerRadiusAttribute();
  }

  @Test
  public void shouldCreation() throws Exception
  {
    assertEquals("Top Left Rounded Corner Radius", attribute.getName());
    assertEquals("pixels", attribute.getCompiler().type);
    assertEquals("0", attribute.getDefaultValue().toString());
  }

  @Test
  public void shouldStyleChanged() throws Exception
  {
    checkBorderChange();
  }
}
