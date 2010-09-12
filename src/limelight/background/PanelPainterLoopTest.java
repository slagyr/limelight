//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.background;

import limelight.BufferedImagePool;
import limelight.ui.api.MockProp;
import limelight.ui.model.*;
import limelight.ui.MockPanel;
import limelight.Context;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.util.ArrayList;

public class PanelPainterLoopTest extends Assert
{
  private PanelPainterLoop loop;
  private MockFrameManager frameManager;
  private MockPropFrame activeFrame;
  private ScenePanel activeRoot;

  @Before
  public void setUp() throws Exception
  {
    loop = new PanelPainterLoop();
    frameManager = new MockFrameManager();
    Context.instance().frameManager = frameManager;
    activeFrame = new MockPropFrame();
    activeRoot = new ScenePanel(new MockProp());
    activeRoot.setFrame(activeFrame);
    activeFrame.setRoot(activeRoot);
  }
  
  @Test
  public void shouldIsAnIdleThreadLoop() throws Exception
  {
    assertEquals(true, loop instanceof IdleThreadLoop);
  }

  @Test
  public void shouldGettingRootWhenNoFrameIsActive() throws Exception
  {
    assertEquals(null, loop.getActiveRoot());
  }
  
  @Test
  public void shouldGetRootWithActiveFrame() throws Exception
  {
    frameManager.focusedFrame = activeFrame;

    assertEquals(activeFrame.getRoot(), loop.getActiveRoot());
  }
  
  @Test
  public void shouldIdleWhenThereIsNoRoot() throws Exception
  {
    assertEquals(true, loop.shouldBeIdle());
  }
  
  @Test
  public void shouldBeIdleWhenRootHasNoPanelsNeedingLayoutsOrDirtyRegions() throws Exception
  {
    activeRoot.getAndClearDirtyRegions(new ArrayList<Rectangle>());
    activeRoot.getAndClearPanelsNeedingLayout(new ArrayList<limelight.ui.Panel>());
    
    frameManager.focusedFrame = activeFrame;

    assertEquals(false, activeFrame.getRoot().hasPanelsNeedingLayout());
    assertEquals(false, activeFrame.getRoot().hasDirtyRegions());
    assertEquals(true, loop.shouldBeIdle());
  }

  @Test
  public void shouldNotBeIdleWhenPanelsNeedLayout() throws Exception
  {
    frameManager.focusedFrame = activeFrame;
    activeRoot.addPanelNeedingLayout(new MockPanel());

    assertEquals(false, loop.shouldBeIdle());
  }

  @Test
  public void shouldNotBeIdleWhenThereAreDirtyRegions() throws Exception
  {
    frameManager.focusedFrame = activeFrame;
    activeRoot.addDirtyRegion(new Rectangle(0, 0, 1, 1));

    assertEquals(false, loop.shouldBeIdle());
  }
  
  @Test
  public void shouldDoLayouts() throws Exception
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
  public void shouldPaintDirtyRegions() throws Exception
  {
    Context.instance().bufferedImagePool = new BufferedImagePool(0.1);
    activeRoot.addDirtyRegion(new Rectangle(0, 0, 10, 10));

    loop.paintDirtyRegions(activeRoot);

    assertEquals(false, activeRoot.hasDirtyRegions());
  }

  @Test
  public void shouldUpdatesPerSecond() throws Exception
  {
    assertEquals(30, loop.getUpdatesPerSecond());
    assertEquals(1000000000 / 30, loop.getOptimalDelayTimeNanos());

    loop.setUpdatesPerSecond(60);
    assertEquals(60, loop.getUpdatesPerSecond());
    assertEquals(1000000000 / 60, loop.getOptimalDelayTimeNanos());
  }
}

