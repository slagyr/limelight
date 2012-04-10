package limelight.builtin.players;

import limelight.ui.events.panel.PanelEvent;
import limelight.ui.model.PropPanel;
import limelight.ui.model.inputs.TextBoxPanel;

public class TextBox
{

  public TextBoxPanel textBoxPanel;
  public PropPanel propPanel;

  public void install(PanelEvent event)
  {
    textBoxPanel = new TextBoxPanel();
    propPanel = (PropPanel)event.getRecipient();
    propPanel.add(textBoxPanel);
    propPanel.getBackstage().put("text-box", this);
  }

  public PropPanel getPropPanel()
  {
    return propPanel;
  }

  public TextBoxPanel getTextBoxPanel()
  {
    return textBoxPanel;
  }

  public boolean isPassword()
  {
    return textBoxPanel.isInPasswordMode();
  }

  public void setPassword(boolean b)
  {
    textBoxPanel.setInPasswordMode(b);
  }
}
