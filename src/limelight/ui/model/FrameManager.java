//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model;

public interface FrameManager
{
  void watch(Frame frame);
  Frame getActiveFrame();
  boolean isWatching(Frame frame);
  int getFrameCount();
}
