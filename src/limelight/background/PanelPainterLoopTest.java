//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.background;

import junit.framework.TestCase;
import limelight.ui.model.MockFrameManager;
import limelight.ui.model.MockStageFrame;
import limelight.ui.model.RootPanel;
import limelight.ui.MockPanel;
import limelight.Context;

import java.awt.*;

public class PanelPainterLoopTest extends TestCase
{
  private PanelPainterLoop loop;
  private MockFrameManager frameManager;
  private MockStageFrame activeFrame;
  private RootPanel activeRoot;

  public void setUp() throws Exception
  {
    loop = new PanelPainterLoop();
    frameManager = new MockFrameManager();
    Context.instance().frameManager = frameManager;
    activeFrame = new MockStageFrame();
    activeRoot = new RootPanel(activeFrame);
    activeFrame.setRoot(activeRoot);
  }

  public void testIsAnIdleThreadLoop() throws Exception
  {
    assertEquals(true, loop instanceof IdleThreadLoop);
  }

  public void testGettingRootWhenNoFrameIsActive() throws Exception
  {
    assertEquals(null, loop.getActiveRoot());
  }
  
  public void testGetRootWithActiveFrame() throws Exception
  {
    frameManager.focusedFrame = activeFrame;

    assertEquals(activeFrame.getRoot(), loop.getActiveRoot());
  }
  
  public void testShouldIdleWhenThereIsNoRoot() throws Exception
  {
    assertEquals(true, loop.shouldBeIdle());
  }
  
  public void testShouldBeIdleWhenRootHasNoPanelsNeedingLayoutsOrDirtyRegions() throws Exception
  {
    frameManager.focusedFrame = activeFrame;
    assertEquals(false, activeFrame.getRoot().hasPanelsNeedingLayout());
    assertEquals(false, activeFrame.getRoot().hasDirtyRegions());
    assertEquals(true, loop.shouldBeIdle());
  }

  public void testShouldNotBeIdleWhenPanelsNeedLayout() throws Exception
  {
    frameManager.focusedFrame = activeFrame;
    activeRoot.addPanelNeedingLayout(new MockPanel());

    assertEquals(false, loop.shouldBeIdle());
  }

  public void testShouldNotBeIdleWhenThereAreDirtyRegions() throws Exception
  {
    frameManager.focusedFrame = activeFrame;
    activeRoot.addDirtyRegion(new Rectangle(0, 0, 1, 1));

    assertEquals(false, loop.shouldBeIdle());
  }
  
  public void testDoLayouts() throws Exception
  {
    MockPanel panel1 = new MockPanel();
    MockPanel panel2 = new MockPanel();
    activeRoot.addPanelNeedingLayout(panel1);
    activeRoot.addPanelNeedingLayout(panel2);

    loop.doAllLayouts(activeRoot);

    assertEquals(true, panel1.wasLaidOut);
    assertEquals(true, panel2.wasLaidOut);
    assertEquals(false, activeRoot.hasPanelsNeedingLayout());
  }

// TODO Perhaps we need a PaintJobFactory with a PaintJob interface  
//  public void testPaintDirtyRegions() throws Exception
//  {
//    activeRoot.addDirtyRegion(new Rectangle(0, 0, 10, 10));
//
//    loop.paintDirtyRegions(activeRoot);
//
//    assertEquals(false, activeRoot.hasDirtyRegions());
//  }

  public void testUpdatesPerSecond() throws Exception
  {
    assertEquals(30, loop.getUpdatesPerSecond());
    assertEquals(1000000000 / 30, loop.getOptimalDelayTimeNanos());

    loop.setUpdatesPerSecond(60);
    assertEquals(60, loop.getUpdatesPerSecond());
    assertEquals(1000000000 / 60, loop.getOptimalDelayTimeNanos());
  }
}

