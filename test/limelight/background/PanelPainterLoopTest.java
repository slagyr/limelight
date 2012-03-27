//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.background;

import limelight.ui.BufferedImagePool;
import limelight.ui.model.*;
import limelight.ui.MockPanel;
import limelight.Context;
import limelight.util.TestUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assume.assumeTrue;

public class PanelPainterLoopTest extends Assert
{
  private PanelPainterLoop loop;
  private MockFrameManager frameManager;
  private StageFrame activeFrame;
  private Scene activeRoot;

  @Before
  public void setUp() throws Exception
  {
    assumeTrue(TestUtil.notHeadless());
    loop = new PanelPainterLoop();
    frameManager = new MockFrameManager();
    Context.instance().frameManager = frameManager;
    MockStage activeStage = new MockStage();
    activeFrame = new StageFrame(activeStage);
    activeRoot = new FakeScene();
    activeRoot.setStage(activeStage);
    activeStage.setScene(activeRoot);
  }

  @Test
  public void gettingRootWhenNoFrameIsActive() throws Exception
  {
    assertEquals(null, loop.getActiveRoot());
  }

  @Test
  public void getRootWithActiveFrame() throws Exception
  {
    frameManager.focusedFrame = activeFrame;

    assertEquals(activeRoot, loop.getActiveRoot());
  }

  @Test
  public void idleWhenThereIsNoRoot() throws Exception
  {
    assertEquals(true, loop.shouldBeIdle());
  }

  @Test
  public void isIdleWhenRootHasNoPanelsNeedingLayoutsOrDirtyRegions() throws Exception
  {
    activeRoot.getAndClearDirtyRegions(new ArrayList<Rectangle>());
    activeRoot.resetLayoutRequired();

    frameManager.focusedFrame = activeFrame;

    assertEquals(false, activeRoot.isLayoutRequired());
    assertEquals(false, activeRoot.hasDirtyRegions());
    assertEquals(true, loop.shouldBeIdle());
  }

  @Test
  public void isNotIdleWhenPanelsNeedLayout() throws Exception
  {
    frameManager.focusedFrame = activeFrame;
    activeRoot.layoutRequired();

    assertEquals(false, loop.shouldBeIdle());
  }

  @Test
  public void isNotIdleWhenThereAreDirtyRegions() throws Exception
  {
    frameManager.focusedFrame = activeFrame;
    activeRoot.addDirtyRegion(new Rectangle(0, 0, 1, 1));

    assertEquals(false, loop.shouldBeIdle());
  }

  @Test
  public void doesLayouts() throws Exception
  {
    MockPanel panel1 = new MockPanel();
    MockPanel panel2 = new MockPanel();
    activeRoot.add(panel1);
    activeRoot.add(panel2);
    final FakeLayout layout1 = new FakeLayout(true);
    final FakeLayout layout2 = new FakeLayout(true);
    panel1.markAsNeedingLayout(layout1);
    panel2.markAsNeedingLayout(layout2);

    loop.doAllLayouts(activeRoot);

    assertEquals(panel1, layout1.lastPanelExpanded);
    assertEquals(panel2, layout2.lastPanelExpanded);
    assertEquals(false, activeRoot.isLayoutRequired());
  }

  @Test
  public void paintsDirtyRegions() throws Exception
  {
    Context.instance().bufferedImagePool = new BufferedImagePool(0.1);
    activeRoot.addDirtyRegion(new Rectangle(0, 0, 10, 10));

    loop.paintDirtyRegions(activeRoot);

    assertEquals(false, activeRoot.hasDirtyRegions());
  }

  @Test
  public void updatesPerSecond() throws Exception
  {
    assertEquals(30, loop.getUpdatesPerSecond());
    assertEquals(1000000000 / 30, loop.getOptimalDelayTimeNanos());

    loop.setUpdatesPerSecond(60);
    assertEquals(60, loop.getUpdatesPerSecond());
    assertEquals(1000000000 / 60, loop.getOptimalDelayTimeNanos());
  }
}

