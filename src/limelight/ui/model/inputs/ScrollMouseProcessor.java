package limelight.ui.model.inputs;

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
    if(scrollBar.getIncreasingButtonBounds().contains(e.getPoint()))
    {
      scrollBar.setIncreasingButtonActive(true);
      scrollBar.setValue(scrollBar.getValue() + scrollBar.getUnitIncrement());
      startRepeater(scrollBar.getUnitIncrement());
    }
    else if(scrollBar.getDecreasingButtonBounds().contains(e.getPoint()))
    {
      scrollBar.setDecreasingButtonActive(true);
      scrollBar.setValue(scrollBar.getValue() - scrollBar.getUnitIncrement());
      startRepeater(-scrollBar.getUnitIncrement());
    }
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
