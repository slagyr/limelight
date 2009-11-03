//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs;

import limelight.ui.Panel;

import java.awt.*;

public class ContainerStub extends Container
{
  private final Panel panel;

  public ContainerStub(limelight.ui.Panel panel)
  {
    this.panel = panel;
  }

  public Container getParent()
  {
    Container parent = null;
    if(panel != null && panel.getRoot() != null)
      parent = (panel.getRoot()).getStageFrame().getWindow();
    return parent;
  }

  public int getX()
  {
    if(panel == null || panel.getRoot() == null)
      return 0;
    else
      return panel.getAbsoluteLocation().x;
  }

  public int getY()
  {
    if(panel == null || panel.getRoot() == null)
      return 0;
    else
    {
      return panel.getAbsoluteLocation().y;
    }
  }

  public Point getLocation(Point rv)
  {
    if(panel == null || panel.getRoot() == null)
      return new Point(0, 0);
    else
      return panel.getAbsoluteLocation();
  }

  public void repaint()
  {
  }

  public void repaint(long tm)
  {
  }

  public void repaint(int x, int y, int width, int height)
  {
  }

  public void repaint(long tm, int x, int y, int width, int height)
  {
  }
}
