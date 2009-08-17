package limelight.ui.model;

import limelight.util.Debug;

import javax.swing.*;
import java.awt.*;

class LimelightContentPane extends JPanel
{
  private final StageFrame frame;

  public LimelightContentPane(StageFrame frame)
  {
    this.frame = frame;
  }

  public void paint(Graphics g)
  {
    if(frame.getRoot() != null)
    {
      frame.getRoot().addDirtyRegion(frame.getRoot().getPanel().getAbsoluteBounds());
    }
  }
}
