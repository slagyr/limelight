//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model;

import java.awt.event.WindowFocusListener;
import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;

public class InertFrameManager implements FrameManager
{
  public void watch(Frame frame)
  {
  }

  public Frame getActiveFrame()
  {
    return null;
  }

  public boolean isWatching(Frame frame)
  {
    return false;
  }

  public int getFrameCount()
  {
    return 0;
  }

  public void closeAllFrames()
  {
  }
}
