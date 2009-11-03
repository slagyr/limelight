//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model;

import java.util.List;
import java.util.ArrayList;

public class InertFrameManager implements FrameManager
{
  private List<PropFrame> frames = new ArrayList<PropFrame>();

  public void watch(PropFrame stageFrame)
  {
    frames.add(stageFrame);
  }

  public PropFrame getFocusedFrame()
  {
    return null;
  }

  public void getVisibleFrames(ArrayList<PropFrame> result)
  {
  }

  public boolean isWatching(PropFrame frame)
  {
    return frames.contains(frame);
  }

  public int getFrameCount()
  {
    return frames.size();
  }

  public void closeAllFrames()
  {
  }
}
