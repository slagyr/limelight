package limelight.ui.model.inputs;

import limelight.ui.EventAction;
import limelight.ui.events.*;
import limelight.ui.events.Event;
import limelight.util.Box;

import java.awt.*;

public class ScrollMouseProcessor implements EventAction
{
  private final ScrollRepeater.ScrollCondition notInSliderScrollCondition = new NotInSliderScrollCondition(this);
  private ScrollBarPanel scrollBar;
  private ScrollRepeater repeater;
  private Point mouseLocation;
  private int sliderDragDelta;
  private boolean sliderDragOn;
  private boolean unitIncrementOn;
  private boolean blockIncrementOn;

  public ScrollMouseProcessor(ScrollBarPanel scrollBar)
  {
    this.scrollBar = scrollBar;
    repeater = new ScrollRepeater(scrollBar);
  }
  
  public void invoke(Event event)
  {
    if(event instanceof MousePressedEvent)
      mousePressed((MousePressedEvent)event);
    else if(event instanceof MouseReleasedEvent)
      mouseReleased((MouseReleasedEvent)event);
    else if(event instanceof MouseDraggedEvent)
      mouseDragged((MouseDraggedEvent)event);
    else if(event instanceof MouseExitedEvent)
      mouseExited((MouseExitedEvent)event);
  }

  public ScrollRepeater getRepeater()
  {
    return repeater;
  }

  private void startRepeater(int delta)
  {
    repeater.setScrollDelta(delta);
    startRepeater();
  }

  private void startRepeater()
  {
    if(!repeater.isRunning())
    {
      repeater.resetTimer();
      repeater.start();
    }
  }

  public void mousePressed(MousePressedEvent e)
  {
    sliderDragOn = false;
    unitIncrementOn = false;
    blockIncrementOn = false;

    mouseLocation = e.getLocation();
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
        sliderDragDelta = (int) (scrollBar.isHorizontal() ? mouseLocation.getX() - scrollBar.getSliderPosition() : mouseLocation.getY() - scrollBar.getSliderPosition());
      }
    }
  }

  public void mouseReleased(MouseReleasedEvent e)
  {
    repeater.stop();
    repeater.reset();
    scrollBar.setDecreasingButtonActive(false);
    scrollBar.setIncreasingButtonActive(false);
    scrollBar.markAsDirty();
  }

  public void mouseDragged(MouseDraggedEvent e)
  {
    mouseLocation = e.getLocation();
    if(sliderDragOn)
      processSliderDrag();
    else if(unitIncrementOn)
      processUnitIncrementDrag();
    else if(blockIncrementOn)
      processBlockIncremementDrag();
  }

  public void mouseExited(MouseExitedEvent e)
  {
    repeater.stop();
    deactivateButtons();
  }

  private void processBlockIncremementDrag()
  {
    if(scrollBar.getTrackBounds().contains(mouseLocation))
    {
      Box sliderBounds = scrollBar.getSliderBounds();
      final boolean shouldReverseBlockIncrement = (repeater.getScrollDelta() < 0 && isAfterSlider(mouseLocation, sliderBounds))
                                               || (repeater.getScrollDelta() > 0 && isBeforeSlider(mouseLocation, sliderBounds));
      if(shouldReverseBlockIncrement)
        repeater.setScrollDelta(repeater.getScrollDelta() * -1);

      startRepeater();
    }
    else
      repeater.stop();
  }

  private void processUnitIncrementDrag()
  {
    if(scrollBar.getDecreasingButtonBounds().contains(mouseLocation))
    {
      scrollBar.setDecreasingButtonActive(true);
      if(repeater.getScrollDelta() > 0)
      {
        scrollBar.setIncreasingButtonActive(false);
        repeater.setScrollDelta(repeater.getScrollDelta() * -1);
      }
      startRepeater();
    }
    else if(scrollBar.getIncreasingButtonBounds().contains(mouseLocation))
    {
      scrollBar.setIncreasingButtonActive(true);
      if(repeater.getScrollDelta() < 0)
      {
        scrollBar.setDecreasingButtonActive(false);
        repeater.setScrollDelta(repeater.getScrollDelta() * -1);
      }
      startRepeater();
    }
    else
    {
      deactivateButtons();
      repeater.stop();
    }
  }

  private void processSliderDrag()
  {
    if(scrollBar.isHorizontal())
      scrollBar.setSliderPosition(mouseLocation.x - sliderDragDelta);
    else
      scrollBar.setSliderPosition(mouseLocation.y - sliderDragDelta);
  }

  private void deactivateButtons()
  {
    if(scrollBar.isIncreasingButtonActive())
      scrollBar.setIncreasingButtonActive(false);
    else if(scrollBar.isDecreasingButtonActive())
      scrollBar.setDecreasingButtonActive(false);
  }

  private void initiateBlockIncrement(int scrollDelta)
  {
    blockIncrementOn = true;
    scrollBar.setValue(scrollBar.getValue() + scrollDelta);
    repeater.setScrollCondition(getNotInSliderScrollCondition());
    startRepeater(scrollDelta);
  }

  private void initiateUnitIncrement(int scrollDelta)
  {
    unitIncrementOn = true;

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

  public boolean isUnitIncrementOn()
  {
    return unitIncrementOn;
  }

  public boolean isBlockIncrementOn()
  {
    return blockIncrementOn;
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
