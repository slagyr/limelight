package limelight.builtin.players;

import limelight.ui.events.panel.PanelEvent;
import limelight.ui.model.PropPanel;
import limelight.ui.model.inputs.CheckBoxPanel;

public class CheckBox
{
  private PropPanel propPanel;
  private CheckBoxPanel checkBoxPanel;

  public void install(PanelEvent event)
  {
    checkBoxPanel = new CheckBoxPanel();
    propPanel = (PropPanel)event.getRecipient();
    propPanel.add(checkBoxPanel);
  }

  public PropPanel getPropPanel()
  {
    return propPanel;
  }

  public CheckBoxPanel getCheckBoxPanel()
  {
    return checkBoxPanel;
  }

  public boolean isChecked()
  {
    return checkBoxPanel.isSelected();
  }

  public boolean isSelected()
  {
    return checkBoxPanel.isSelected();
  }

  public void setChecked(boolean b)
  {
    checkBoxPanel.setSelected(b);
  }

  public void setSelected(boolean b)
  {
    checkBoxPanel.setSelected(b);
  }
}
