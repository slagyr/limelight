//- Copyright 2008 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight;

import junit.framework.TestCase;
import limelight.ui.MockPanel;
import limelight.ui.Panel;
import limelight.ui.model.RootPanel;
import limelight.ui.model.MockFrame;
import limelight.ui.model.MockFrameManager;
import limelight.task.TaskEngine;
import limelight.styles.styling.RealStyleAttributeCompilerFactory;

public class AnimationTaskTest extends TestCase
{
  static
  {
    RealStyleAttributeCompilerFactory.install();
  }

  private MockPanel panel;
  private TestableAnimationTask task;
  private MockFrameManager frameManager;
  private TaskEngine taskEngine;

  public void setUp() throws Exception
  {
    panel = new MockPanel();
    task = new TestableAnimationTask("name", 2000, panel);

    frameManager = new MockFrameManager();
    Context.instance().frameManager = frameManager;
    taskEngine = new TaskEngine();
    Context.instance().taskEngine = taskEngine;
    Context.instance().panelPanter = new MockIdleLoopThread();
  }

  public void testIsStrict() throws Exception
  {
    assertEquals(true, task.isStrict());
  }

  private class TestableAnimationTask extends AnimationTask
  {

    public TestableAnimationTask(String name, int updatesPerSecond, Panel panel)
    {
      super(name, updatesPerSecond, panel);
    }

    protected void doPerform()
    {
    }

  }

//  public void testShouldPerformOnlyIfPanelIsPartOfActiveRoot() throws Exception
//  {
//    MockFrame frame = new MockFrame();
//    RootPanel root = new RootPanel(frame);
//    frame.setRoot(root);
//    frameManager.activeFrame = frame;
//
//    assertEquals(false, task.isReady());
//
//    root.setPanel(panel);
//    assertEquals(true, task.isReady());
//  }

  public void testStartAndStop() throws Exception
  {
    assertEquals(false, taskEngine.getTasks().contains(task));

    task.start();

    assertEquals(true, taskEngine.getTasks().contains(task));

    task.stop();

    assertEquals(false, taskEngine.getTasks().contains(task));
  }
}
