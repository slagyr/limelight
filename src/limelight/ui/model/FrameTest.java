//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model;

import junit.framework.TestCase;
import limelight.ui.api.MockStage;
import limelight.ui.MockPanel;
import limelight.Context;
import limelight.KeyboardFocusManager;
import limelight.styles.styling.RealStyleAttributeCompilerFactory;

import javax.swing.*;
import java.awt.*;

public class FrameTest extends TestCase
{
  private MockStage stage;
  private Frame frame;
  private FrameManager frameManager;

  public void setUp() throws Exception
  {
    frameManager = new AlertFrameManager();
    Context.instance().frameManager = frameManager;
    Context.instance().keyboardFocusManager = new KeyboardFocusManager();

    stage = new MockStage();
    frame = new Frame(stage);
  }

  public void testIcon() throws Exception
  {
    assertNotNull(frame.getIconImage());
  }
  
  public void testStage() throws Exception
  {
    assertSame(stage, frame.getStage());
  }

  public void testLoad() throws Exception
  {
    MockPanel panel = new MockPanel();
    frame.load(panel);

    RootPanel root = frame.getRoot();

    assertSame(panel, root.getPanel());
  }

  public void testLoadSetsDefaultCursor() throws Exception
  {
    frame.getContentPane().setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    MockPanel panel = new MockPanel();
    frame.load(panel);

    assertEquals(Cursor.DEFAULT_CURSOR, frame.getContentPane().getCursor().getType());
  }

  public void testLoadWillDestroyPreviousRoots() throws Exception
  {
    MockPanel panel = new MockPanel();
    frame.load(panel);

    RootPanel firstRoot = frame.getRoot();
    assertEquals(true, firstRoot.isAlive());

    MockPanel panel2 = new MockPanel();
    frame.load(panel2);

    assertEquals(false, firstRoot.isAlive());
  }
  
  public void testAddsSelfToFrameManager() throws Exception
  {
    assertEquals(1, frameManager.getFrameCount());
    assertEquals(true, frameManager.isWatching(frame));
  }
  
  public void testDefaultCloseOperations() throws Exception
  {
    assertEquals(WindowConstants.DISPOSE_ON_CLOSE, frame.getDefaultCloseOperation());
  }
}
