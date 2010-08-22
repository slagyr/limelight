package limelight.ui.model.inputs;

import limelight.Context;
import limelight.background.AnimationLoop;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class ScrollRepeaterTest
{
  private ScrollBarPanel scrollBar;
  private ScrollRepeater repeater;

  @Before
  public void setUp() throws Exception
  {
    scrollBar = new ScrollBarPanel(ScrollBarPanel.HORIZONTAL);
    scrollBar.configure(10, 100);
    repeater = scrollBar.getMouseProcessor().getRepeater();

    Context.instance().animationLoop = new AnimationLoop();
  }
  
  @Test
  public void updateIncrementsScrollBar() throws Exception
  {
    repeater.setScrollDelta(1);

    assertEquals(0, scrollBar.getValue());
    repeater.doUpdate();
    assertEquals(1, scrollBar.getValue());
    repeater.doUpdate();
    assertEquals(2, scrollBar.getValue());
  }
  
  @Test
  public void increasesUpdatesAfterFirstUpdate() throws Exception
  {
    assertEquals(2, repeater.getUpdatesPerSecond(), 0.1);

    repeater.doUpdate();

    assertEquals(20, repeater.getUpdatesPerSecond(), 0.1);
  }
  
  @Test
  public void resetUpdatesPErSecond() throws Exception
  {
    repeater.doUpdate();
    repeater.reset();

    assertEquals(2, repeater.getUpdatesPerSecond(), 0.1);
  }

  @Test
  public void acceleratesAfterSeveralUpdates() throws Exception
  {
    repeater.setScrollDelta(100);
    
    repeater.doUpdate();
    for(int i = 0; i < 10; i++)
      repeater.doUpdate();
    
    assertEquals(125, repeater.getScrollDelta());
  }

  @Test
  public void acceleratedMoreAfterSeveralMoreUpdates() throws Exception
  {
    repeater.setScrollDelta(100);

    repeater.doUpdate();
    for(int i = 0; i < 20; i++)
      repeater.doUpdate();
    
    assertEquals(156, repeater.getScrollDelta());
  }

  @Test
  public void wontExceedMaxAcceleration() throws Exception
  {
    repeater.setScrollDelta(100);

    repeater.doUpdate();
    for(int i = 0; i < 1000; i++)
      repeater.doUpdate();

    assertEquals(500, repeater.getScrollDelta());
  }
  
  @Test
  public void addingRepeatConditionPreventsScrolling() throws Exception
  {
    repeater.setScrollDelta(5);
    repeater.setScrollCondition(new ScrollRepeater.ScrollCondition(){
      public boolean canScroll()
      {
        return false;
      }
    });

    repeater.doUpdate();

    assertEquals(0, scrollBar.getValue());
  }
}

