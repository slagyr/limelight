package limelight.builtin.players;

import limelight.ui.events.panel.PanelEvent;
import limelight.ui.model.PropPanel;
import limelight.ui.model.inputs.RadioButtonPanel;

public class RadioButton
{
  public void install(PanelEvent event)
  {
    RadioButtonPanel input = new RadioButtonPanel();
    final PropPanel panel = (PropPanel)event.getRecipient();
    panel.add(input);
  }
}
