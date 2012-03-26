//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.background;

import limelight.Context;
import limelight.Log;
import limelight.ui.Panel;
import limelight.ui.model.*;
import limelight.util.Box;
import limelight.util.NanoTimer;

import java.awt.*;
import java.util.*;
import java.util.concurrent.locks.Lock;

public class PanelPainterLoop extends IdleThreadLoop
{
  private final HashMap<Panel, Layout> panelBuffer = new HashMap<Panel, Layout>(47);
  private final ArrayList<Rectangle> regionBuffer = new ArrayList<Rectangle>(50);
  private final ArrayList<StageFrame> frameBuffer = new ArrayList<StageFrame>(5);
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

    for(StageFrame stageFrame : frameBuffer)
    {
      Scene root = stageFrame.getStage().getScene();
//      if(root != null && (root.hasPanelsNeedingLayout() || root.hasDirtyRegions()))
      if(root != null && (root.isLayoutRequired() || root.hasDirtyRegions()))
      {
        somethingToDo = true;
        break;
      }
    }
    return !somethingToDo;
  }

  protected void execute()
  {
    for(StageFrame stageFrame : frameBuffer)
    {
      Scene root = stageFrame.getStage().getScene();
      if(root != null)
      {
        final Lock lock = root.getLock();
        try
        {
          lock.lock();
          doAllLayouts(root);
          paintDirtyRegions(root);
        }
        finally
        {
          lock.unlock();
        }
      }
    }
    lastExecutionDuration = timer.getIdleNanos();
  }

  protected void delay()
  {
    timer.sleep(optimalDelayTimeNanos - lastExecutionDuration);
  }

  public Scene getActiveRoot()
  {
    StageFrame frame = Context.getActiveFrame();
    if(frame != null)
      return frame.getStage().getScene();
    else
      return null;
  }

  public void paintDirtyRegions(Scene root)
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
        Log.debug("painting rectangle = " + rectangle);
        doPaintJob(root, new Box(rectangle), rootGraphics);
      }
    }
  }

  public static void doPaintJob(Panel panel, Box area, Graphics2D graphics2D)
  {
    PaintJob job = new PaintJob(area, graphics2D.getBackground());
    job.paint(panel);
    job.applyTo(graphics2D);
    job.dispose();
  }

  public void doAllLayouts(Scene root)
  {
Log.debug("root.isLayoutRequired() = " + root.isLayoutRequired() + " " + this);
    if(root.isLayoutRequired())
    {
      Log.debug("doing layout on = " + root);
      root.resetLayoutRequired();
      LayoutJob.layoutPanel(root);
    }
    //////////////////////////////////
//    panelBuffer.clear();
//    root.getAndClearPanelsNeedingLayout(panelBuffer);
//    Log.debug(this + " " + root + " doing layout on panels: " + panelBuffer.size());
//    LayoutJob.go(panelBuffer);
    ///////////////////////////////////
//    final ArrayList<Panel> orderedPanels = new ArrayList<Panel>(panelBuffer.keySet());
//    Collections.sort(orderedPanels, new Comparator<Panel>(){
//      public int compare(Panel panel, Panel panel1)
//      {
//        return depthOf(panel).compareTo(depthOf(panel1));
//      }
//    });
////    Collections.reverse(orderedPanels);
//    final ArrayList<Panel> finishedPanels = new ArrayList<Panel>();
//    for(Panel panel : orderedPanels)
//    {
//      if(!ancestorFinished(panel, finishedPanels))
//      {
//        final Layout layout = panelBuffer.get(panel);
//        Log.debug("root level layout: " + panel + " " + layout);
//        layout.doLayout(panel, panelBuffer);
//      }
//      else
//        Log.debug("skipping layout of panel = " + panel);
//      finishedPanels.add(panel);
//    }
  }
//
//  private Integer depthOf(Panel panel)
//  {
//    if(panel == null)
//      return -1;
//    else
//      return depthOf(panel, 0);
//  }
//
//  private int depthOf(Panel panel, int depth)
//  {
//    if(panel.getParent() == null)
//      return depth;
//    else
//      return depthOf(panel.getParent(), depth + 1);
//  }
//
//  private boolean ancestorFinished(Panel panel, ArrayList<Panel> finishedPanels)
//  {
//    for(Panel ancestor : finishedPanels)
//    {
//      if(panel.isDescendantOf(ancestor))
//        return true;
//    }
//    return false;
//  }

  public PanelPainterLoop started()
  {
    start();
    return this;
  }
}
