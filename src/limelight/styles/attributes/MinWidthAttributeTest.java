package limelight.styles.attributes;

import limelight.styles.compiling.DimensionAttributeCompiler;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MinWidthAttributeTest extends Assert
{
  private MinWidthAttribute attribute;

  @Before
  public void setUp() throws Exception
  {
    attribute = new MinWidthAttribute();
  }

  @Test
  public void shouldCreation() throws Exception
  {
    assertEquals("Min Width", attribute.getName());
    assertEquals("noneable simple dimension", attribute.getCompiler().type);
    assertEquals("none", attribute.getDefaultValue().toString());
  }
}
