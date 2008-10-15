package limelight.background;

import limelight.ui.Panel;
import limelight.ui.model.Frame;
import limelight.ui.model.PaintJob;
import limelight.ui.model.RootPanel;
import limelight.util.Box;
import limelight.util.NanoTimer;
import limelight.util.Debug;
import limelight.Context;

import java.awt.*;
import java.util.ArrayList;

public class PanelPainterLoop extends IdleThreadLoop
{
  private final ArrayList<Panel> panelBuffer = new ArrayList<Panel>(50);
  private final ArrayList<Rectangle> regionBuffer = new ArrayList<Rectangle>(50);
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
    RootPanel root = getActiveRoot();
    return root == null || nothingToDo(root);
  }

  private boolean nothingToDo(RootPanel root)
  {
    return !root.hasPanelsNeedingLayout() && !root.hasDirtyRegions();
  }

  protected void execute()
  {
//Debug.debug1.log("Paiting begun");
//Debug.debug2.mark();
//    timer.markTime();
    RootPanel root = getActiveRoot();
    if(root != null)
    {
//Debug.debug2.log("  about to layout");
      doAllLayouts(root);
//Debug.debug2.log("  laidout");
      paintDirtyRegions(root);
//Debug.debug2.log("  painted dirty regions");
    }
    lastExecutionDuration = timer.getIdleNanos();
//Debug.debug1.log("Paiting completed");
  }

  protected void delay()
  {
    timer.sleep(optimalDelayTimeNanos - lastExecutionDuration);
  }

  public RootPanel getActiveRoot()
  {
    Frame frame = Context.getActiveFrame();
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
//NanoTimer timer = new NanoTimer();
      PaintJob job = new PaintJob(new Box(rectangle));
//timer.log("paint job creates");
      job.paint(root.getPanel());
//timer.log("root painted");
      Graphics2D rootGraphics = root.getGraphics();
      job.applyTo(rootGraphics);
//timer.log("applied painting to graphics");
      job.dispose();
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
