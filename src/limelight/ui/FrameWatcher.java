//- Copyright 2008 8th Light, Inc.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui;

import limelight.rapi.Theater;

import java.awt.event.WindowFocusListener;
import java.awt.event.WindowEvent;
import java.awt.*;

public class FrameWatcher implements WindowFocusListener
{
  private Theater theater;

  public FrameWatcher(Theater theater)
  {
    this.theater = theater;
  }

  public void windowGainedFocus(WindowEvent e)
  {
    Window window = e.getWindow();
    if(window instanceof Frame)
    {
      Frame frame = (Frame)window;
      theater.stage_activated(frame.getStage());
    }
  }

  public void windowLostFocus(WindowEvent e)
  {
  }

  public void watch(Frame frame)
  {
    frame.addWindowFocusListener(this);
  }
}
