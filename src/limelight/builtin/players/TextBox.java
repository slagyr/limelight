package limelight.builtin.players;

import limelight.ui.events.panel.PanelEvent;
import limelight.ui.model.PropPanel;
import limelight.ui.model.inputs.TextBoxPanel;

public class TextBox
{
  public void install(PanelEvent event)
  {
    TextBoxPanel textbox = new TextBoxPanel();
    final PropPanel panel = (PropPanel)event.getRecipient();
    panel.add(textbox);
  }
}
