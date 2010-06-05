package limelight.styles.attributes;

import org.junit.Before;
import org.junit.Test;

public class XAttributeTest extends AbstractStyleAttributeTest
{
  @Before
  public void setUp() throws Exception
  {
    attribute = new XAttribute();
  }

  @Test
  public void shouldCreation() throws Exception
  {
    assertEquals("X", attribute.getName());
    assertEquals("x-coordinate", attribute.getCompiler().type);
    assertEquals("0", attribute.getDefaultValue().toString());
  }

  @Test
  public void shouldStyleChanged() throws Exception
  {
    checkCoordinateChange();
  }
}
