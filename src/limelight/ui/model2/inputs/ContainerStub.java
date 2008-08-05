package limelight.ui.model2.inputs;

import limelight.ui.Panel;
import limelight.ui.model2.RootPanel;

import javax.accessibility.AccessibleContext;
import java.awt.*;
import java.awt.im.InputMethodRequests;
import java.awt.im.InputContext;
import java.awt.image.ColorModel;
import java.awt.image.ImageProducer;
import java.awt.image.VolatileImage;
import java.awt.image.ImageObserver;
import java.awt.dnd.DropTarget;
import java.awt.peer.ComponentPeer;
import java.awt.event.*;
import java.util.EventListener;
import java.util.Set;
import java.util.Locale;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.beans.PropertyChangeListener;

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
