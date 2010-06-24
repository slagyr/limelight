package limelight.styles.attributes;

import org.junit.Before;
import org.junit.Test;

public class VerticalAlignmentAttributeTest extends AbstractStyleAttributeTest
{
  @Before
  public void setUp() throws Exception
  {
    attribute = new VerticalAlignmentAttribute();
  }

  @Test
  public void shouldCreation() throws Exception
  {
    assertEquals("Vertical Alignment", attribute.getName());
    assertEquals("vertical alignment", attribute.getCompiler().type);
    assertEquals("top", attribute.getDefaultValue().toString());
  }

  @Test
  public void shouldStyleChanged() throws Exception
  {
    checkAlignmentChange();
  }
}
