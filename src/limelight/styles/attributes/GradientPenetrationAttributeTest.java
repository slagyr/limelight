package limelight.styles.attributes;

import limelight.styles.compiling.DimensionAttributeCompiler;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class GradientPenetrationAttributeTest extends Assert
{
  private GradientPenetrationAttribute attribute;

  @Before
  public void setUp() throws Exception
  {
    attribute = new GradientPenetrationAttribute();
  }

  @Test
  public void shouldCreation() throws Exception
  {
    assertEquals("Gradient Penetration", attribute.getName());
    assertEquals("percentage", attribute.getCompiler().type);
    assertEquals("100%", attribute.getDefaultValue().toString());
  }
}
