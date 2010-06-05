package limelight.styles.attributes;

import limelight.styles.compiling.DimensionAttributeCompiler;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class HorizontalScrollbarAttributeTest extends Assert
{
  private HorizontalScrollbarAttribute attribute;

  @Before
  public void setUp() throws Exception
  {
    attribute = new HorizontalScrollbarAttribute();
  }

  @Test
  public void shouldCreation() throws Exception
  {
    assertEquals("Horizontal Scrollbar", attribute.getName());
    assertEquals("on/off", attribute.getCompiler().type);
    assertEquals("off", attribute.getDefaultValue().toString());
  }
}
