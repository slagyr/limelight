//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs;

import limelight.ui.model.ScenePanel;
import limelight.ui.model.PropPanel;
import limelight.ui.model.MockPropFrame;
import limelight.ui.api.MockProp;
import limelight.ui.model.inputs.painting.ScrollBarPainter;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.awt.event.MouseEvent;

import static junit.framework.Assert.assertEquals;

public class ScrollBar2PanelTest
{
  private ScrollBar2Panel verticalScrollBar;
  private ScrollBar2Panel horizontalScrollBar;
  private boolean clicked;
  private boolean pressed;
  private boolean released;
  private boolean dragged;

  @Before
  public void setUp() throws Exception
  {
    verticalScrollBar = new ScrollBar2Panel(ScrollBar2Panel.VERTICAL);
    horizontalScrollBar = new ScrollBar2Panel(ScrollBar2Panel.HORIZONTAL);
  }

  @Test
  public void shouldHasScrollBarWithOrientation() throws Exception
  {
    assertEquals(ScrollBar2Panel.VERTICAL, verticalScrollBar.getOrientation());

    assertEquals(ScrollBar2Panel.HORIZONTAL, horizontalScrollBar.getOrientation());
  }

  @Test
  public void shouldDefaultSizes() throws Exception
  {
    assertEquals(15, verticalScrollBar.getWidth());
    assertEquals(15, horizontalScrollBar.getHeight());
  }

  @Test
  public void shouldSize() throws Exception
  {
    verticalScrollBar.setSize(100, 200);
    assertEquals(200, verticalScrollBar.getHeight());
    assertEquals(15, verticalScrollBar.getWidth());

    horizontalScrollBar.setSize(100, 200);
    assertEquals(100, horizontalScrollBar.getWidth());
    assertEquals(15, horizontalScrollBar.getHeight());
  }

//  @Test
//  public void shouldMousePressedCaptured() throws Exception
//  {
////    addMouseListener();
//
//    verticalScrollBar.mousePressed(new MouseEvent(new java.awt.Panel(), 1, 2, 3, 4, 5, 6, false));
//
//    assertEquals(true, pressed);
//  }
//
//  @Test
//  public void shouldMouseReleasedCaptured() throws Exception
//  {
////    addMouseListener();
//
//    verticalScrollBar.mouseReleased(new MouseEvent(new java.awt.Panel(), 1, 2, 3, 4, 5, 6, false));
//
//    assertEquals(true, released);
//  }
//
//  @Test
//  public void shouldMouseClickedCaptured() throws Exception
//  {
////    addMouseListener();
//
//    verticalScrollBar.mouseClicked(new MouseEvent(new java.awt.Panel(), 1, 2, 3, 4, 5, 6, false));
//
//    assertEquals(true, clicked);
//  }
//
//  @Test
//  public void shouldMouseDragCaptured() throws Exception
//  {
////    addMouseMotionListener();
//
//    verticalScrollBar.mouseDragged(new MouseEvent(new java.awt.Panel(), 1, 2, 3, 4, 5, 6, false));
//
//    assertEquals(true, dragged);
//  }

  @Test
  public void shouldChanges() throws Exception
  {
    ScenePanel root = new ScenePanel(new MockProp());
    root.setFrame(new MockPropFrame());
    PropPanel parent = new PropPanel(new MockProp());
    root.add(parent);
    parent.add(verticalScrollBar);
    parent.doLayout();
    verticalScrollBar.setValue(50);

    assertEquals(true, parent.needsLayout());
  }

  @Test
  public void shouldParentIsMarkedAsChanged() throws Exception
  {
    ScenePanel root = new ScenePanel(new MockProp());
    root.setFrame(new MockPropFrame());
    PropPanel parent = new PropPanel(new MockProp());
    root.add(parent);
    parent.add(verticalScrollBar);
    parent.doLayout();

    verticalScrollBar.setValue(50);

    assertEquals(true, parent.needsLayout());
  }

  @Test
  public void shouldConfigure() throws Exception
  {
    verticalScrollBar.configure(100, 500);
    assertEquals(100, verticalScrollBar.getVisibleAmount());
    assertEquals(500, verticalScrollBar.getAvailableAmount());
    assertEquals(10, verticalScrollBar.getUnitIncrement());
    assertEquals(90, verticalScrollBar.getBlockIncrement());

    verticalScrollBar.configure(500, 1000);
    assertEquals(500, verticalScrollBar.getVisibleAmount());
    assertEquals(1000, verticalScrollBar.getAvailableAmount());
    assertEquals(50, verticalScrollBar.getUnitIncrement());
    assertEquals(450, verticalScrollBar.getBlockIncrement());
  }

  @Test
  public void shouldCannotBeBuffered() throws Exception
  {
    assertEquals(false, verticalScrollBar.canBeBuffered());
  }

  @Test
  public void shouldCalculateGemSize() throws Exception
  {
    horizontalScrollBar.setSize(100, 15);

    horizontalScrollBar.configure(50, 100);
    assertEquals(31, horizontalScrollBar.getGemSize());

    horizontalScrollBar.configure(25, 100);
    assertEquals(16, horizontalScrollBar.getGemSize());

    horizontalScrollBar.configure(33, 100);
    assertEquals(20, horizontalScrollBar.getGemSize());
  }

  @Test
  public void shouldHaveMinimunGemSize() throws Exception
  {
    horizontalScrollBar.setSize(100, 15);
    horizontalScrollBar.configure(1, 100);
    assertEquals(16, horizontalScrollBar.getGemSize());
  }

  @Test
  public void shouldCalculateGemLocation() throws Exception
  {
    horizontalScrollBar.setSize(100, 15);
    horizontalScrollBar.configure(50, 100);
    
    horizontalScrollBar.setValue(0);
    assertEquals(5, horizontalScrollBar.getGemLocation());

    horizontalScrollBar.setValue(25);
    assertEquals(21, horizontalScrollBar.getGemLocation());

    horizontalScrollBar.setValue(50);
    assertEquals(36, horizontalScrollBar.getGemLocation());
  }


}
