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
      Frame frame = Context.instance().frameManager.getActiveFrame();
      if(frame != null && frame.getRoot() != null)
        frame.getRoot().repaintChangedPanels();
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
  }
}
