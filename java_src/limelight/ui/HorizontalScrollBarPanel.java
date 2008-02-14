package limelight.ui;

import javax.swing.*;

public class HorizontalScrollBarPanel extends ScrollBarPanel
{
  protected ScrollBar createScrollBar()
  {
    return new ScrollBar(JScrollBar.HORIZONTAL, this);
  }

  protected void valueChanged(int value)
  {
  }

  public void snapToSize()
  {  
    setSize(getParent().getWidth(), SCROLL_BAR_WIDTH);
  }


}
