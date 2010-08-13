package limelight.ui.model.inputs;

import limelight.util.Box;

import java.awt.*;
import java.awt.event.MouseEvent;

public class ScrollMouseProcessor
{
  private final ScrollRepeater.ScrollCondition notInSliderScrollCondition = new NotInSliderScrollCondition(this);
  private ScrollBar2Panel scrollBar;
  private ScrollRepeater repeater;
  private Point mouseLocation;
  private boolean sliderDragOn;
  private int sliderDragDelta;

  public ScrollMouseProcessor(ScrollBar2Panel scrollBar)
  {
    this.scrollBar = scrollBar;
    repeater = new ScrollRepeater(scrollBar);
  }

  public ScrollRepeater getRepeater()
  {
    return repeater;
  }

  private void startRepeater(int delta)
  {
    repeater.setScrollDelta(delta);
    repeater.resetTimer();
    repeater.start();
  }

  public void mousePressed(MouseEvent e)
  {
    mouseLocation = e.getPoint();
    if(scrollBar.getIncreasingButtonBounds().contains(mouseLocation))
      initiateUnitIncrement(scrollBar.getUnitIncrement());
    else if(scrollBar.getDecreasingButtonBounds().contains(mouseLocation))
      initiateUnitIncrement(-scrollBar.getUnitIncrement());
    else if(scrollBar.getTrackBounds().contains(mouseLocation))
    {
      Box sliderBounds = scrollBar.getSliderBounds();
      if(isAfterSlider(mouseLocation, sliderBounds))
        initiateBlockIncrement(scrollBar.getBlockIncrement());
      else if(isBeforeSlider(mouseLocation, sliderBounds))
        initiateBlockIncrement(-scrollBar.getBlockIncrement());
      else if(scrollBar.getSliderBounds().contains(mouseLocation))
      {
        sliderDragOn = true;
        sliderDragDelta = (int)(scrollBar.isHorizontal() ? mouseLocation.getX() - scrollBar.getSliderPosition() : mouseLocation.getY() - scrollBar.getSliderPosition());
      }
    }
  }

  public void mouseReleased(MouseEvent e)
  {
    repeater.stop();
    repeater.reset();
    scrollBar.setDecreasingButtonActive(false);
    scrollBar.setIncreasingButtonActive(false);
    sliderDragOn = false;
    scrollBar.markAsDirty();
  }

  public void mouseClicked(MouseEvent e)
  {
  }

  public void mouseDragged(MouseEvent e)
  {
    if(sliderDragOn)
    {
      if(scrollBar.isHorizontal())
        scrollBar.setSliderPosition(e.getX() - sliderDragDelta);
      else
        scrollBar.setSliderPosition(e.getY() - sliderDragDelta);
    }
  }

  public void mouseEntered(MouseEvent e)
  {

  }

  public void mouseExited(MouseEvent e)
  {

  }

  public void mouseMoved(MouseEvent e)
  {

  }

  private void initiateBlockIncrement(int scrollDelta)
  {
    scrollBar.setValue(scrollBar.getValue() + scrollDelta);
    repeater.setScrollCondition(getNotInSliderScrollCondition());
    startRepeater(scrollDelta);
  }

  private void initiateUnitIncrement(int scrollDelta)
  {
    if(scrollDelta > 0)
      scrollBar.setIncreasingButtonActive(true);
    else
      scrollBar.setDecreasingButtonActive(true);

    scrollBar.setValue(scrollBar.getValue() + scrollDelta);
    startRepeater(scrollDelta);
  }

  private boolean isAfterSlider(Point point, Box sliderBounds)
  {
    return scrollBar.isHorizontal() ? point.x > sliderBounds.right() : point.y > sliderBounds.bottom();
  }

  private boolean isBeforeSlider(Point point, Box sliderBounds)
  {
    return scrollBar.isHorizontal() ? point.x < sliderBounds.x : point.y < sliderBounds.y;
  }

  public Point getMouseLocation()
  {
    return mouseLocation;
  }

  public ScrollRepeater.ScrollCondition getNotInSliderScrollCondition()
  {
    return notInSliderScrollCondition;
  }

  public void setMouseLocation(int x, int y)
  {
    mouseLocation = new Point(x, y);
  }

  public boolean isSliderDragOn()
  {
    return sliderDragOn;
  }

  private static class NotInSliderScrollCondition implements ScrollRepeater.ScrollCondition
  {
    private ScrollMouseProcessor scrollMouseProcessor;

    public NotInSliderScrollCondition(ScrollMouseProcessor scrollMouseProcessor)
    {
      this.scrollMouseProcessor = scrollMouseProcessor;
    }

    public boolean canScroll()
    {
      return !scrollMouseProcessor.scrollBar.getSliderBounds().contains(scrollMouseProcessor.getMouseLocation());
    }
  }
}
