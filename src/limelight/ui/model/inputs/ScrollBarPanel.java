//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs;

import limelight.styles.ScreenableStyle;
import limelight.ui.model.BasePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ScrollBarPanel extends BasePanel
{
  public static final int VERTICAL = JScrollBar.VERTICAL;
  public static final int HORIZONTAL = JScrollBar.HORIZONTAL;
  private final JScrollBar scrollBar;
  private final int preferredGirth;
  private boolean configuring;

  public ScrollBarPanel(int orientation)
  {
    scrollBar = new JScrollBar(orientation);
    scrollBar.addAdjustmentListener(new AdjustmentListener()
    {
      public void adjustmentValueChanged(AdjustmentEvent e)
      {
        if(!configuring)
        {
//          ((BasePanel)getParent()).markAsNeedingLayout(new ScrollLayout(scrollBar.getOrientation(), scrollBar));
        }
      }
    });
    if(getOrientation() == VERTICAL)
      preferredGirth = width = scrollBar.getPreferredSize().width;
    else
      preferredGirth = height = scrollBar.getPreferredSize().height;
  }

  public int getOrientation()
  {
    return scrollBar.getOrientation();
  }

  public JScrollBar getScrollBar()
  {
    return scrollBar;
  }

  public limelight.util.Box getChildConsumableArea()
  {
    return getBoundingBox();
  }

  public limelight.util.Box getBoxInsidePadding()
  {
    return getBoundingBox();
  }

  public ScreenableStyle getStyle()
  {
    return getParent().getStyle();
  }

  public void setSize(int width, int height)
  {
    if(getOrientation() == VERTICAL)
      width = preferredGirth;
    else
      height = preferredGirth;
    super.setSize(width, height);
    scrollBar.setSize(width, height);
  }

  public void setHeight(int height)
  {
    setSize(preferredGirth, height);
  }

  public void setWidth(int width)
  {
    setSize(width, preferredGirth);
  }

  public void mousePressed(MouseEvent e)
  {
    for(MouseListener mouseListener : scrollBar.getMouseListeners())
      mouseListener.mousePressed(translatedEvent(e));
  }

  public void mouseReleased(MouseEvent e)
  {
    for(MouseListener mouseListener : scrollBar.getMouseListeners())
      mouseListener.mouseReleased(translatedEvent(e));
  }

  public void mouseClicked(MouseEvent e)
  {
    for(MouseListener mouseListener : scrollBar.getMouseListeners())
      mouseListener.mouseClicked(translatedEvent(e));
  }

  public void mouseDragged(MouseEvent e)
  {
    for(MouseMotionListener mouseListener : scrollBar.getMouseMotionListeners())
      mouseListener.mouseDragged(translatedEvent(e));
  }

  public void paintOn(Graphics2D graphics)
  {
    scrollBar.paint(graphics);
  }

  public boolean canBeBuffered()
  {
    return false;
  }

  public void setValue(int value)
  {
    scrollBar.setValue(value);
  }

  public int getValue()
  {
    return scrollBar.getValue();
  }

  public int getMaximumValue()
  {
    return scrollBar.getMaximum();
  }

  public int getVisibleAmount()
  {
    return scrollBar.getVisibleAmount();
  }

  public int getUnitIncrement()
  {
    return scrollBar.getUnitIncrement();
  }

  public int getBlockIncrement()
  {
    return scrollBar.getBlockIncrement();
  }

  public void configure(int visible, int maximum)
  {
    configuring = true;
    scrollBar.setMaximum(maximum);
    scrollBar.setVisibleAmount(visible);
    scrollBar.setUnitIncrement((int) (visible * 0.1));
    scrollBar.setBlockIncrement((int) (visible * 0.9));
    configuring = false;
  }
}
