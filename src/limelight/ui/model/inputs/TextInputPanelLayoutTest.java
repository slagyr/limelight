package limelight.ui.model.inputs;

import limelight.ui.MockPanel;
import limelight.ui.model.MockParentPanel;
import org.junit.Assert;
import org.junit.Test;

public class TextInputPanelLayoutTest extends Assert
{
  @Test
  public void shouldInstance() throws Exception
  {
    assertEquals(TextInputPanelLayout.class, TextInputPanelLayout.instance.getClass());
  }

  @Test
  public void shouldClearTextLayout() throws Exception
  {
    MockTextInputPanel panel = new MockTextInputPanel();
    MockParentPanel parent = new MockParentPanel();
    parent.add(panel);

    TextInputPanelLayout.instance.doLayout(panel);

    assertEquals(true, panel.mockModel.clearLayoutsCalled);
  }
}
