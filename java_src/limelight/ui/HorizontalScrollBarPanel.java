package limelight.ui;

import javax.swing.*;

public class HorizontalScrollBarPanel extends ScrollBarPanel
{
  protected ScrollBar createScrollBar()
  {
    return new ScrollBar(JScrollBar.HORIZONTAL, this);
  }

  public void snapToSize()
  {
    if(!getScrollPanel().isHorizontalScrollBarOn())
      setSize(0, 0);
    else if(getScrollPanel().isVerticalScrollBarOn())
      setSize(getParent().getWidth() - SCROLL_BAR_WIDTH, SCROLL_BAR_WIDTH);
    else
      setSize(getParent().getWidth(), SCROLL_BAR_WIDTH);
  }
}
