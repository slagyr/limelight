//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.ui.model;

import java.util.ArrayList;

public interface FrameManager
{
  void watch(StageFrame frame);
  StageFrame getActiveFrame();
  void getVisibleFrames(ArrayList<StageFrame> result);
  boolean isWatching(StageFrame frame);
  int getFrameCount();
  void closeAllFrames();
}
