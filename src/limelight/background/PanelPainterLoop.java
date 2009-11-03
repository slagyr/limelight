//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.background;

import limelight.ui.Panel;
import limelight.ui.model.PaintJob;
import limelight.ui.model.RootPanel;
import limelight.ui.model.PropFrame;
import limelight.util.Box;
import limelight.util.NanoTimer;
import limelight.Context;

import java.awt.*;
import java.util.ArrayList;

public class PanelPainterLoop extends IdleThreadLoop
{
  private final ArrayList<Panel> panelBuffer = new ArrayList<Panel>(50);
  private final ArrayList<Rectangle> regionBuffer = new ArrayList<Rectangle>(50);
  private final ArrayList<PropFrame> frameBuffer = new ArrayList<PropFrame>(5);
  private int updatesPerSecond;
  private int optimalDelayTimeNanos;
  private final NanoTimer timer;
  private long lastExecutionDuration;

  public PanelPainterLoop()
  {
    timer = new NanoTimer();
    setUpdatesPerSecond(30);
  }

  public void setUpdatesPerSecond(int value)
  {
    updatesPerSecond = value;
    optimalDelayTimeNanos = 1000000000 / updatesPerSecond;
  }

  public int getUpdatesPerSecond()
  {
    return updatesPerSecond;
  }

  public int getOptimalDelayTimeNanos()
  {
    return optimalDelayTimeNanos;
  }

  public boolean shouldBeIdle()
  {
    // load the frame buffer once... is valid for next execute invocation.
    frameBuffer.clear();
    if(Context.instance().frameManager == null)
      return true;
    else
    {
      Context.instance().frameManager.getVisibleFrames(frameBuffer);
      return frameBuffer.isEmpty() || nothingToDo();
    }
  }

  private boolean nothingToDo()
  {
    boolean somethingToDo = false;
    for(PropFrame stageFrame : frameBuffer)
    {
      RootPanel root = stageFrame.getRoot();
      if(root != null && (root.hasPanelsNeedingLayout() || root.hasDirtyRegions()))
      {
        somethingToDo = true;
        break;
      }
    }
    return !somethingToDo;
  }

  protected void execute()
  {
    for(PropFrame stageFrame : frameBuffer)
    {
      RootPanel root = stageFrame.getRoot();
      if(root != null)
      {
        doAllLayouts(root);
        paintDirtyRegions(root);
      }
    }
    lastExecutionDuration = timer.getIdleNanos();
  }

  protected void delay()
  {
    timer.sleep(optimalDelayTimeNanos - lastExecutionDuration);
  }

  public RootPanel getActiveRoot()
  {
    PropFrame frame = Context.getActiveFrame();
    if(frame != null)
      return frame.getRoot();
    else
      return null;
  }

  public void paintDirtyRegions(RootPanel root)
  {
    regionBuffer.clear();
    root.getAndClearDirtyRegions(regionBuffer);
    for(Rectangle rectangle : regionBuffer)
    {
      if(rectangle.width <= 0 || rectangle.height <= 0)
        continue;
      Graphics2D rootGraphics = root.getGraphics();
      if(rootGraphics != null)
      {
        PaintJob job = new PaintJob(new Box(rectangle), rootGraphics.getBackground());
        job.paint(root.getPanel());
        job.applyTo(rootGraphics);
        job.dispose();     
      }
    }
  }

  public void doAllLayouts(RootPanel root)
  {
    panelBuffer.clear();
    root.getAndClearPanelsNeedingLayout(panelBuffer);
    for(limelight.ui.Panel panel : panelBuffer)
    {     
      panel.doLayout();
    }
  }

  public PanelPainterLoop started()
  {
    start();
    return this;
  }
}
