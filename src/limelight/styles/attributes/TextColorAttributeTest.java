package limelight.styles.attributes;

import limelight.ui.model.MockTextAccessor;
import org.junit.Before;
import org.junit.Test;

public class TextColorAttributeTest extends AbstractStyleAttributeTest
{
  @Before
  public void setUp() throws Exception
  {
    attribute = new TextColorAttribute();
  }

  @Test
  public void shouldCreation() throws Exception
  {
    assertEquals("Text Color", attribute.getName());
    assertEquals("color", attribute.getCompiler().type);
    assertEquals("#000000ff", attribute.getDefaultValue().toString());
  }

  @Test
  public void shouldApplyChanges() throws Exception
  {
    setUpPanel();
    MockTextAccessor accessor = new MockTextAccessor();
    panel.textAccessor = accessor;

    attribute.applyChange(panel, null);

    assertEquals(true, accessor.markAsDirtyCalled);
  }
}
