//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs;

import limelight.ui.EventAction;
import limelight.ui.events.*;
import limelight.ui.model.BasePanel;

public abstract class InputPanel extends BasePanel
{
  protected InputPanel()
  {
    getEventHandler().add(FocusGainedEvent.class, MakeDirtyAction.instance);
    getEventHandler().add(FocusLostEvent.class, MakeDirtyAction.instance);
  }

  public boolean hasFocus()
  {
    return getRoot().getKeyListener().getFocusedPanel() == this;
  }

  private static class MakeDirtyAction implements EventAction
  {
    private static MakeDirtyAction instance = new MakeDirtyAction();

    public void invoke(Event event)
    {
      event.getPanel().markAsDirty();
    }
  }
}
