//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model;

public class MockFrameManager implements FrameManager
{
  public Frame activeFrame;
  public boolean allFramesClosed;

  public void watch(Frame frame)
  {
  }

  public Frame getActiveFrame()
  {
    return activeFrame;
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
    allFramesClosed = true;
  }
}
