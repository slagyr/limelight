package limelight.ui.model.inputs;

import limelight.background.Animation;

public class ScrollRepeater extends Animation
{
  private int scrollDelta;
  private ScrollBarPanel scrollBar;
  private int maxScrollDelta;
  private int ticks;
  private boolean accelerating;
  private ScrollCondition scrollCondition;

  public ScrollRepeater(ScrollBarPanel scrollBar)
  {
    super(2);
    this.scrollBar = scrollBar;
  }

  @Override
  protected void doUpdate()
  {
    if (scrollCondition != null && !scrollCondition.canScroll())
      return;

    scrollBar.setValue(scrollBar.getValue() + scrollDelta);
    if(accelerating)
      handleAcceleration();
    else
      startAccelerating();
  }

  private void startAccelerating()
  {
    stop();
    setUpdatesPerSecond(20);
    accelerating = true;
    start();
  }

  private void handleAcceleration()
  {
    ticks++;
    if(ticks >= 10 && scrollDelta < maxScrollDelta)
    {
      scrollDelta *= 1.25;
      scrollDelta = Math.min(scrollDelta, maxScrollDelta);
      ticks = 0;
    }
  }

  public int getScrollDelta()
  {
    return scrollDelta;
  }

  public void setScrollDelta(int delta)
  {
    scrollDelta = delta;
    maxScrollDelta = delta * 5;
  }

  public void reset()
  {
    ticks = 0;
    accelerating = false;
    setUpdatesPerSecond(2);
  }

  public void setScrollCondition(ScrollCondition scrollCondition)
  {
    this.scrollCondition = scrollCondition;
  }

  public ScrollCondition getScrollCondition()
  {
    return scrollCondition;
  }

  public interface ScrollCondition
  {
    public boolean canScroll();
  }
}
