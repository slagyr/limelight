package limelight.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public abstract class ScrollBarPanel extends ComponentPanel
{
  public static final int SCROLL_BAR_WIDTH = 15;

  private JScrollBar scrollBar;
  private int value;
  private boolean locked;

  public ScrollBarPanel()
  {
    scrollBar = createScrollBar();
    scrollBar.setMinimum(0);
    scrollBar.setUnitIncrement(5);
    scrollBar.addAdjustmentListener(new ScrollAdjustmentListener());
  }

  protected abstract ScrollBar createScrollBar();

  public Component getComponent()
  {
    return scrollBar;
  }

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

  public void paintOn(Graphics2D graphics)
  {
    scrollBar.paint(graphics);
  }

  private class ScrollAdjustmentListener implements AdjustmentListener
  {
    public void adjustmentValueChanged(AdjustmentEvent e)
    {
      valueChanged(e.getValue());
    }
  }

  static protected class ScrollBar extends JScrollBar
  {
    private Panel panel;

    public ScrollBar(int orientation, Panel panel)
    {
      super(orientation);
      setOpaque(true);
      this.panel = panel;
    }

    public Graphics getGraphics()
    {
      return panel.getClippedGraphics();
    }
  }
}
