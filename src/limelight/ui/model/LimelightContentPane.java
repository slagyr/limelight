//- Copyright � 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model;

import limelight.background.PanelPainterLoop;

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
    RootPanel rootPanel = frame.getRoot();
    if(rootPanel != null)
    {
      if(isWindowResizing())
      {
        PanelPainterLoop.doPaintJob(rootPanel.getPanel(), rootPanel.getPanel().getAbsoluteBounds(), (Graphics2D) g);
      }
      else
      {
        rootPanel.addDirtyRegion(frame.getRoot().getPanel().getAbsoluteBounds());
      }
    }
  }

  // MDM - When the window is resizing, the layout and painting have to take place
  // immediately.  Otherwise, the window flashes and flickers annoyingly.
  // The best way to tell if the window is being resized (as far as I can tell), is to
  // check the currentEvent in the EventQueue. I didn't see anything in particular about
  // the event that tells us what's happening.
  private boolean isWindowResizing()
  {
    return EventQueue.getCurrentEvent() != null;
  }

  public void doLayout()
  {
    RootPanel rootPanel = frame.getRoot();
    if(rootPanel != null && isWindowResizing())
    {
      rootPanel.doLayout();
    }
    else
      super.doLayout();
  }
}
