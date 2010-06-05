package limelight.styles.attributes;

import org.junit.Before;
import org.junit.Test;

public class LeftMarginAttributeTest extends AbstractStyleAttributeTest
{
  @Before
  public void setUp() throws Exception
  {
    attribute = new LeftMarginAttribute();
  }

  @Test
  public void shouldCreation() throws Exception
  {
    assertEquals("Left Margin", attribute.getName());
    assertEquals("pixels", attribute.getCompiler().type);
    assertEquals("0", attribute.getDefaultValue().toString());
  }

  @Test
  public void shouldStyleChanged() throws Exception
  {
    checkInsetChange();
  }
}
