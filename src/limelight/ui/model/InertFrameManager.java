//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model;

import java.util.List;
import java.util.ArrayList;

public class InertFrameManager implements FrameManager
{
  private List<StageFrame> frames = new ArrayList<StageFrame>();

  public void watch(StageFrame stageFrame)
  {
    frames.add(stageFrame);
  }

  public StageFrame getFocusedFrame()
  {
    return null;
  }

  public void getVisibleFrames(ArrayList<StageFrame> result)
  {
  }

  public boolean isWatching(StageFrame frame)
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
