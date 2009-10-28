//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model;

import limelight.Context;

import java.awt.event.WindowFocusListener;
import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.awt.*;
import java.util.HashSet;
import java.util.ArrayList;

public class AlertFrameManager implements WindowFocusListener, WindowListener, WindowStateListener, FrameManager
{
  private StageFrame focusedFrame;
  private final HashSet<StageFrame> frames;
  private StageFrame lastFrameAdded;

  public AlertFrameManager()
  {
    frames = new HashSet<StageFrame>();
  }

  public void windowGainedFocus(WindowEvent e)
  {
  }

  public void windowLostFocus(WindowEvent e)
  {
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

  public void windowOpened(WindowEvent e)
  {
  }

  public void windowClosing(WindowEvent e)
  {
    StageFrame frame = (StageFrame) e.getWindow();
    if(frame.shouldAllowClose())
      frame.close(e);
  }

  public synchronized void windowClosed(WindowEvent e)
  {
    StageFrame frame = ((StageFrame) e.getWindow());
    if(lastFrameAdded == frame)
      lastFrameAdded = null;
    if(focusedFrame == frame)
      focusedFrame = null;
    if(Context.instance().keyboardFocusManager != null)
      Context.instance().keyboardFocusManager.releaseFrame(frame);
    frames.remove(frame);
    frame.closed(e);
    if(frame.isVital() && !hasVisibleVitalFrame())
      Context.instance().attemptShutdown();
  }

  private synchronized boolean hasVisibleVitalFrame()
  {
    for(StageFrame frame : frames)
    {
      if(frame.isVital() && frame.isVisible())
        return true;
    }
    return false;
  }

  public void windowIconified(WindowEvent e)
  {
    ((StageFrame)e.getWindow()).iconified(e);
  }

  public void windowDeiconified(WindowEvent e)
  {
    ((StageFrame)e.getWindow()).deiconified(e);
  }

  public void windowActivated(WindowEvent e)
  {
    Window window = e.getWindow();
    if(window instanceof StageFrame)
    {
      activateFrame(e);
    }
  }

  public void windowDeactivated(WindowEvent e)
  {
    ((StageFrame)e.getWindow()).deactivated(e);
  }

  public void windowStateChanged(WindowEvent e)
  {
  }

  public StageFrame getFocusedFrame()
  {
    if(focusedFrame == null && lastFrameAdded != null && lastFrameAdded.isVisible())
      activateFrame(new WindowEvent(lastFrameAdded, WindowEvent.WINDOW_ACTIVATED));
    return focusedFrame;
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

  public synchronized void closeAllFrames()
  {
    for(StageFrame frame : frames)
      frame.close(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
  }

  // private //////////////////////////////////////////////

  private void activateFrame(WindowEvent e)
  {
    focusedFrame = (StageFrame) e.getWindow();

    if(Context.instance().keyboardFocusManager != null)
      Context.instance().keyboardFocusManager.focusFrame(focusedFrame);

    focusedFrame.activated(e);
  }

  public void getVisibleFrames(ArrayList<StageFrame> result)
  {
    for(StageFrame frame : frames)
    {
      if(frame.isVisible())
        result.add(frame);
    }
  }
}