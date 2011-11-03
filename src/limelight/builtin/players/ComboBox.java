package limelight.builtin.players;

import limelight.ui.events.panel.PanelEvent;
import limelight.ui.model.PropPanel;
import limelight.ui.model.inputs.ComboBoxPanel;

public class ComboBox
{
  public void install(PanelEvent event)
  {
    ComboBoxPanel input = new ComboBoxPanel();
    final PropPanel panel = (PropPanel)event.getRecipient();
    panel.add(input);
  }
}
