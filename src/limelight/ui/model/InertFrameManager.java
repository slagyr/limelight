//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model;

public class InertFrameManager implements FrameManager
{
  public void watch(StageFrame stageFrame)
  {
  }

  public StageFrame getActiveFrame()
  {
    return null;
  }

  public boolean isWatching(StageFrame frame)
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
