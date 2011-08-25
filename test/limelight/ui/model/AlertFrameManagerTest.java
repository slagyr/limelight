//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model;

import limelight.model.api.MockStudio;
import limelight.Context;
import limelight.MockContext;
import limelight.ui.events.stage.*;
import limelight.ui.model.inputs.MockEventAction;
import org.junit.Before;
import org.junit.Test;

import java.awt.event.WindowEvent;
import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;

public class AlertFrameManagerTest
{
  private static class HackedWindowEvent extends WindowEvent
  {
    public HackedWindowEvent(StageFrame frame)
    {
      super(frame, 1, 0, 1);
      source = frame;
    }
  }

  private AlertFrameManager manager;
  private StageFrame frame;
  private MockEventAction action;
  private MockStage stage;

  @Before
  public void setUp() throws Exception
  {
    StageFrame.hiddenMode = false;
    manager = new AlertFrameManager();
    Context.instance().frameManager = manager;
    Context.instance().studio = new MockStudio();
    Context.instance().environment = "test";
    stage = new MockStage();
    frame = new StageFrame(stage);
    action = new MockEventAction();
  }

  @Test
  public void closeAllFrames() throws Exception
  {
    manager.watch(frame);

    manager.closeAllFrames();

    assertEquals(true, stage.closed);
  }

  @Test
  public void shouldAskStageFrameIfItCanClose() throws Exception
  {
    stage.shouldAllowClose = false;
    manager.windowClosing(new HackedWindowEvent(frame));
    assertEquals(false, stage.closed);

    stage.shouldAllowClose = true;
    manager.windowClosing(new HackedWindowEvent(frame));
    assertEquals(true, stage.closed);
  }

  @Test
  public void checkingWithStudioBeforeShuttingDown() throws Exception
  {
    MockContext context = MockContext.stub();
    manager.watch(frame);
    manager.windowClosed(new HackedWindowEvent(frame));
    assertEquals(true, context.shutdownAttempted);
  }

  @Test
  public void shouldNotInvokeShutdownForNonVitalFrames() throws Exception
  {
    MockContext context = MockContext.stub();
    stage.setVital(false);
    manager.watch(frame);
    manager.windowClosed(new HackedWindowEvent(frame));
    assertEquals(false, context.shutdownAttempted);
  }
  
  @Test
  public void shouldInvokeShutdownWhenOnlyNonVitalFramesRemain() throws Exception
  {
    MockContext context = MockContext.stub();
    stage.setVital(false);
    StageFrame frame2 = new StageFrame(new MockStage());
    manager.watch(frame);
    manager.watch(frame2);

    manager.windowClosed(new HackedWindowEvent(frame2));
    assertEquals(true, context.shutdownAttempted);
  }

  @Test
  public void getActiveFrameWhenNoneHasGainedFocus() throws Exception
  {
    manager.watch(frame);
    assertEquals(null, manager.getActiveFrame());

    stage.setVisible(true);
    assertEquals(frame, manager.getActiveFrame());
  }

  @Test
  public void getVisibleFrames() throws Exception
  {
    ArrayList<StageFrame> result = new ArrayList<StageFrame>();
    stage.setVisible(true);
    final MockStage stage2 = new MockStage();
    StageFrame frame2 = new StageFrame(stage2);

    manager.getVisibleFrames(result);
    assertEquals(0, result.size());
    result.clear();

    manager.watch(frame);
    manager.getVisibleFrames(result);
    assertEquals(1, result.size());
    result.clear();

    stage2.setVisible(false);
    manager.watch(frame2);
    manager.getVisibleFrames(result);
    assertEquals(1, result.size());
    result.clear();

    stage2.setVisible(true);
    manager.getVisibleFrames(result);
    assertEquals(2, result.size());
    result.clear();
  }

  @Test
  public void frameIsNotifiedWhenOpened() throws Exception
  {
    stage.getEventHandler().add(StageOpenedEvent.class, action);

    manager.windowOpened(new HackedWindowEvent(frame));

    assertEquals(true, action.invoked);
  }

  @Test
  public void frameIsClosedWhenClosingEventIsReceived() throws Exception
  {
    stage.shouldAllowClose = true;
    manager.windowClosing(new HackedWindowEvent(frame));

    assertEquals(true, stage.closed);
  }

//  @Test
//  public void tellsKeyboradFocusManagerToReleaseStageUponClosing() throws Exception
//  {
//    KeyboardFocusManager keyboard = new KeyboardFocusManager();
//    Context.instance().keyboardFocusManager = keyboard;
//    keyboard.focusFrame(frame.getWindow());
//
//    manager.windowClosed(new HackedWindowEvent(frame));
//
//    assertEquals(null, keyboard.getFocusedWindow());
//    assertEquals(null, keyboard.getFocusedFrame());
//  }

  @Test
  public void stageNotifiedWhenActivated() throws Exception
  {
    stage.setVisible(true);
    stage.getEventHandler().add(StageActivatedEvent.class, action);

    manager.windowActivated(new HackedWindowEvent(frame));

    assertEquals(true, action.invoked);
  }

  @Test
  public void cantActivateFramesThatAreNotVisible() throws Exception
  {
    stage.setVisible(false);
    stage.getEventHandler().add(StageActivatedEvent.class, action);

    manager.windowActivated(new HackedWindowEvent(frame));
    assertEquals(false, action.invoked);
    assertEquals(null, manager.getActiveFrame());

    stage.setVisible(true);
    manager.windowActivated(new HackedWindowEvent(frame));
    assertEquals(true, action.invoked);
    assertEquals(frame, manager.getActiveFrame());
  }

  @Test
  public void stageNotifiedWhenDeactivated() throws Exception
  {
    stage.setVisible(true);
    manager.windowActivated(new HackedWindowEvent(frame));
    assertEquals(frame, manager.getActiveFrame());
    stage.getEventHandler().add(StageDeactivatedEvent.class, action);

    manager.windowDeactivated(new HackedWindowEvent(frame));

    assertEquals(true, action.invoked);
  }
  
  @Test
  public void cantDeactivateFramesThatAreNotActive() throws Exception
  {
    assertEquals(null, manager.getActiveFrame());
    stage.getEventHandler().add(StageDeactivatedEvent.class, action);

    manager.windowDeactivated(new HackedWindowEvent(frame));

    assertEquals(false, action.invoked);
  }

  @Test
  public void stageNotifiedWhenIconified() throws Exception
  {
    stage.getEventHandler().add(StageIconifiedEvent.class, action);

    manager.windowIconified(new HackedWindowEvent(frame));

    assertEquals(true, action.invoked);
  }

  @Test
  public void stageNotifiedWhenDeiconified() throws Exception
  {
    stage.getEventHandler().add(StageDeiconifiedEvent.class, action);

    manager.windowDeiconified(new HackedWindowEvent(frame));

    assertEquals(true, action.invoked);
  }

  @Test
  public void stageNotifiedWhenFocusGained() throws Exception
  {
    stage.getEventHandler().add(StageGainedFocusEvent.class, action);

    manager.windowGainedFocus(new HackedWindowEvent(frame));

    assertEquals(true, action.invoked);
  }
  
  @Test
  public void stageNotifiedWhenFocusLost() throws Exception
  {
    stage.getEventHandler().add(StageLostFocusEvent.class, action);

    manager.windowLostFocus(new HackedWindowEvent(frame));

    assertEquals(true, action.invoked);
  }
}
