package limelight.styles.attributes;

import limelight.styles.compiling.DimensionAttributeCompiler;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TopBorderColorAttributeTest extends Assert
{
  private TopBorderColorAttribute attribute;

  @Before
  public void setUp() throws Exception
  {
    attribute = new TopBorderColorAttribute();
  }

  @Test
  public void shouldCreation() throws Exception
  {
    assertEquals("Top Border Color", attribute.getName());
    assertEquals("color", attribute.getCompiler().type);
    assertEquals("#000000ff", attribute.getDefaultValue().toString());
  }
}
