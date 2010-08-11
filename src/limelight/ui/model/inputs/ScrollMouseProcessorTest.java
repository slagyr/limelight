package limelight.ui.model.inputs;

import limelight.Context;
import limelight.background.AnimationLoop;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.awt.*;
import java.awt.event.MouseEvent;

import static junit.framework.Assert.assertEquals;

public class ScrollMouseProcessorTest
{
  private ScrollBar2Panel verticalScrollBar;
  private ScrollBar2Panel horizontalScrollBar;
  private Panel panel;
  private ScrollMouseProcessor verticalProcessor;
  private ScrollMouseProcessor horizontalProcessor;

  @BeforeClass
  public static void suiteSetup() throws Exception
  {
    Context.instance().animationLoop = new AnimationLoop();
  }

  @AfterClass
  public static void suiteTeardown() throws Exception
  {
    Context.instance().animationLoop = null;
  }

  @Before
  public void setUp() throws Exception
  {
    verticalScrollBar = new ScrollBar2Panel(ScrollBar2Panel.VERTICAL);
    verticalScrollBar.setSize(15, 100);
    verticalScrollBar.configure(10, 100);
    verticalProcessor = verticalScrollBar.getMouseProcessor();

    horizontalScrollBar = new ScrollBar2Panel(ScrollBar2Panel.HORIZONTAL);
    horizontalScrollBar.setSize(100, 15);
    horizontalScrollBar.configure(10, 100);
    horizontalProcessor = horizontalScrollBar.getMouseProcessor();

    panel = new Panel();
  }

  private MouseEvent event(int x, int y)
  {
    return new MouseEvent(panel, 1, 2, 3, x, y, 0, false);
  }

  private void press(ScrollBar2Panel scrollBar, int x, int y)
  {
    scrollBar.mousePressed(event(x, y));
  }

  private void release(ScrollBar2Panel scrollBar, int x, int y)
  {
    scrollBar.mouseReleased(event(x, y));
  }

  @Test
  public void pressingHorizontalIncreaseScrollButtonWillStartRepeater() throws Exception
  {
    ScrollRepeater repeater = horizontalProcessor.getRepeater();
    assertEquals(false, horizontalScrollBar.isIncreasingButtonActive());
    assertEquals(false, repeater.isRunning());

    press(horizontalScrollBar, 90, 7);

    assertEquals(true, repeater.isRunning());
//    assertEquals(5, repeater.getUpdatesPerSecond(), 0.01);
    assertEquals(horizontalScrollBar.getUnitIncrement(), repeater.getScrollDelta());
    assertEquals(true, horizontalScrollBar.isIncreasingButtonActive());
  }

  @Test
  public void pressingHorizontalDecreaseScrollButtonWillStartRepeater() throws Exception
  {
    ScrollRepeater repeater = horizontalProcessor.getRepeater();
    assertEquals(false, horizontalScrollBar.isDecreasingButtonActive());
    assertEquals(false, repeater.isRunning());

    press(horizontalScrollBar, 80, 7);

    assertEquals(true, repeater.isRunning());
//    assertEquals(5, repeater.getUpdatesPerSecond(), 0.01);
    assertEquals(-horizontalScrollBar.getUnitIncrement(), repeater.getScrollDelta());
    assertEquals(true, horizontalScrollBar.isDecreasingButtonActive());
  }

  @Test
  public void pressingVerticalIncreaseScrollButtonWillStartRepeater() throws Exception
  {
    ScrollRepeater repeater = verticalProcessor.getRepeater();
    assertEquals(false, verticalScrollBar.isIncreasingButtonActive());
    assertEquals(false, repeater.isRunning());

    press(verticalScrollBar, 7, 90);

    assertEquals(true, repeater.isRunning());
//    assertEquals(5, repeater.getUpdatesPerSecond(), 0.01);
    assertEquals(verticalScrollBar.getUnitIncrement(), repeater.getScrollDelta());
    assertEquals(true, verticalScrollBar.isIncreasingButtonActive());
  }

  @Test
  public void pressingVerticalDecreaseScrollButtonWillStartRepeater() throws Exception
  {
    ScrollRepeater repeater = verticalProcessor.getRepeater();
    assertEquals(false, verticalScrollBar.isDecreasingButtonActive());
    assertEquals(false, repeater.isRunning());

    press(verticalScrollBar, 7, 80);

    assertEquals(true, repeater.isRunning());
//    assertEquals(5, repeater.getUpdatesPerSecond(), 0.01);
    assertEquals(-verticalScrollBar.getUnitIncrement(), repeater.getScrollDelta());
    assertEquals(true, verticalScrollBar.isDecreasingButtonActive());
  }
  
  @Test
  public void releasingMouseWillStopRepeaterWhenIncreasing() throws Exception
  {
    press(verticalScrollBar, 7, 80);
    release(verticalScrollBar, 0, 0);

    assertEquals(false, verticalProcessor.getRepeater().isRunning());
    assertEquals(false, verticalScrollBar.isDecreasingButtonActive());
  }
  
  @Test
  public void releasingMouseWillStopRepeaterWhenDecreasing() throws Exception
  {
    press(verticalScrollBar, 7, 90);
    release(verticalScrollBar, 0, 0);

    assertEquals(false, verticalProcessor.getRepeater().isRunning());
    assertEquals(false, verticalScrollBar.isIncreasingButtonActive());
  }
}
