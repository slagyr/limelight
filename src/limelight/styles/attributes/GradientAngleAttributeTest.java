package limelight.styles.attributes;

import limelight.styles.compiling.DimensionAttributeCompiler;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class GradientAngleAttributeTest extends Assert
{
  private GradientAngleAttribute attribute;

  @Before
  public void setUp() throws Exception
  {
    attribute = new GradientAngleAttribute();
  }

  @Test
  public void shouldCreation() throws Exception
  {
    assertEquals("Gradient Angle", attribute.getName());
    assertEquals("degrees", attribute.getCompiler().type);
    assertEquals("90", attribute.getDefaultValue().toString());
  }
}
