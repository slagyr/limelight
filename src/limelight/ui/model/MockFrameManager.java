//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model;

import java.util.ArrayList;

public class MockFrameManager implements FrameManager
{
  public StageFrame activeFrame;
  public boolean allFramesClosed;

  public void watch(StageFrame frame)
  {
  }

  public StageFrame getActiveFrame()
  {
    return activeFrame;
  }

  public void getVisibleFrames(ArrayList<StageFrame> result)
  {
    if(activeFrame != null)
      result.add(activeFrame);
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
    allFramesClosed = true;
  }
}
