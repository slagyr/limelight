package limelight.builtin.players;

import limelight.ui.RadioButtonGroup;
import limelight.ui.events.panel.PanelEvent;
import limelight.ui.model.PropPanel;
import limelight.ui.model.inputs.RadioButtonPanel;

public class RadioButton
{

  private PropPanel propPanel;
  private RadioButtonPanel buttonPanel;

  public void install(PanelEvent event)
  {
    buttonPanel = new RadioButtonPanel();
    propPanel = (PropPanel)event.getRecipient();
    propPanel.add(buttonPanel);
    propPanel.getStagehands().put("radio-button", this);
  }

  public PropPanel getPropPanel()
  {
    return propPanel;
  }

  public RadioButtonPanel getButtonPanel()
  {
    return buttonPanel;
  }

  public boolean isSelected()
  {
    return buttonPanel.isSelected();
  }

  public void setSelected(boolean b)
  {
    buttonPanel.setSelected(b);
  }

  public void select()
  {
    buttonPanel.setSelected(true);
  }

  public String getGroup()
  {
    return buttonPanel.getGroup();
  }

  public void setGroup(String group)
  {
    buttonPanel.setGroup(group);
  }
}
