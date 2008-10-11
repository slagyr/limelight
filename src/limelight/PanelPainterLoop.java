package limelight;

import limelight.ui.Panel;
import limelight.ui.model.Frame;
import limelight.ui.model.PaintJob;
import limelight.ui.model.RootPanel;
import limelight.util.Box;
import limelight.util.NanoTimer;

import java.awt.*;
import java.util.ArrayList;

public class PanelPainterLoop extends IdleThreadLoop
{
  private ArrayList<Panel> panelBuffer = new ArrayList<Panel>(50);
  private ArrayList<Rectangle> regionBuffer = new ArrayList<Rectangle>(50);
  private int updatesPerSecond;
  private int optimalDelayTimeNanos;
  private NanoTimer timer;
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
    timer.markTime();
    RootPanel root = getActiveRoot();
    if(root != null)
    {
System.err.println("PanelPainter.execute!!!!");      
      doAllLayouts(root);
      paintDirtyRegions(root);
    }
    lastExecutionDuration = timer.getIdleNanos();
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
System.err.println("dirty region = " + rectangle);      
      if(rectangle.width <= 0 || rectangle.height <= 0)
        continue;
      
      PaintJob job = new PaintJob(new Box(rectangle));
      job.paint(root.getPanel());
      Graphics2D rootGraphics = root.getGraphics();
      job.applyTo(rootGraphics);
      job.dispose();
    }
  }

  public void doAllLayouts(RootPanel root)
  {
    panelBuffer.clear();
    root.getAndClearPanelsNeedingLayout(panelBuffer);
    for(limelight.ui.Panel panel : panelBuffer)
    {
System.err.println("layout = " + panel);      
      panel.doLayout();
    }
  }
  
  public PanelPainterLoop started()
  {
    start();
    return this;
  }
}
