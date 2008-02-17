package limelight.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseWheelEvent;
import java.util.LinkedList;

public class ScrollPanel extends ParentPanel
{
  private ScrollViewPanel view;
  private ScrollBarPanel verticalScrollBar;
  private ScrollBarPanel horizontalScrollBar;
  private boolean verticalScrollBarOn;
  private boolean horizontalScrollBarOn;

  public ScrollPanel(LinkedList<Panel> children) throws SterilePanelException
  {
    view = new ScrollViewPanel(children);
    verticalScrollBar = new VerticalScrollBarPanel();
    horizontalScrollBar = new HorizontalScrollBarPanel();
    add(view);
    enableVerticalScrollBar();
    enableHorizontalScrollBar();
  }

  public ScrollViewPanel getView()
  {
    return view;
  }

  public ScrollBarPanel getVerticalScrollBar()
  {
    return verticalScrollBar;
  }

  public ScrollBarPanel getHorizontalScrollBar()
  {
    return horizontalScrollBar;
  }

  public Point getScrollCoordinates()
  {
    return new Point(horizontalScrollBar.getValue(), verticalScrollBar.getValue());
  }

  public Rectangle getChildConsumableArea()
  {
    return new Rectangle(0, 0, getWidth(), getHeight());
  }

  public void doLayout()
  {
    snapToSize();
    view.doLayout();

    verticalScrollBar.setConfigurations(view.getConsumedHeight(), view.getHeight());
    horizontalScrollBar.setConfigurations(view.getConsumedWidth(), view.getWidth());

    verticalScrollBar.doLayout();
    horizontalScrollBar.doLayout();

    view.setLocation(0, 0);
    verticalScrollBar.setLocation(getWidth() - ScrollBarPanel.SCROLL_BAR_WIDTH, 0);
    horizontalScrollBar.setLocation(0, getHeight() - ScrollBarPanel.SCROLL_BAR_WIDTH);
  }

  public void snapToSize()
  {
    Rectangle consumableArea = getParent().getChildConsumableArea();
    setSize(consumableArea.width,  consumableArea.height);
  }

  public void paintOn(Graphics2D graphics)
  {
  }

  public void mouseWheelMoved(MouseWheelEvent e)
  {
    boolean isVertical = e.getModifiers() % 2 == 0;
    JScrollBar scrollBar = isVertical ? verticalScrollBar.getScrollBar() : horizontalScrollBar.getScrollBar();
    scrollBar.setValue(scrollBar.getValue() + e.getUnitsToScroll());
  }

  public boolean isVerticalScrollBarOn()
  {
    return verticalScrollBarOn;
  }

  public boolean isHorizontalScrollBarOn()
  {
    return horizontalScrollBarOn;
  }

  public void disableVerticalScrollBar()
  {
    verticalScrollBarOn = false;
    remove(verticalScrollBar);
  }

  public void enableVerticalScrollBar()
  {
    verticalScrollBarOn = true;
    addUnchecked(verticalScrollBar);
  }

  public void disableHorizontalScrollBar()
  {
    horizontalScrollBarOn = false;
    remove(horizontalScrollBar);
  }

  public void enableHorizontalScrollBar()
  {
    horizontalScrollBarOn = true;
    addUnchecked(horizontalScrollBar);
  }
}
