//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs;

import junit.framework.TestCase;

import javax.swing.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import limelight.ui.model.RootPanel;
import limelight.ui.model.PropPanel;
import limelight.ui.model.MockPropFrame;
import limelight.ui.api.MockProp;

public class ScrollBarPanelTest extends TestCase
{
  private ScrollBarPanel panel;
  private ScrollBarPanel horizontalPanel;
  private boolean clicked;
  private boolean pressed;
  private boolean released;
  private boolean dragged;

  public void setUp() throws Exception
  {
    panel = new ScrollBarPanel(ScrollBarPanel.VERTICAL);
    horizontalPanel = new ScrollBarPanel(ScrollBarPanel.HORIZONTAL);
  }

  public void testHasScrollBarWithOrientation() throws Exception
  {
    assertEquals(ScrollBarPanel.VERTICAL, panel.getOrientation());
    assertEquals(JScrollBar.VERTICAL, panel.getScrollBar().getOrientation());

    assertEquals(ScrollBarPanel.HORIZONTAL, horizontalPanel.getOrientation());
    assertEquals(JScrollBar.HORIZONTAL, horizontalPanel.getScrollBar().getOrientation());
  }
  
  public void testDefaultSizes() throws Exception
  {
    assertEquals(panel.getScrollBar().getPreferredSize().width, panel.getWidth());
    assertEquals(horizontalPanel.getScrollBar().getPreferredSize().height, horizontalPanel.getHeight());
  }

  public void testSize() throws Exception
  {
    panel.setSize(100, 200);
    assertEquals(200, panel.getHeight());
    assertEquals(panel.getScrollBar().getPreferredSize().width, panel.getWidth());

    horizontalPanel.setSize(100, 200);
    assertEquals(100, horizontalPanel.getWidth());
    assertEquals(horizontalPanel.getScrollBar().getPreferredSize().height, horizontalPanel.getHeight());
  }
  
  public void testMousePressedCaptured() throws Exception
  {
    addMouseListener();

    panel.mousePressed(new MouseEvent(new JPanel(), 1, 2, 3, 4, 5, 6, false));

    assertEquals(true, pressed);
  }

  public void testMouseReleasedCaptured() throws Exception
  {
    addMouseListener();

    panel.mouseReleased(new MouseEvent(new JPanel(), 1, 2, 3, 4, 5, 6, false));

    assertEquals(true, released);
  }

  public void testMouseClickedCaptured() throws Exception
  {
    addMouseListener();

    panel.mouseClicked(new MouseEvent(new JPanel(), 1, 2, 3, 4, 5, 6, false));

    assertEquals(true, clicked);
  }
  
  public void testMouseDragCaptured() throws Exception
  {
    addMouseMotionListener();

    panel.mouseDragged(new MouseEvent(new JPanel(), 1, 2, 3, 4, 5, 6, false));

    assertEquals(true, dragged);
  }
  
  public void testChanges() throws Exception
  {
    RootPanel root = new RootPanel(new MockPropFrame());
    PropPanel parent = new PropPanel(new MockProp());
    root.setPanel(parent);
    parent.add(panel);
    parent.doLayout();
    panel.setValue(50);

    assertEquals(true, parent.needsLayout());
  }

  public void testParentIsMarkedAsChanged() throws Exception
  {
    RootPanel root = new RootPanel(new MockPropFrame());
    PropPanel parent = new PropPanel(new MockProp());
    root.setPanel(parent);
    parent.add(panel);
    parent.doLayout();

    panel.setValue(50);

    assertEquals(true, parent.needsLayout());
  }

  public void testConfigure() throws Exception
  {
    panel.configure(100, 500);
    assertEquals(100, panel.getVisibleAmount());
    assertEquals(500, panel.getMaximumValue());
    assertEquals(10, panel.getUnitIncrement());
    assertEquals(90, panel.getBlockIncrement());

    panel.configure(500, 1000);
    assertEquals(500, panel.getVisibleAmount());
    assertEquals(1000, panel.getMaximumValue());
    assertEquals(50, panel.getUnitIncrement());
    assertEquals(450, panel.getBlockIncrement());
  }

  public void testCannotBeBuffered() throws Exception
  {
    assertEquals(false, panel.canBeBuffered());
  }

  private void addMouseMotionListener()
  {
    panel.getScrollBar().addMouseMotionListener(new MouseMotionListener(){
      public void mouseDragged(MouseEvent e)
      {
        dragged = true;
      }

      public void mouseMoved(MouseEvent e)
      {
      }
    });
  }

  private void addMouseListener()
  {
    panel.getScrollBar().addMouseListener(new MouseListener(){
      public void mouseClicked(MouseEvent e)
      {
        clicked = true;
      }

      public void mousePressed(MouseEvent e)
      {
        pressed = true;
      }

      public void mouseReleased(MouseEvent e)
      {
        released = true;
      }

      public void mouseEntered(MouseEvent e)
      {
      }

      public void mouseExited(MouseEvent e)
      {
      }
    });
  }


}
