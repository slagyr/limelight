//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

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
    button.setButtonGroup(this);
    if(button.isSelected())
      buttonSelected(button);
  }

  public synchronized void remove(RadioButtonGroupMember button)
  {
    buttons.remove(button);
    button.setButtonGroup(null);
    if(selected == button)
      selected = null;
  }

  public synchronized void buttonSelected(RadioButtonGroupMember selected)
  {
    for(RadioButtonGroupMember button : buttons)
    {
      if(button != selected)
        button.setSelected(false);
    }
    this.selected = selected;
  }

  public ArrayList<RadioButtonGroupMember> getButtons()
  {
    return buttons;
  }

  public RadioButtonGroupMember getSelection()
  {
    return selected;
  }
}
