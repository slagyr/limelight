package limelight.ui.model;

import limelight.model.api.FakePropProxy;
import org.junit.Assert;
import org.junit.Test;

public class TempTextAccessorTest extends Assert
{
  @Test
  public void shouldReplaceItselfWithTextPanel() throws Exception
  {
    PropPanel panel = new PropPanel(new FakePropProxy());
    assertEquals(TempTextAccessor.instance(), panel.getTextAccessor());

    TempTextAccessor.instance().setText("Howdy", panel);

    TextAccessor newAccessor = panel.getTextAccessor();
    assertEquals(TextPanel.class, newAccessor.getClass());
    assertEquals("Howdy", newAccessor.getText());
  }
}
