//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model;

import java.util.ArrayList;

public class MockFrameManager implements FrameManager
{
  public PropFrame focusedFrame;
  public boolean allFramesClosed;

  public void watch(PropFrame frame)
  {
  }

  public PropFrame getFocusedFrame()
  {
    return focusedFrame;
  }

  public void getVisibleFrames(ArrayList<PropFrame> result)
  {
    if(focusedFrame != null)
      result.add(focusedFrame);
  }

  public boolean isWatching(PropFrame frame)
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
