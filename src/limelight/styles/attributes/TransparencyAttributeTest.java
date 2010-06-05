package limelight.styles.attributes;

import org.junit.Before;
import org.junit.Test;

public class TransparencyAttributeTest extends AbstractStyleAttributeTest
{
  @Before
  public void setUp() throws Exception
  {
    attribute = new TransparencyAttribute();
  }

  @Test
  public void shouldCreation() throws Exception
  {
    assertEquals("Transparency", attribute.getName());
    assertEquals("percentage", attribute.getCompiler().type);
    assertEquals("0%", attribute.getDefaultValue().toString());
  }
  
  @Test
  public void shouldApplyChanges() throws Exception
  {
    setUpPanel();

    attribute.applyChange(panel, null);

    assertNotNull(cache.retrieve(panel));
    assertEquals(true, panel.markedAsDirty);
  }
}
