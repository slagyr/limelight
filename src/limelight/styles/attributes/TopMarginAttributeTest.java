package limelight.styles.attributes;

import org.junit.Before;
import org.junit.Test;

public class TopMarginAttributeTest extends AbstractStyleAttributeTest
{
  @Before
  public void setUp() throws Exception
  {
    attribute = new TopMarginAttribute();
  }

  @Test
  public void shouldCreation() throws Exception
  {
    assertEquals("Top Margin", attribute.getName());
    assertEquals("pixels", attribute.getCompiler().type);
    assertEquals("0", attribute.getDefaultValue().toString());
  }

  @Test
  public void shouldStyleChanged() throws Exception
  {
    checkInsetChange();
  }
}
