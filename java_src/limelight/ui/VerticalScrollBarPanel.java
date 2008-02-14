package limelight.ui;

import javax.swing.*;

public class VerticalScrollBarPanel extends ScrollBarPanel
{
  protected ScrollBar createScrollBar()
  {
    return new ScrollBar(JScrollBar.VERTICAL, this);
  }

  protected void valueChanged(int value)
  {
    
  }

  public void snapToSize()
  {
    setSize(SCROLL_BAR_WIDTH, getParent().getHeight());  
  }
}
