package limelight.ui.model.inputs;

import limelight.ui.Panel;
import limelight.ui.model.RootPanel;

import java.awt.*;

public class ContainerStub extends Container
{
  private Panel panel;

  public ContainerStub(limelight.ui.Panel panel)
  {
    this.panel = panel;
  }

  public Container getParent()
  {
    Container parent = null;
    if(panel != null & panel.getRoot() != null)
      parent = ((RootPanel)panel.getRoot()).getFrame();
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
      return panel.getAbsoluteLocation().y;
  }

  public Point getLocation(Point rv)
  {
    if(panel == null || panel.getRoot() == null)
      return new Point(0, 0);
    else
      return panel.getAbsoluteLocation();
  }
}
