//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.builtin.players;

import limelight.ui.events.panel.PanelEvent;
import limelight.ui.model.PropPanel;
import limelight.ui.model.inputs.ButtonPanel;

public class Button
{
  public void install(PanelEvent event)
  {
    ButtonPanel button = new ButtonPanel();
    final PropPanel panel = (PropPanel)event.getRecipient();
    panel.add(button);
  }
}
