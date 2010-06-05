package limelight.styles.attributes;

import limelight.styles.compiling.DimensionAttributeCompiler;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MaxHeightAttributeTest extends Assert
{
  private MaxHeightAttribute attribute;

  @Before
  public void setUp() throws Exception
  {
    attribute = new MaxHeightAttribute();
  }

  @Test
  public void shouldCreation() throws Exception
  {
    assertEquals("Max Height", attribute.getName());
    assertEquals("noneable simple dimension", attribute.getCompiler().type);
    assertEquals("none", attribute.getDefaultValue().toString());
  }
}
