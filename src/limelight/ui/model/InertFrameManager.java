//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.ui.model;

import java.util.ArrayList;
import java.util.List;

public class InertFrameManager implements FrameManager
{
  private List<StageFrame> frames = new ArrayList<StageFrame>();

  public void watch(StageFrame stageFrame)
  {
    frames.add(stageFrame);
  }

  public StageFrame getActiveFrame()
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
