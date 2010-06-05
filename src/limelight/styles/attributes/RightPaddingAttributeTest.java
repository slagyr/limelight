package limelight.styles.attributes;

import org.junit.Before;
import org.junit.Test;

public class RightPaddingAttributeTest extends AbstractStyleAttributeTest
{
  @Before
  public void setUp() throws Exception
  {
    attribute = new RightPaddingAttribute();
  }

  @Test
  public void shouldCreation() throws Exception
  {
    assertEquals("Right Padding", attribute.getName());
    assertEquals("pixels", attribute.getCompiler().type);
    assertEquals("0", attribute.getDefaultValue().toString());
  }

  @Test
  public void shouldStyleChanged() throws Exception
  {
    checkInsetChange();
  }
}
