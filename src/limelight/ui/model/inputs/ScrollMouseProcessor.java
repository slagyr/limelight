package limelight.ui.model.inputs;

import limelight.util.Box;

import java.awt.*;
import java.awt.event.MouseEvent;

public class ScrollMouseProcessor
{
  private ScrollBar2Panel scrollBar;
  private ScrollRepeater repeater;

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
    final Point point = e.getPoint();
    if(scrollBar.getIncreasingButtonBounds().contains(point))
    {
      scrollBar.setIncreasingButtonActive(true);
      scrollBar.setValue(scrollBar.getValue() + scrollBar.getUnitIncrement());
      startRepeater(scrollBar.getUnitIncrement());
    }
    else if(scrollBar.getDecreasingButtonBounds().contains(point))
    {
      scrollBar.setDecreasingButtonActive(true);
      scrollBar.setValue(scrollBar.getValue() - scrollBar.getUnitIncrement());
      startRepeater(-scrollBar.getUnitIncrement());
    }
    else if(scrollBar.getTrackBounds().contains(point))
    {
      Box gemBounds = scrollBar.getGemBounds();
      if(isBeforeGem(point, gemBounds))
      {
        scrollBar.setValue(scrollBar.getValue() - scrollBar.getBlockIncrement());
      }
      else if(isAfterGem(point, gemBounds))
      {
        scrollBar.setValue(scrollBar.getValue() + scrollBar.getBlockIncrement());
      }
    }
  }

  private boolean isAfterGem(Point point, Box gemBounds)
  {
    return scrollBar.isHorizontal() ? point.x > gemBounds.right() : point.y > gemBounds.bottom();
  }

  private boolean isBeforeGem(Point point, Box gemBounds)
  {
    return scrollBar.isHorizontal() ? point.x < gemBounds.x : point.y < gemBounds.y;
  }

  public void mouseReleased(MouseEvent e)
  {
    repeater.stop();
    repeater.reset();
    scrollBar.setDecreasingButtonActive(false);
    scrollBar.setIncreasingButtonActive(false);
    scrollBar.markAsDirty();
  }

  public void mouseClicked(MouseEvent e)
  {
  }

  public void mouseDragged(MouseEvent e)
  {
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
}
