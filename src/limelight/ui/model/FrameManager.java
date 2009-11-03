//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model;

import java.util.ArrayList;

public interface FrameManager
{
  void watch(PropFrame frame);
  PropFrame getFocusedFrame();
  void getVisibleFrames(ArrayList<PropFrame> result);
  boolean isWatching(PropFrame frame);
  int getFrameCount();
  void closeAllFrames();
}
