package limelight.styles.attributes;

import org.junit.Before;
import org.junit.Test;

public class YAttributeTest extends AbstractStyleAttributeTest
{
  @Before
  public void setUp() throws Exception
  {
    attribute = new YAttribute();
  }

  @Test
  public void shouldCreation() throws Exception
  {
    assertEquals("Y", attribute.getName());
    assertEquals("y-coordinate", attribute.getCompiler().type);
    assertEquals("0", attribute.getDefaultValue().toString());
  }

  @Test
  public void shouldStyleChanged() throws Exception
  {
    checkCoordinateChange();
  }
}
