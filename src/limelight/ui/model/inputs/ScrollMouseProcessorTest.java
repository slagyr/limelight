package limelight.ui.model.inputs;

import limelight.Context;
import limelight.background.AnimationLoop;
import limelight.ui.events.MouseDraggedEvent;
import limelight.ui.events.MouseExitedEvent;
import limelight.ui.events.MousePressedEvent;
import limelight.ui.events.MouseReleasedEvent;
import limelight.util.Box;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.awt.*;

import static junit.framework.Assert.assertEquals;

public class ScrollMouseProcessorTest
{
  private ScrollBarPanel scrollBar;
  private ScrollMouseProcessor processor;
  private ScrollRepeater repeater;

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
  }

  private void setUpHorizontally()
  {
    scrollBar = new ScrollBarPanel(ScrollBarPanel.HORIZONTAL);
    scrollBar.setSize(100, 15);
    scrollBar.configure(10, 100);
    processor = scrollBar.getMouseProcessor();
    repeater = processor.getRepeater();
  }

  private void setUpVertically()
  {
    scrollBar = new ScrollBarPanel(ScrollBarPanel.VERTICAL);
    scrollBar.setSize(15, 100);
    scrollBar.configure(10, 100);
    processor = scrollBar.getMouseProcessor();
    repeater = processor.getRepeater();
  }

  private void press(ScrollBarPanel scrollBar, int x, int y)
  {
    new MousePressedEvent(scrollBar, 0, new Point(x, y), 0).dispatch(scrollBar);
  }

  private void release(ScrollBarPanel scrollBar, int x, int y)
  {
    new MouseReleasedEvent(scrollBar, 0, new Point(x, y), 0).dispatch(scrollBar);
  }

  private void drag(ScrollBarPanel scrollBar, int x, int y)
  {
    new MouseDraggedEvent(scrollBar, 0, new Point(x, y), 0).dispatch(scrollBar);
  }

  private void exit(ScrollBarPanel scrollBar, int x, int y)
  {
    new MouseExitedEvent(scrollBar, 0, new Point(x, y), 0).dispatch(scrollBar);
  }

  @Test
  public void pressingHorizontalIncreaseScrollButtonWillIncrement() throws Exception
  {
    setUpHorizontally();
    assertEquals(false, scrollBar.isIncreasingButtonActive());

    press(scrollBar, 90, 7);

    assertEquals(true, scrollBar.isIncreasingButtonActive());
    assertEquals(5, scrollBar.getValue());
  }

  @Test
  public void pressingHorizontalDecreaseScrollButtonWillDecrement() throws Exception
  {               
    setUpHorizontally();
    scrollBar.setValue(50);
    assertEquals(false, scrollBar.isDecreasingButtonActive());

    press(scrollBar, 80, 7);

    assertEquals(true, scrollBar.isDecreasingButtonActive());
    assertEquals(45, scrollBar.getValue());
  }

  @Test
  public void pressingVerticalIncreaseScrollButtonWillIncrement() throws Exception
  {
    setUpVertically();
    assertEquals(false, scrollBar.isIncreasingButtonActive());

    press(scrollBar, 7, 90);

    assertEquals(true, scrollBar.isIncreasingButtonActive());
    assertEquals(5, scrollBar.getValue());
  }

  @Test
  public void pressingVerticalDecreaseScrollButtonWillDecrement() throws Exception
  {
    setUpVertically();
    scrollBar.setValue(50);
    assertEquals(false, scrollBar.isDecreasingButtonActive());

    press(scrollBar, 7, 80);

    assertEquals(true, scrollBar.isDecreasingButtonActive());
    assertEquals(45, scrollBar.getValue());
  }

  @Test
  public void pressingHorizontalIncreaseScrollButtonWillStartRepeater() throws Exception
  {             
    setUpHorizontally();
    assertEquals(false, repeater.isRunning());

    press(scrollBar, 90, 7);

    assertEquals(true, repeater.isRunning());
    assertEquals(scrollBar.getUnitIncrement(), repeater.getScrollDelta());
  }

  @Test
  public void pressingHorizontalDecreaseScrollButtonWillStartRepeater() throws Exception
  {        
    setUpHorizontally();
    assertEquals(false, repeater.isRunning());

    press(scrollBar, 80, 7);

    assertEquals(true, repeater.isRunning());
    assertEquals(-scrollBar.getUnitIncrement(), repeater.getScrollDelta());
  }

  @Test
  public void pressingVerticalIncreaseScrollButtonWillStartRepeater() throws Exception
  {
    setUpVertically();
    assertEquals(false, repeater.isRunning());

    press(scrollBar, 7, 90);

    assertEquals(true, repeater.isRunning());
    assertEquals(scrollBar.getUnitIncrement(), repeater.getScrollDelta());
  }

  @Test
  public void pressingVerticalDecreaseScrollButtonWillStartRepeater() throws Exception
  {
    setUpVertically();
    assertEquals(false, repeater.isRunning());

    press(scrollBar, 7, 80);

    assertEquals(true, repeater.isRunning());
    assertEquals(-scrollBar.getUnitIncrement(), repeater.getScrollDelta());
  }
  
  @Test
  public void releasingMouseWillStopRepeaterWhenIncreasing() throws Exception
  {
    setUpVertically();
    press(scrollBar, 7, 80);
    release(scrollBar, 0, 0);

    assertEquals(false, processor.getRepeater().isRunning());
    assertEquals(false, scrollBar.isDecreasingButtonActive());
  }
  
  @Test
  public void releasingMouseWillStopRepeaterWhenDecreasing() throws Exception
  {
    setUpVertically();
    press(scrollBar, 7, 90);
    release(scrollBar, 0, 0);

    assertEquals(false, processor.getRepeater().isRunning());
    assertEquals(false, scrollBar.isIncreasingButtonActive());
  }

  @Test
  public void pressingTrackIncreasesByBlockVertically() throws Exception
  {
    setUpVertically();
    press(scrollBar, 7, 60);

    assertEquals(scrollBar.getBlockIncrement(), scrollBar.getValue());
  }

  @Test
  public void pressingTrackDecreasesByBlockVertically() throws Exception
  {
    setUpVertically();
    scrollBar.setValue(scrollBar.getMaxValue());
    press(scrollBar, 7, 10);

    assertEquals(scrollBar.getMaxValue() - scrollBar.getBlockIncrement(), scrollBar.getValue());
  }

  @Test
  public void pressingTrackIncreasesByBlockHorizontally() throws Exception
  {          
    setUpHorizontally();
    press(scrollBar, 60, 7);

    assertEquals(scrollBar.getBlockIncrement(), scrollBar.getValue());
  }
  
  @Test
  public void pressingTrackDecreasesByBlockHorizontally() throws Exception
  {               
    setUpHorizontally();
    scrollBar.setValue(scrollBar.getMaxValue());
    press(scrollBar, 10, 7);

    assertEquals(scrollBar.getMaxValue() - scrollBar.getBlockIncrement(), scrollBar.getValue());
  }

  @Test
  public void pressingHorizontalTrackToIncreaseWillStartRepeater() throws Exception
  {        
    setUpHorizontally();
    assertEquals(false, repeater.isRunning());

    press(scrollBar, 60, 7);

    assertEquals(true, repeater.isRunning());
    assertEquals(scrollBar.getBlockIncrement(), repeater.getScrollDelta());
    assertEquals(processor.getNotInSliderScrollCondition(), repeater.getScrollCondition());
  }

  @Test
  public void pressingHorizontalTrackToDecreaseWillStartRepeater() throws Exception
  {              
    setUpHorizontally();
    scrollBar.setValue(scrollBar.getMaxValue());
    assertEquals(false, repeater.isRunning());

    press(scrollBar, 10, 7);

    assertEquals(true, repeater.isRunning());
    assertEquals(-1 * scrollBar.getBlockIncrement(), repeater.getScrollDelta());
    assertEquals(processor.getNotInSliderScrollCondition(), repeater.getScrollCondition());
  }

  @Test
  public void pressingVerticalTrackToIncreaseWillStartRepeater() throws Exception
  {
    setUpVertically();
    assertEquals(false, repeater.isRunning());

    press(scrollBar, 7, 60);

    assertEquals(true, repeater.isRunning());
    assertEquals(scrollBar.getBlockIncrement(), repeater.getScrollDelta());
    assertEquals(processor.getNotInSliderScrollCondition(), repeater.getScrollCondition());
  }

  @Test
  public void pressingVerticalTrackToDecreaseWillStartRepeater() throws Exception
  {
    setUpVertically();
    scrollBar.setValue(scrollBar.getMaxValue());
    assertEquals(false, repeater.isRunning());

    press(scrollBar, 7, 10);

    assertEquals(true, repeater.isRunning());
    assertEquals(-1 * scrollBar.getBlockIncrement(), repeater.getScrollDelta());
    assertEquals(processor.getNotInSliderScrollCondition(), repeater.getScrollCondition());
  }

  @Test
  public void remembersMousePosition() throws Exception
  {
    setUpVertically();
    press(scrollBar, 7, 60);
    
    assertEquals(new Point(7, 60), processor.getMouseLocation());
  }

  @Test
  public void mouseInSliderScrollConditionVertical() throws Exception
  {
    setUpVertically();
    scrollBar.setValue(10);
    
    Box sliderBounds = scrollBar.getSliderBounds();
    final ScrollRepeater.ScrollCondition condition = processor.getNotInSliderScrollCondition();

    processor.setMouseLocation(7, sliderBounds.top() - 1);
    assertEquals(true, condition.canScroll());

    processor.setMouseLocation(7, sliderBounds.top() + 1);
    assertEquals(false, condition.canScroll());

    processor.setMouseLocation(7, sliderBounds.bottom() - 1);
    assertEquals(false, condition.canScroll());

    processor.setMouseLocation(7, sliderBounds.bottom() + 1);
    assertEquals(true, condition.canScroll());
  }
  
  @Test
  public void mouseInSliderScrollConditionHorizontal() throws Exception
  {            
    setUpHorizontally();
    scrollBar.setValue(10);

    Box sliderBounds = scrollBar.getSliderBounds();
    final ScrollRepeater.ScrollCondition condition = processor.getNotInSliderScrollCondition();

    processor.setMouseLocation(sliderBounds.left() - 1, 7);
    assertEquals(true, condition.canScroll());

    processor.setMouseLocation(sliderBounds.left() + 1, 7);
    assertEquals(false, condition.canScroll());

    processor.setMouseLocation(sliderBounds.right() - 1, 7);
    assertEquals(false, condition.canScroll());

    processor.setMouseLocation(sliderBounds.right() + 1, 7);
    assertEquals(true, condition.canScroll());
  }

  @Test
  public void startDraggingSlider() throws Exception
  {              
    setUpHorizontally();
    press(scrollBar, 10, 7);
    assertEquals(true, processor.isSliderDragOn());

    drag(scrollBar, 11, 7);

    assertEquals(scrollBar.getMinSliderPosition() + 1, scrollBar.getSliderPosition());
  }

  @Test
  public void stopsDraggingWhenNotNotPressedInSlider() throws Exception
  {                
    setUpHorizontally();
    press(scrollBar, 10, 7);
    assertEquals(true, processor.isSliderDragOn());

    press(scrollBar, 1, 7);
    assertEquals(false, processor.isSliderDragOn());
  }

  @Test
  public void repeaterStopsWhenIncreasingButtonIsPressedAndMouseExits() throws Exception
  {            
    setUpHorizontally();
    press(scrollBar, 90, 7);
    assertEquals(true, processor.isUnitIncrementOn());
    exit(scrollBar, -1, -1);
    
    assertEquals(false, scrollBar.isIncreasingButtonActive());
    assertEquals(false, repeater.isRunning());
  }
  
  @Test
  public void repeaterStopsWhenDecreasingButtonIsPressedAndMouseExits() throws Exception
  {               
    setUpHorizontally();
    press(scrollBar, 80, 7);
    exit(scrollBar, -1, -1);

    assertEquals(false, scrollBar.isDecreasingButtonActive());
    assertEquals(false, repeater.isRunning());
  }
  
  @Test
  public void draggingFromDecreasingToIncreasingReversedRepeaterDelta() throws Exception
  {
    setUpHorizontally();
    press(scrollBar, 80, 7);
    drag(scrollBar, 90, 7);

    assertEquals(true, repeater.isRunning());
    assertEquals(scrollBar.getUnitIncrement(), repeater.getScrollDelta());
    assertEquals(false, scrollBar.isDecreasingButtonActive());
    assertEquals(true, scrollBar.isIncreasingButtonActive());
  }

  @Test
  public void draggingFromIncreasingToDecreasingReversedRepeaterDelta() throws Exception
  {
    setUpHorizontally();
    press(scrollBar, 90, 7);
    drag(scrollBar, 80, 7);

    assertEquals(true, repeater.isRunning());
    assertEquals(-scrollBar.getUnitIncrement(), repeater.getScrollDelta());
    assertEquals(false, scrollBar.isIncreasingButtonActive());
    assertEquals(true, scrollBar.isDecreasingButtonActive());
  }

  @Test
  public void draggingMouseOutOfDecreasingButtonShouldStopRepeater() throws Exception
  {
    setUpHorizontally();
    press(scrollBar, 90, 7);
    drag(scrollBar, 50, 7);

    assertEquals(false, repeater.isRunning());
    assertEquals(false, scrollBar.isIncreasingButtonActive());
  }
  
  @Test
  public void draggingMouseOutOfIncreasingButtonShouldStopRepeater() throws Exception
  {
    setUpHorizontally();
    press(scrollBar, 80, 7);
    drag(scrollBar, 50, 7);

    assertEquals(false, repeater.isRunning());
    assertEquals(false, scrollBar.isDecreasingButtonActive());
  }

  @Test
  public void draggingMouseOutOfButtonsAndBackInOppositeDirectionWillResumeRepeater() throws Exception
  {
    setUpHorizontally();
    press(scrollBar, 80, 7);
    drag(scrollBar, 50, 7); 
    drag(scrollBar, 90, 7);

    assertEquals(true, repeater.isRunning());
    assertEquals(true, scrollBar.isIncreasingButtonActive());
  }
  
  @Test
  public void draggingMouseOutOfButtonsAndBackInSameDirectionWillResumeRepeater() throws Exception
  {
    setUpHorizontally();
    press(scrollBar, 80, 7);
    drag(scrollBar, 50, 7);
    drag(scrollBar, 80, 7);

    assertEquals(true, repeater.isRunning());
    assertEquals(true, scrollBar.isDecreasingButtonActive());
  }

  @Test
  public void draggingToIncreaseToDecreaseSideOfTrackWillChangeDirectionOfBlockRepeater() throws Exception
  {
    setUpVertically();

    press(scrollBar, 7, 60);
    assertEquals(true, processor.isBlockIncrementOn());
    drag(scrollBar, 7, 10);

    assertEquals(true, repeater.isRunning());
    assertEquals(scrollBar.getBlockIncrement(), repeater.getScrollDelta());
  }

  @Test
  public void draggingToDecreaseToIncreaseSideOfTrackWillChangeDirectionOfBlockRepeater() throws Exception
  {
    setUpVertically();
    scrollBar.setValue(scrollBar.getMaxValue());

    press(scrollBar, 7, 10);
    drag(scrollBar, 7, 60);

    assertEquals(true, repeater.isRunning());
    assertEquals(-scrollBar.getBlockIncrement(), repeater.getScrollDelta());
  }

  @Test
  public void draggingOutOfTheTrackStopsTheRepeater() throws Exception
  {
    setUpVertically();

    press(scrollBar, 7, 60);
    drag(scrollBar, -1, -1);

    assertEquals(false, repeater.isRunning());
  }
  
  @Test
  public void draggingOutOfTheTrackThenBackInResumeRepeater() throws Exception
  {
    setUpVertically();

    press(scrollBar, 7, 60);
    drag(scrollBar, -1, -1);
    drag(scrollBar, 7, 60);

    assertEquals(true, repeater.isRunning());
  }
}
