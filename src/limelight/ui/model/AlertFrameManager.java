//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model;

import limelight.Context;
import limelight.ui.events.stage.*;

import java.awt.event.WindowFocusListener;
import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.util.HashSet;
import java.util.ArrayList;

public class AlertFrameManager implements WindowFocusListener, WindowListener, WindowStateListener, FrameManager
{
  private PropFrame activeFrame;
  private final HashSet<PropFrame> frames;
  private PropFrame lastFrameAdded;

  public AlertFrameManager()
  {
    frames = new HashSet<PropFrame>();
  }

  public synchronized void watch(PropFrame frame)
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
    if(e.getSource() instanceof PropFrame)
      new StageGainedFocusEvent().dispatch((PropFrame)e.getSource());
  }

  public void windowLostFocus(WindowEvent e)
  {
    if(e.getSource() instanceof PropFrame)
      new StageLostFocusEvent().dispatch((PropFrame)e.getSource());
  }

  public void windowOpened(WindowEvent e)
  {
    if(e.getSource() instanceof PropFrame)
      new StageOpenedEvent().dispatch((PropFrame)e.getSource());
  }

  public void windowClosing(WindowEvent e)
  {
    PropFrame frame = ((PropFrame) e.getSource());
    if(frame.shouldAllowClose())
      frame.close();
  }

  public synchronized void windowClosed(WindowEvent e)
  {
    PropFrame frame = ((PropFrame) e.getSource());
    if(lastFrameAdded == frame)
      lastFrameAdded = null;
    if(activeFrame == frame)
      activeFrame = null;
    frames.remove(frame);
    if(frame.isVital() && !hasVisibleVitalFrame())
      Context.instance().attemptShutdown();
  }

  private synchronized boolean hasVisibleVitalFrame()
  {
    for(PropFrame frame : frames)
    {
      if(frame.isVital() && frame.isVisible())
        return true;
    }
    return false;
  }

  public void windowIconified(WindowEvent e)
  {
    if(e.getSource() instanceof PropFrame)
      new StageIconifiedEvent().dispatch((PropFrame)e.getSource());
  }

  public void windowDeiconified(WindowEvent e)
  {
    if(e.getSource() instanceof PropFrame)
      new StageDeiconifiedEvent().dispatch((PropFrame)e.getSource());
  }

  public void windowActivated(WindowEvent e)
  {
    if(e.getSource() instanceof PropFrame)
      activateFrame((PropFrame)e.getSource());
  }

  public void windowDeactivated(WindowEvent e)
  {
    if(e.getSource() instanceof PropFrame)
    {
      PropFrame propFrame = (PropFrame)e.getSource();
      if(propFrame == activeFrame)
      {
        activeFrame = null;
        new StageDeactivatedEvent().dispatch(propFrame);
      }
    }
  }

  public void windowStateChanged(WindowEvent e)
  {
  }

  public PropFrame getActiveFrame()
  {
    if(activeFrame == null && lastFrameAdded != null && lastFrameAdded.isVisible())
      activateFrame(lastFrameAdded);
    return activeFrame;
  }

  public boolean isWatching(PropFrame frame)
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

  public synchronized void closeAllFrames()
  {
    for(PropFrame frame : frames)
      frame.close();
  }

  // private //////////////////////////////////////////////

  private void activateFrame(PropFrame propFrame)
  {
    if(activeFrame == propFrame || !propFrame.isVisible())
      return;

    PropFrame previouslyActiveFrame = activeFrame;
    activeFrame = propFrame;
    if(previouslyActiveFrame != null)
      new StageDeactivatedEvent().dispatch(previouslyActiveFrame);

    new StageActivatedEvent().dispatch(propFrame);
  }

  public void getVisibleFrames(ArrayList<PropFrame> result)
  {
    for(PropFrame frame : frames)
    {
      if(frame.isVisible())
        result.add(frame);
    }
  }
}