package limelight.ui;

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
