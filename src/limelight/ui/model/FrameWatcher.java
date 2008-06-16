//- Copyright 2008 8th Light, Inc.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model;

import limelight.ui.api.Theater;

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
  }
}
