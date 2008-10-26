package limelight.ui.model;

public interface FrameManager
{
  void watch(Frame frame);
  Frame getActiveFrame();
  boolean isWatching(Frame frame);
  int getFrameCount();
}
