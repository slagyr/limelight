//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.ui.model;

import limelight.Context;
import limelight.ui.events.stage.*;

import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;
import java.util.ArrayList;
import java.util.HashSet;

public class AlertFrameManager implements WindowFocusListener, WindowListener, WindowStateListener, FrameManager
{
  private StageFrame activeFrame;
  private final HashSet<StageFrame> frames;
  private StageFrame lastFrameAdded;
  private Context context;

  public AlertFrameManager(Context context)
  {
    this.context = context; // MDM - Store context locally, otherwise race conditions may ensue. (old AlertFrameManager calls shutdown on newly created context... observed in tests)
    frames = new HashSet<StageFrame>();
  }

  public synchronized void watch(StageFrame frame)
  {
    if(!frames.contains(frame))
    {
      frame.addWindowStateListener(this);
      frame.addWindowFocusListener(this);
      frame.addWindowListener(this);
      frames.add(frame);
      lastFrameAdded = frame;
    }
  }

  public void windowGainedFocus(WindowEvent e)
  {
    if(e.getSource() instanceof StageFrame)
      new StageGainedFocusEvent().dispatch(((StageFrame) e.getSource()).getStage());
  }

  public void windowLostFocus(WindowEvent e)
  {
    if(e.getSource() instanceof StageFrame)
      new StageLostFocusEvent().dispatch(((StageFrame) e.getSource()).getStage());
  }

  public void windowOpened(WindowEvent e)
  {
    if(e.getSource() instanceof StageFrame)
      new StageOpenedEvent().dispatch(((StageFrame) e.getSource()).getStage());
  }

  public void windowClosing(WindowEvent e)
  {
    StageFrame frame = ((StageFrame) e.getSource());
    if(frame.getStage().shouldAllowClose())
      frame.getStage().close();
  }

  public synchronized void windowClosed(WindowEvent e)
  {
    StageFrame frame = ((StageFrame) e.getSource());
    if(lastFrameAdded == frame)
      lastFrameAdded = null;
    if(activeFrame == frame)
      activeFrame = null;
    frames.remove(frame);
    if(frame.getStage().isVital() && !hasVisibleVitalFrame())
      context.attemptShutdown();
  }

  private synchronized boolean hasVisibleVitalFrame()
  {
    for(StageFrame frame : frames)
    {
      if(frame.getStage().isVital() && frame.isVisible())
        return true;
    }
    return false;
  }

  public void windowIconified(WindowEvent e)
  {
    if(e.getSource() instanceof StageFrame)
      new StageIconifiedEvent().dispatch(((StageFrame) e.getSource()).getStage());
  }

  public void windowDeiconified(WindowEvent e)
  {
    if(e.getSource() instanceof StageFrame)
      new StageDeiconifiedEvent().dispatch(((StageFrame) e.getSource()).getStage());
  }

  public void windowActivated(WindowEvent e)
  {
    if(e.getSource() instanceof StageFrame)
      activateFrame((StageFrame) e.getSource());
  }

  public void windowDeactivated(WindowEvent e)
  {
    if(e.getSource() instanceof StageFrame)
    {
      StageFrame frame = (StageFrame) e.getSource();
      if(frame == activeFrame)
      {
        activeFrame = null;
        new StageDeactivatedEvent().dispatch(frame.getStage());
      }
    }
  }

  public void windowStateChanged(WindowEvent e)
  {
  }

  public StageFrame getActiveFrame()
  {
    if(activeFrame == null && lastFrameAdded != null && lastFrameAdded.getStage().isVisible())
      activateFrame(lastFrameAdded);
    return activeFrame;
  }

  public boolean isWatching(StageFrame frame)
  {
    boolean found = false;
    for(WindowFocusListener listener : frame.getWindowFocusListeners())
    {
      if(listener == this)
        found = true;
    }
    return found;
  }

  public synchronized int getFrameCount()
  {
    return frames.size();
  }

  public void closeAllFrames()
  {
    ArrayList<StageFrame> stageFrames;
    synchronized(this)
    {
      stageFrames = new ArrayList<StageFrame>(frames);
    }
    for(StageFrame frame : stageFrames)
      frame.getStage().close();
  }

  // private //////////////////////////////////////////////

  private void activateFrame(StageFrame stageFrame)
  {
    if(activeFrame == stageFrame || !stageFrame.getStage().isVisible())
      return;

    StageFrame previouslyActiveFrame = activeFrame;
    activeFrame = stageFrame;
    if(previouslyActiveFrame != null)
      new StageDeactivatedEvent().dispatch(previouslyActiveFrame.getStage());

    new StageActivatedEvent().dispatch(stageFrame.getStage());
    Context.kickPainter();
  }

  public void getVisibleFrames(ArrayList<StageFrame> result)
  {
    for(StageFrame frame : frames)
    {
      if(frame.getStage().isVisible())
        result.add(frame);
    }
  }
}