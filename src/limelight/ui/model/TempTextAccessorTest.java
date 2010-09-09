package limelight.ui.model;

import limelight.ui.api.MockProp;
import org.junit.Assert;
import org.junit.Test;

public class TempTextAccessorTest extends Assert
{
  @Test
  public void shouldReplaceItselfWithTextPanel() throws Exception
  {
    PropPanel panel = new PropPanel(new MockProp());
    assertEquals(TempTextAccessor.instance(), panel.getTextAccessor());

    TempTextAccessor.instance().setText("Howdy", panel);

    TextAccessor newAccessor = panel.getTextAccessor();
    assertEquals(TextPanel.class, newAccessor.getClass());
    assertEquals("Howdy", newAccessor.getText());
  }
}
