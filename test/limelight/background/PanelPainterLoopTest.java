//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.background;

import limelight.model.api.FakePropProxy;
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

import static org.junit.Assume.assumeTrue;

public class PanelPainterLoopTest extends Assert
{
  private PanelPainterLoop loop;
  private MockFrameManager frameManager;
  private StageFrame activeFrame;
  private ScenePanel activeRoot;

  @Before
  public void setUp() throws Exception
  {
    assumeTrue(TestUtil.notHeadless());
    loop = new PanelPainterLoop();
    frameManager = new MockFrameManager();
    Context.instance().frameManager = frameManager;
    MockStage activeStage = new MockStage();
    activeFrame = new StageFrame(activeStage);
    activeRoot = new ScenePanel(new FakePropProxy());
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
    activeRoot.getAndClearPanelsNeedingLayout(new ArrayList<limelight.ui.Panel>());
    
    frameManager.focusedFrame = activeFrame;

    assertEquals(false, activeRoot.hasPanelsNeedingLayout());
    assertEquals(false, activeRoot.hasDirtyRegions());
    assertEquals(true, loop.shouldBeIdle());
  }

  @Test
  public void isNotIdleWhenPanelsNeedLayout() throws Exception
  {
    frameManager.focusedFrame = activeFrame;
    activeRoot.addPanelNeedingLayout(new MockPanel());

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
    activeRoot.addPanelNeedingLayout(panel1);
    activeRoot.addPanelNeedingLayout(panel2);

    loop.doAllLayouts(activeRoot);

    assertEquals(true, panel1.wasLaidOut);
    assertEquals(true, panel2.wasLaidOut);
    assertEquals(false, activeRoot.hasPanelsNeedingLayout());
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

