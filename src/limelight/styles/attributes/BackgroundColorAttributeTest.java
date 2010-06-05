package limelight.styles.attributes;

import limelight.styles.compiling.DimensionAttributeCompiler;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BackgroundColorAttributeTest extends Assert
{
  private BackgroundColorAttribute attribute;

  @Before
  public void setUp() throws Exception
  {
    attribute = new BackgroundColorAttribute();
  }

  @Test
  public void shouldCreation() throws Exception
  {
    assertEquals("Background Color", attribute.getName());
    assertEquals("color", attribute.getCompiler().type);
    assertEquals("#00000000", attribute.getDefaultValue().toString());
  }
}
