//- Copyright 2008 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight;

import limelight.task.RecurringTask;
import limelight.ui.model.Frame;

class PanelPainterTask extends RecurringTask
{
  public PanelPainterTask()
  {
    super("Panel Painter", 80);
  }

  protected void doPerform()
  {
    try
    {
      Frame frame = Context.getActiveFrame();
      if(frame != null && frame.getRoot() != null)
        frame.getRoot().repaintChangedPanels();
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
  }
}
