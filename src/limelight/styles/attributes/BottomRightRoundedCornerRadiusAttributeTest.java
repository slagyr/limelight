package limelight.styles.attributes;

import org.junit.Before;
import org.junit.Test;

public class BottomRightRoundedCornerRadiusAttributeTest extends AbstractStyleAttributeTest
{
  @Before
  public void setUp() throws Exception
  {
    attribute = new BottomRightRoundedCornerRadiusAttribute();
  }

  @Test
  public void shouldCreation() throws Exception
  {
    assertEquals("Bottom Right Rounded Corner Radius", attribute.getName());
    assertEquals("pixels", attribute.getCompiler().type);
    assertEquals("0", attribute.getDefaultValue().toString());
  }

  @Test
  public void shouldStyleChanged() throws Exception
  {
    checkBorderChange();
  }
}
