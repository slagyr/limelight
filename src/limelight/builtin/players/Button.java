//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.builtin.players;

import limelight.ui.events.panel.PanelEvent;
import limelight.ui.model.PropPanel;
import limelight.ui.model.inputs.ButtonPanel;

public class Button
{
  private PropPanel propPanel;
  private ButtonPanel buttonPanel;

  public void install(PanelEvent event)
  {
    buttonPanel = new ButtonPanel();
    propPanel = (PropPanel)event.getRecipient();
    propPanel.add(buttonPanel);
    propPanel.getStagehands().put("button", this);
  }

  public PropPanel getPropPanel()
  {
    return propPanel;
  }

  public ButtonPanel getButtonPanel()
  {
    return buttonPanel;
  }
}
