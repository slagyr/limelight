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
  private PropFrame focusedFrame;
  private final HashSet<PropFrame> frames;
  private PropFrame lastFrameAdded;

  public AlertFrameManager()
  {
    frames = new HashSet<PropFrame>();
  }

  public void windowGainedFocus(WindowEvent e)
  {
  }

  public void windowLostFocus(WindowEvent e)
  {
  }

  public synchronized void watch(PropFrame frame)
  {
    if(!frames.contains(frame))
    {
      frame.getWindow().addWindowStateListener(this);
      frame.getWindow().addWindowFocusListener(this);
      frame.getWindow().addWindowListener(this);
      frames.add(frame);
      lastFrameAdded = frame;
    }
  }

  public void windowOpened(WindowEvent e)
  {
  }

  public void windowClosing(WindowEvent e)
  {
    PropFrame frame = ((PropFrameWindow) e.getWindow()).getPropFrame();
    if(frame.shouldAllowClose())
      frame.close(e);
  }

  public synchronized void windowClosed(WindowEvent e)
  {
    PropFrame frame = ((PropFrameWindow) e.getWindow()).getPropFrame();
    if(lastFrameAdded == frame)
      lastFrameAdded = null;
    if(focusedFrame == frame)
      focusedFrame = null;
    if(Context.instance().keyboardFocusManager != null)
      Context.instance().keyboardFocusManager.releaseFrame(frame.getWindow());
    frames.remove(frame);
    frame.closed(e);
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
    ((PropFrameWindow)e.getWindow()).getPropFrame().iconified(e);
  }

  public void windowDeiconified(WindowEvent e)
  {
    ((PropFrameWindow)e.getWindow()).getPropFrame().deiconified(e);
  }

  public void windowActivated(WindowEvent e)
  {
    Window window = e.getWindow();
    if(window instanceof PropFrameWindow)
    {
      activateFrame(e);
    }
  }

  public void windowDeactivated(WindowEvent e)
  {
    ((PropFrameWindow)e.getWindow()).getPropFrame().deactivated(e);
  }

  public void windowStateChanged(WindowEvent e)
  {
  }

  public PropFrame getFocusedFrame()
  {
    if(focusedFrame == null && lastFrameAdded != null && lastFrameAdded.isVisible())
      activateFrame(new WindowEvent(lastFrameAdded.getWindow(), WindowEvent.WINDOW_ACTIVATED));
    return focusedFrame;
  }

  public boolean isWatching(PropFrame frame)
  {
    boolean found = false;
    for(WindowFocusListener listener : frame.getWindow().getWindowFocusListeners())
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
      frame.close(new WindowEvent(frame.getWindow(), WindowEvent.WINDOW_CLOSING));
  }

  // private //////////////////////////////////////////////

  private void activateFrame(WindowEvent e)
  {
    focusedFrame = ((PropFrameWindow) e.getWindow()).getPropFrame();

    if(Context.instance().keyboardFocusManager != null)
      Context.instance().keyboardFocusManager.focusFrame(focusedFrame.getWindow());

    focusedFrame.activated(e);
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