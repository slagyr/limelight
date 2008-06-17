//- Copyright 2008 8th Light, Inc.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model;

import limelight.ui.api.Theater;

import java.awt.event.WindowFocusListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.*;

public class FrameWatcher implements WindowFocusListener, WindowListener
{
  private Theater theater;
  private int frameCount;

  public FrameWatcher(Theater theater)
  {
    this.theater = theater;
    frameCount = 0;
  }

  public void windowGainedFocus(WindowEvent e)
  {
    Window window = e.getWindow();
    if(window instanceof limelight.ui.model.Frame)
    {
      limelight.ui.model.Frame frame = (limelight.ui.model.Frame)window;
      theater.stage_activated(frame.getStage());
    }
  }

  public void windowLostFocus(WindowEvent e)
  {
  }

  public void watch(limelight.ui.model.Frame frame)
  {
    frame.addWindowFocusListener(this);
    frame.addWindowListener(this);
    frameCount++;
  }

  public void windowOpened(WindowEvent e)
  {
  }

  public void windowClosing(WindowEvent e)
  {
  }

  public void windowClosed(WindowEvent e)
  {
    frameCount--;
System.err.println("frameCount = " + frameCount);
    if(frameCount == 0)
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
}
