package limelight.ui;

import junit.framework.TestCase;

import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

public class FrameWatcherTest extends TestCase
{
  private FrameWatcher watcher;
  private MockTheater theater;
  private Frame frame;
  private MockStage stage;

  public void setUp() throws Exception
  {
    theater = new MockTheater();
    watcher = new FrameWatcher(theater);
    stage = new MockStage();
    frame = new Frame(stage);
  }

  public void testAddingFrames() throws Exception
  {
    watcher.watch(frame);

    assertEquals(1, frame.getWindowFocusListeners().length);
    assertEquals(watcher, frame.getWindowFocusListeners()[0]);
  }
  
  public void testFocusingOnAWindow() throws Exception
  {
    watcher.watch(frame);

    WindowEvent event = new WindowEvent(frame, 123);
    for (WindowFocusListener listener : frame.getWindowFocusListeners())
      listener.windowGainedFocus(event);

    assertSame(stage, theater.activatedStage);
  }

}
