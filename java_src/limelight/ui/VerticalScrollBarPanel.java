package limelight.ui;

import javax.swing.*;

public class VerticalScrollBarPanel extends ScrollBarPanel
{
  protected ScrollBar createScrollBar()
  {
    return new ScrollBar(JScrollBar.VERTICAL, this);
  }

  public void snapToSize()
  {
    if(!getScrollPanel().isVerticalScrollBarOn())
      setSize(0, 0);
    else if(getScrollPanel().isHorizontalScrollBarOn())
      setSize(SCROLL_BAR_WIDTH, getParent().getHeight() - SCROLL_BAR_WIDTH);
    else
      setSize(SCROLL_BAR_WIDTH, getParent().getHeight());
  }
}
