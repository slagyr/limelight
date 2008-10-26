//- Copyright 2008 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model;

import java.awt.event.WindowFocusListener;
import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;
import java.awt.*;
import java.util.HashSet;

public class AlertFrameManager implements WindowFocusListener, WindowListener, FrameManager
{
  private Frame activeFrame;
  private final HashSet<Frame> frames;

  public AlertFrameManager()
  {
    frames = new HashSet<Frame>();
  }

  public void windowGainedFocus(WindowEvent e)
  {
    Window window = e.getWindow();  
    if(window instanceof Frame)
    {
      activeFrame = (Frame)window;
//      theater.stage_activated(frame.getStage());
    }
  }

  public void windowLostFocus(WindowEvent e)
  {
  }

  public void watch(Frame frame)
  {
    if(!frames.contains(frame))
    {
      frame.addWindowFocusListener(this);
      frame.addWindowListener(this);
      frames.add(frame);
    }
  }

  public void windowOpened(WindowEvent e)
  {
  }

  public void windowClosing(WindowEvent e)
  {
  }

  public void windowClosed(WindowEvent e)
  {
    Frame frame = (Frame)e.getWindow();
    frames.remove(frame);
    if(frames.isEmpty())
      System.exit(0);
  }

  public void windowIconified(WindowEvent e)
  {
  }

  public void windowDeiconified(WindowEvent e)
  {
  }

  public void windowActivated(WindowEvent e)
  {
  }

  public void windowDeactivated(WindowEvent e)
  {
  }

  public Frame getActiveFrame()
  {
    return activeFrame;
  }

  public boolean isWatching(Frame frame)
  {
    boolean found = false;
    for(WindowFocusListener listener : frame.getWindowFocusListeners())
    {
      if(listener == this)
        found = true;
    }
    return found;
  }

  public int getFrameCount()
  {
    return frames.size();
  }
}