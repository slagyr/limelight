package limelight.builtin.players;

import limelight.ui.events.panel.PanelEvent;
import limelight.ui.model.PropPanel;
import limelight.ui.model.inputs.DropDownPanel;

import java.util.Collection;
import java.util.List;

public class DropDown
{
  private PropPanel propPanel;
  private DropDownPanel dropDownPanel;

  public void install(PanelEvent event)
  {
    dropDownPanel = new DropDownPanel();
    propPanel = (PropPanel)event.getRecipient();
    propPanel.add(dropDownPanel);
    propPanel.getBackstage().put("drop-down", this);
  }

  public PropPanel getPropPanel()
  {
    return propPanel;
  }

  public DropDownPanel getDropDownPanel()
  {
    return dropDownPanel;
  }

  public void setChoices(Collection<?> choices)
  {
    dropDownPanel.setChoices(choices);
  }

  public List<?> getChoices()
  {
    return dropDownPanel.getChoices();
  }

  public void setValue(Object value)
  {
    dropDownPanel.setSelectedChoice(value);
  }

  public Object getValue()
  {
    return dropDownPanel.getSelectedChoice();
  }
}
