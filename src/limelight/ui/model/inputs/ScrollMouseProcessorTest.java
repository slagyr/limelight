package limelight.ui.model.inputs;

import limelight.Context;
import limelight.background.AnimationLoop;
import limelight.util.Box;
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

  private void drag(ScrollBar2Panel scrollBar, int x, int y)
  {
    scrollBar.mouseDragged(event(x, y));
  }

  @Test
  public void pressingHorizontalIncreaseScrollButtonWillIncrement() throws Exception
  {
    assertEquals(false, horizontalScrollBar.isIncreasingButtonActive());

    press(horizontalScrollBar, 90, 7);

    assertEquals(true, horizontalScrollBar.isIncreasingButtonActive());
    assertEquals(5, horizontalScrollBar.getValue());
  }

  @Test
  public void pressingHorizontalDecreaseScrollButtonWillDecrement() throws Exception
  {
    horizontalScrollBar.setValue(50);
    assertEquals(false, horizontalScrollBar.isDecreasingButtonActive());

    press(horizontalScrollBar, 80, 7);

    assertEquals(true, horizontalScrollBar.isDecreasingButtonActive());
    assertEquals(45, horizontalScrollBar.getValue());
  }

  @Test
  public void pressingVerticalIncreaseScrollButtonWillIncrement() throws Exception
  {
    assertEquals(false, verticalScrollBar.isIncreasingButtonActive());

    press(verticalScrollBar, 7, 90);

    assertEquals(true, verticalScrollBar.isIncreasingButtonActive());
    assertEquals(5, verticalScrollBar.getValue());
  }

  @Test
  public void pressingVerticalDecreaseScrollButtonWillDecrement() throws Exception
  {
    verticalScrollBar.setValue(50);
    assertEquals(false, verticalScrollBar.isDecreasingButtonActive());

    press(verticalScrollBar, 7, 80);

    assertEquals(true, verticalScrollBar.isDecreasingButtonActive());
    assertEquals(45, verticalScrollBar.getValue());
  }

  @Test
  public void pressingHorizontalIncreaseScrollButtonWillStartRepeater() throws Exception
  {
    ScrollRepeater repeater = horizontalProcessor.getRepeater();
    assertEquals(false, repeater.isRunning());

    press(horizontalScrollBar, 90, 7);

    assertEquals(true, repeater.isRunning());
    assertEquals(horizontalScrollBar.getUnitIncrement(), repeater.getScrollDelta());
  }

  @Test
  public void pressingHorizontalDecreaseScrollButtonWillStartRepeater() throws Exception
  {
    ScrollRepeater repeater = horizontalProcessor.getRepeater();
    assertEquals(false, repeater.isRunning());

    press(horizontalScrollBar, 80, 7);

    assertEquals(true, repeater.isRunning());
    assertEquals(-horizontalScrollBar.getUnitIncrement(), repeater.getScrollDelta());
  }

  @Test
  public void pressingVerticalIncreaseScrollButtonWillStartRepeater() throws Exception
  {
    ScrollRepeater repeater = verticalProcessor.getRepeater();
    assertEquals(false, repeater.isRunning());

    press(verticalScrollBar, 7, 90);

    assertEquals(true, repeater.isRunning());
    assertEquals(verticalScrollBar.getUnitIncrement(), repeater.getScrollDelta());
  }

  @Test
  public void pressingVerticalDecreaseScrollButtonWillStartRepeater() throws Exception
  {
    ScrollRepeater repeater = verticalProcessor.getRepeater();
    assertEquals(false, repeater.isRunning());

    press(verticalScrollBar, 7, 80);

    assertEquals(true, repeater.isRunning());
    assertEquals(-verticalScrollBar.getUnitIncrement(), repeater.getScrollDelta());
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

  @Test
  public void pressingTrackIncreasesByBlockVertically() throws Exception
  {
    press(verticalScrollBar, 7, 60);

    assertEquals(verticalScrollBar.getBlockIncrement(), verticalScrollBar.getValue());
  }

  @Test
  public void pressingTrackDecreasesByBlockVertically() throws Exception
  {
    verticalScrollBar.setValue(verticalScrollBar.getMaxValue());
    press(verticalScrollBar, 7, 10);

    assertEquals(verticalScrollBar.getMaxValue() - verticalScrollBar.getBlockIncrement(), verticalScrollBar.getValue());
  }

  @Test
  public void pressingTrackIncreasesByBlockHorizontally() throws Exception
  {
    press(horizontalScrollBar, 60, 7);

    assertEquals(horizontalScrollBar.getBlockIncrement(), horizontalScrollBar.getValue());
  }
  
  @Test
  public void pressingTrackDecreasesByBlockHorizontally() throws Exception
  {
    horizontalScrollBar.setValue(horizontalScrollBar.getMaxValue());
    press(horizontalScrollBar, 10, 7);

    assertEquals(horizontalScrollBar.getMaxValue() - horizontalScrollBar.getBlockIncrement(), horizontalScrollBar.getValue());
  }

  @Test
  public void pressingHorizontalTrackToIncreaseWillStartRepeater() throws Exception
  {
    ScrollRepeater repeater = horizontalProcessor.getRepeater();
    assertEquals(false, repeater.isRunning());

    press(horizontalScrollBar, 60, 7);

    assertEquals(true, repeater.isRunning());
    assertEquals(horizontalScrollBar.getBlockIncrement(), repeater.getScrollDelta());
    assertEquals(horizontalProcessor.getNotInSliderScrollCondition(), repeater.getScrollCondition());
  }

  @Test
  public void pressingHorizontalTrackToDecreaseWillStartRepeater() throws Exception
  {
    horizontalScrollBar.setValue(horizontalScrollBar.getMaxValue());
    ScrollRepeater repeater = horizontalProcessor.getRepeater();
    assertEquals(false, repeater.isRunning());

    press(horizontalScrollBar, 10, 7);

    assertEquals(true, repeater.isRunning());
    assertEquals(-1 * horizontalScrollBar.getBlockIncrement(), repeater.getScrollDelta());
    assertEquals(horizontalProcessor.getNotInSliderScrollCondition(), repeater.getScrollCondition());
  }

  @Test
  public void pressingVerticalTrackToIncreaseWillStartRepeater() throws Exception
  {
    ScrollRepeater repeater = verticalProcessor.getRepeater();
    assertEquals(false, repeater.isRunning());

    press(verticalScrollBar, 7, 60);

    assertEquals(true, repeater.isRunning());
    assertEquals(verticalScrollBar.getBlockIncrement(), repeater.getScrollDelta());
    assertEquals(verticalProcessor.getNotInSliderScrollCondition(), repeater.getScrollCondition());
  }

  @Test
  public void pressingVerticalTrackToDecreaseWillStartRepeater() throws Exception
  {
    verticalScrollBar.setValue(verticalScrollBar.getMaxValue());
    ScrollRepeater repeater = verticalProcessor.getRepeater();
    assertEquals(false, repeater.isRunning());

    press(verticalScrollBar, 7, 10);

    assertEquals(true, repeater.isRunning());
    assertEquals(-1 * verticalScrollBar.getBlockIncrement(), repeater.getScrollDelta());
    assertEquals(verticalProcessor.getNotInSliderScrollCondition(), repeater.getScrollCondition());
  }

  @Test
  public void remembersMousePosition() throws Exception
  {
    press(verticalScrollBar, 7, 60);
    
    assertEquals(new Point(7, 60), verticalProcessor.getMouseLocation());
  }

  @Test
  public void mouseInSliderScrollConditionVertical() throws Exception
  {
    verticalScrollBar.configure(10, 100);
    verticalScrollBar.setValue(10);
    
    Box sliderBounds = verticalScrollBar.getSliderBounds();
    final ScrollRepeater.ScrollCondition condition = verticalProcessor.getNotInSliderScrollCondition();

    verticalProcessor.setMouseLocation(7, sliderBounds.top() - 1);
    assertEquals(true, condition.canScroll());

    verticalProcessor.setMouseLocation(7, sliderBounds.top() + 1);
    assertEquals(false, condition.canScroll());

    verticalProcessor.setMouseLocation(7, sliderBounds.bottom() - 1);
    assertEquals(false, condition.canScroll());

    verticalProcessor.setMouseLocation(7, sliderBounds.bottom() + 1);
    assertEquals(true, condition.canScroll());
  }
  
  @Test
  public void mouseInSliderScrollConditionHorizontal() throws Exception
  {
    horizontalScrollBar.configure(10, 100);
    horizontalScrollBar.setValue(10);

    Box sliderBounds = horizontalScrollBar.getSliderBounds();
    final ScrollRepeater.ScrollCondition condition = horizontalProcessor.getNotInSliderScrollCondition();

    horizontalProcessor.setMouseLocation(sliderBounds.left() - 1, 7);
    assertEquals(true, condition.canScroll());

    horizontalProcessor.setMouseLocation(sliderBounds.left() + 1, 7);
    assertEquals(false, condition.canScroll());

    horizontalProcessor.setMouseLocation(sliderBounds.right() - 1, 7);
    assertEquals(false, condition.canScroll());

    horizontalProcessor.setMouseLocation(sliderBounds.right() + 1, 7);
    assertEquals(true, condition.canScroll());
  }

  @Test
  public void startDraggingSlider() throws Exception
  {
    horizontalScrollBar.configure(10, 100);
    
    press(horizontalScrollBar, 10, 7);
    assertEquals(true, horizontalProcessor.isSliderDragOn());

    drag(horizontalScrollBar, 11, 7);

    assertEquals(horizontalScrollBar.getMinSliderPosition() + 1, horizontalScrollBar.getSliderPosition());
  }

  @Test
  public void releasingMouseEndsDrag() throws Exception
  {
    horizontalScrollBar.configure(10, 100);

    press(horizontalScrollBar, 10, 7);
    assertEquals(true, horizontalProcessor.isSliderDragOn());

    release(horizontalScrollBar, 0, 0);
    assertEquals(false, horizontalProcessor.isSliderDragOn());
  }
}
