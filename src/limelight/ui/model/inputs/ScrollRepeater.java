package limelight.ui.model.inputs;

import limelight.background.Animation;

public class ScrollRepeater extends Animation
{
  private int scrollDelta;
  private ScrollBar2Panel scrollBar;

  public ScrollRepeater(ScrollBar2Panel scrollBar)
  {
    super(1);
    this.scrollBar = scrollBar;
  }

  @Override
  protected void doUpdate()
  {
    scrollBar.setValue(scrollBar.getValue() + scrollDelta);    
  }

  public int getScrollDelta()
  {
    return scrollDelta;
  }

  public void setScrollDelta(int delta)
  {
    scrollDelta = delta;
  }
}
