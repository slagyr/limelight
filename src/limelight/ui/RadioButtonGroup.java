package limelight.ui;

import java.util.ArrayList;

public class RadioButtonGroup
{
  private ArrayList<RadioButtonGroupMember> buttons = new ArrayList<RadioButtonGroupMember>();
  private RadioButtonGroupMember selected;

  public void add(RadioButtonGroupMember button)
  {
    if(!buttons.contains(button))
      buttons.add(button);
    button.setGroup(this);
    if(button.isSelected())
      buttonSelected(button);
  }

  public void buttonSelected(RadioButtonGroupMember selected)
  {
    for(RadioButtonGroupMember button : buttons)
    {
      if(button != selected)
        button.setSelected(false);

    }
  }
}
