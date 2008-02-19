package limelight.ui;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public abstract class ComponentPanel extends Panel
{
  public abstract Component getComponent();

  public void doLayout()
  {
    if(getComponent().getParent() == null)
      getFrame().getHiddenPanel().add(getComponent());
    super.doLayout();
  }

  public void setSize(int width, int height)
  {
    super.setSize(width, height);
    getComponent().setSize(width, height);
  }

    public void mousePressed(MouseEvent e)
  {
    e = translatedMouseEvent(e);
    for(MouseListener listener: getComponent().getMouseListeners())
      listener.mousePressed(e);
  }

  public void mouseReleased(MouseEvent e)
  {
    e = translatedMouseEvent(e);
    for(MouseListener listener: getComponent().getMouseListeners())
      listener.mouseReleased(e);
  }

  public void mouseClicked(MouseEvent e)
  {
    e = translatedMouseEvent(e);
    for(MouseListener listener: getComponent().getMouseListeners())
      listener.mouseClicked(e);
  }

  public void mouseDragged(MouseEvent e)
  {
    e = translatedMouseEvent(e);
    for(MouseMotionListener listener: getComponent().getMouseMotionListeners())
      listener.mouseDragged(e);
  }

  public void mouseMoved(MouseEvent e)
  {
    e = translatedMouseEvent(e);
    for(MouseMotionListener listener: getComponent().getMouseMotionListeners())
      listener.mouseMoved(e);
  }

  private MouseEvent translatedMouseEvent(MouseEvent e)
  {
    Point absoluteLocation = getAbsoluteLocation();
    return new MouseEvent(getComponent(), e.getID(), e.getWhen(), e.getModifiers(), e.getX() - absoluteLocation.x, e.getY()- absoluteLocation.y, e.getClickCount(), false);
  }
}
