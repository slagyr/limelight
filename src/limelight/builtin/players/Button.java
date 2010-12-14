package limelight.builtin.players;

import limelight.ui.events.panel.PanelEvent;
import limelight.ui.model.PropPanel;
import limelight.ui.model.inputs.ButtonPanel;

public class Button
{
  public void install(PanelEvent event)
  {
    ButtonPanel button = new ButtonPanel();
    final PropPanel panel = (PropPanel)event.getRecipient();
    panel.add(button);
  }
}
