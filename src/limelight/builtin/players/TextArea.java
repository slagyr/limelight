package limelight.builtin.players;

import limelight.ui.events.panel.PanelEvent;
import limelight.ui.model.PropPanel;
import limelight.ui.model.inputs.TextAreaPanel;

public class TextArea
{
  public void install(PanelEvent event)
  {
    TextAreaPanel input = new TextAreaPanel();
    final PropPanel panel = (PropPanel)event.getRecipient();
    panel.add(input);
  }
}
