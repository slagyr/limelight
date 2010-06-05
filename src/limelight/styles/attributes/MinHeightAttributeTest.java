package limelight.styles.attributes;

import limelight.styles.compiling.DimensionAttributeCompiler;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MinHeightAttributeTest extends Assert
{
  private MinHeightAttribute attribute;

  @Before
  public void setUp() throws Exception
  {
    attribute = new MinHeightAttribute();
  }

  @Test
  public void shouldCreation() throws Exception
  {
    assertEquals("Min Height", attribute.getName());
    assertEquals("noneable simple dimension", attribute.getCompiler().type);
    assertEquals("none", attribute.getDefaultValue().toString());
  }
}
