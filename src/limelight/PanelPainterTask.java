//- Copyright 2008 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight;

import limelight.task.RecurringTask;
import limelight.ui.Panel;
import limelight.ui.model.Frame;
import limelight.ui.model.PaintJob;
import limelight.ui.model.RootPanel;
import limelight.util.Box;

import java.awt.*;
import java.util.ArrayList;

class PanelPainterTask extends RecurringTask
{

  private ArrayList<Panel> panelBuffer = new ArrayList<Panel>(50);
  private ArrayList<Rectangle> regionBuffer = new ArrayList<Rectangle>(50);

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
      {
        doAllLayout(frame.getRoot());
        paintDirtyRegions(frame.getRoot());
      }
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
  }

  private void paintDirtyRegions(RootPanel root)
  {
    regionBuffer.clear();
    root.getAndClearDirtyRegions(regionBuffer);
    for(Rectangle rectangle : regionBuffer)
    {
      if(rectangle.width <= 0 || rectangle.height <= 0)
        return;
      PaintJob job = new PaintJob(new Box(rectangle));

      job.paint(root.getPanel());
      Graphics2D rootGraphics = root.getGraphics();
      job.applyTo(rootGraphics);
      job.dispose();
    }
  }

  private void doAllLayout(RootPanel root)
  {
    panelBuffer.clear();
    root.getAndClearPanelsNeedingLayout(panelBuffer);
    for(Panel panel : panelBuffer)
      panel.doLayout();
  }
}
