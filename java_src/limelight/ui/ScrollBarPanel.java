package limelight.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.Rectangle;
import java.awt.event.*;

public abstract class ScrollBarPanel extends Panel
{
  public static final int SCROLL_BAR_WIDTH = 15;

  private JScrollBar scrollBar;
  private int value;
  private boolean locked;

  public ScrollBarPanel()
  {
    scrollBar = createScrollBar();
    scrollBar.setMinimum(0);
    scrollBar.setUnitIncrement(1);
    scrollBar.addAdjustmentListener(new ScrollAdjustmentListener());
  }

  protected abstract ScrollBar createScrollBar();

  public void setConfigurations(int max, int viewable)
  {
    locked = true;
    scrollBar.setMaximum(max);
    scrollBar.setVisibleAmount(viewable);
    scrollBar.setBlockIncrement((int)(((float)viewable) * 0.9));
    locked = false;
  }

  private void valueChanged(int value)
  {
    this.value = value;
    if(locked)
      return;
    getView().update();
  }

  public int getValue()
  {
    return value;
  }

  public void setValue(int value)
  {
    this.value = value;
  }

  public JScrollBar getScrollBar()
  {
    return scrollBar;
  }

  protected ScrollViewPanel getView()
  {
    return getScrollPanel().getView();
  }

  protected ScrollPanel getScrollPanel()
  {
    return (ScrollPanel)getParent();
  }

  public void setSize(int width, int height)
  {
    super.setSize(width, height);
    scrollBar.setSize(width, height);
  }

  public void paintOn(Graphics2D graphics)
  {
    scrollBar.paint(graphics);
  }

  public void mousePressed(MouseEvent e)
  {
    e = translatedMouseEvent(e);
    for(MouseListener listener: scrollBar.getMouseListeners())
      listener.mousePressed(e);
  }

  public void mouseReleased(MouseEvent e)
  {
    e = translatedMouseEvent(e);
    for(MouseListener listener: scrollBar.getMouseListeners())
      listener.mouseReleased(e);
  }

  public void mouseClicked(MouseEvent e)
  {
    e = translatedMouseEvent(e);
    for(MouseListener listener: scrollBar.getMouseListeners())
      listener.mouseClicked(e);
  }

  public void mouseDragged(MouseEvent e)
  {
    e = translatedMouseEvent(e);
    for(MouseMotionListener listener: scrollBar.getMouseMotionListeners())
      listener.mouseDragged(e);
  }

  private MouseEvent translatedMouseEvent(MouseEvent e)
  {
    Point absoluteLocation = getAbsoluteLocation();
    return new MouseEvent(scrollBar, e.getID(), e.getWhen(), e.getModifiers(), e.getX() - absoluteLocation.x, e.getY()- absoluteLocation.y, e.getClickCount(), false);
  }

  private class ScrollAdjustmentListener implements AdjustmentListener
  {
    public void adjustmentValueChanged(AdjustmentEvent e)
    {
      valueChanged(e.getValue());
    }
  }

  protected class ScrollBar extends JScrollBar
  {
    private Panel panel;

    public ScrollBar(int orientation, Panel panel)
    {
      super(orientation);
      this.panel = panel;
    }

    public void repaint(long tm, int x, int y, int width, int height)
    {
      repaint();
    }

    public void repaint(Rectangle r)
    {
      repaint();
    }

    public void repaint()
    {
      if(panel == null || locked)
        return;
      paint(getClippedGraphics());
    }
  }
}
