package limelight.builtin.players;

import limelight.ui.events.panel.PanelEvent;
import limelight.ui.model.PropPanel;
import limelight.ui.model.inputs.CheckBoxPanel;

public class CheckBox
{
  public void install(PanelEvent event)
  {
    CheckBoxPanel input = new CheckBoxPanel();
    final PropPanel panel = (PropPanel)event.getRecipient();
    panel.add(input);
  }
}
