package limelight.styles.attributes;

import limelight.styles.compiling.DimensionAttributeCompiler;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class VerticalScrollbarAttributeTest extends Assert
{
  private VerticalScrollbarAttribute attribute;

  @Before
  public void setUp() throws Exception
  {
    attribute = new VerticalScrollbarAttribute();
  }

  @Test
  public void shouldCreation() throws Exception
  {
    assertEquals("Vertical Scrollbar", attribute.getName());
    assertEquals("on/off", attribute.getCompiler().type);
    assertEquals("off", attribute.getDefaultValue().toString());
  }
}
