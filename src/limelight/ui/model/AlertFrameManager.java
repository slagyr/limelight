//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model;

import limelight.Context;
import limelight.ui.api.Stage;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;
import java.awt.*;
import java.util.HashSet;
import java.util.ArrayList;

public class AlertFrameManager implements WindowFocusListener, WindowListener, FrameManager
{
  private StageFrame activeFrame;
  private final HashSet<StageFrame> frames;
  private StageFrame lastFrameAdded;

  public AlertFrameManager()
  {
    frames = new HashSet<StageFrame>();
  }

  public void windowGainedFocus(WindowEvent e)
  {
    Window window = e.getWindow();
    if(window instanceof StageFrame)
    {
      activateFrame(window);
    }
  }

  public void windowLostFocus(WindowEvent e)
  {
  }

  public synchronized void watch(StageFrame frame)
  {
    if(!frames.contains(frame))
    {
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
      frame.close();
  }

  public synchronized void windowClosed(WindowEvent e)
  {
    StageFrame frame = ((StageFrame) e.getWindow());
    frames.remove(frame);
    if(frames.isEmpty() || allFramesHidden())
      Context.instance().attemptShutdown();
  }

  private synchronized boolean allFramesHidden()
  {
    for(StageFrame frame : frames)
    {
      if(frame.isVisible())
        return false;
    }
    return true;
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

  public StageFrame getActiveFrame()
  {
    if(activeFrame == null && lastFrameAdded != null && lastFrameAdded.isVisible())
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

  public synchronized void closeAllFrames()
  {
    for(StageFrame frame : frames)
      frame.close();
  }

  // private //////////////////////////////////////////////

  private void activateFrame(Window window)
  {
    activeFrame = (StageFrame) window;

    if(Context.instance().keyboardFocusManager != null)
      Context.instance().keyboardFocusManager.focusFrame(activeFrame);

    Stage stage = activeFrame.getStage();
    stage.theater().stage_activated(stage);
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