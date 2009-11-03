//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs;

import junit.framework.TestCase;
import limelight.ui.model.*;
import limelight.ui.api.MockProp;
import limelight.ui.api.MockStage;
import limelight.ui.MockPanel;
import limelight.Context;
import limelight.styles.Style;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseListener;
import java.awt.event.ActionEvent;
import java.awt.*;

public class InputPanelTest extends TestCase
{
  private TestableInputPanel input;
  private PropPanel parent;
  private boolean dragged;
  private boolean pressed;
  private boolean released;
  private boolean clicked;
  private MockPanel rootPanel;
  private TestableInputPanel input2;
  private TestableInputPanel input3;

  private static class TestableInputPanel extends InputPanel
  {
    public Component input;

    protected Component createComponent()
    {
      return input = new JPanel();
    }

    protected TextAccessor createTextAccessor()
    {
      return null;
    }

    protected void setDefaultStyles(Style style)
    {
      style.setWidth("123");
      style.setHeight("456");
    }
  }

  public void setUp() throws Exception
  {
    input = new TestableInputPanel();
    parent = new PropPanel(new MockProp());
    parent.add(input);
    Context.instance().keyboardFocusManager = new limelight.KeyboardFocusManager().installed();
  }

  public void testGetsParentsStyle() throws Exception
  {
    assertSame(parent.getStyle(), input.getStyle());
  }

  public void testGetBoundingBox() throws Exception
  {
    input.getComponent().setSize(100, 100);

    limelight.util.Box box = input.getBoundingBox();
    assertEquals(100, box.width);
    assertEquals(100, box.height);
    assertSame(box, input.getBoxInsidePadding());
    assertSame(box, input.getChildConsumableArea());
  }

  public void testMousePressedCaptured() throws Exception
  {
    addMouseListener();

    input.mousePressed(new MouseEvent(new JPanel(), 1, 2, 3, 4, 5, 6, false));

    assertEquals(true, pressed);
  }

  public void testMouseReleasedCaptured() throws Exception
  {
    addMouseListener();

    input.mouseReleased(new MouseEvent(new JPanel(), 1, 2, 3, 4, 5, 6, false));

    assertEquals(true, released);
  }

  public void testMouseClickedCaptured() throws Exception
  {
    addMouseListener();

    input.mouseClicked(new MouseEvent(new JPanel(), 1, 2, 3, 4, 5, 6, false));

    assertEquals(true, clicked);
  }

  public void testMouseDragCaptured() throws Exception
  {
    addMouseMotionListener();

    input.mouseDragged(new MouseEvent(new JPanel(), 1, 2, 3, 4, 5, 6, false));

    assertEquals(true, dragged);
  }

  private void buildInputTree()
  {
    attatchRoot();
    input2 = new TestableInputPanel();
    input3 = new TestableInputPanel();
    rootPanel.add(input2);
    rootPanel.add(input3);
  }

  private void attatchRoot()
  {
    RootPanel root = new RootPanel(new MockPropFrame());
    rootPanel = new MockPanel();
    root.setPanel(rootPanel);
    rootPanel.add(parent);
  }

  public void testFindNextInput() throws Exception
  {
    buildInputTree();
    assertSame(input2, input.nextInputPanel());
    assertSame(input3, input2.nextInputPanel());
    assertSame(input, input3.nextInputPanel());
  }

  public void testFindPreviousInput() throws Exception
  {
    buildInputTree();
    assertSame(input, input2.previousInputPanel());
    assertSame(input2, input3.previousInputPanel());
    assertSame(input3, input.previousInputPanel());
  }

  public void testSetSize() throws Exception
  {
    input.setSize(100, 200);
    assertEquals(100, input.getWidth());
    assertEquals(200, input.getHeight());
    assertEquals(100, input.getComponent().getWidth());
    assertEquals(200, input.getComponent().getHeight());
  }
  
  public void testSetLocation() throws Exception
  {
    input.setLocation(123, 456);
    assertEquals(123, input.getX());
    assertEquals(456, input.getY());
    assertEquals(123, input.getComponent().getX());
    assertEquals(456, input.getComponent().getY());
  }
  
  public void testDoLayout() throws Exception
  {
    attatchRoot();
    parent.getStyle().setWidth("100");
    parent.getStyle().setHeight("200");
    parent.getStyle().setMargin("5");
    parent.getStyle().setPadding("2");
    parent.doLayout();

    input.doLayout();
    assertEquals(new Point(7, 7), input.getLocation());
    assertEquals(86, input.getWidth());
    assertEquals(186, input.getHeight());
  }
  
  public void testSettingParentAlsoSetsDefaultStyles() throws Exception
  {
    assertEquals("123", parent.getStyle().getWidth());
    assertEquals("456", parent.getStyle().getHeight());
  }
  
  public void testMousePressedEventsGetPassedToParent() throws Exception
  {
    MockProp prop = (MockProp)parent.getProp();
    MouseEvent event = new MouseEvent(input.getComponent(), 1, 2, 3, 4, 5, 6, false);
    input.mousePressed(event);

    assertNotNull(prop.pressedMouse);
    assertEquals(input.getComponent(), ((MouseEvent)prop.pressedMouse).getSource());
  }

  public void testMouseReleasedEventsGetPassedToParent() throws Exception
  {
    MockProp prop = (MockProp)parent.getProp();
    MouseEvent event = new MouseEvent(input.getComponent(), 1, 2, 3, 4, 5, 6, false);
    input.mouseReleased(event);

    assertNotNull(prop.releasedMouse);
    assertEquals(input.getComponent(), ((MouseEvent)prop.releasedMouse).getSource());
  }

  public void testMouseClickedEventsGetPassedToParent() throws Exception
  {
    MockProp prop = (MockProp)parent.getProp();
    MouseEvent event = new MouseEvent(input.getComponent(), 1, 2, 3, 4, 5, 6, false);
    input.mouseClicked(event);

    assertNotNull(prop.clickedMouse);
    assertEquals(input.getComponent(), ((MouseEvent)prop.clickedMouse).getSource());
  }

  public void testButtonPressedEventsGetPassedToParent() throws Exception
  {
    input = new TestableInputPanel() {
      protected Component createComponent()
      {
        return input = new JButton();
      }
    };
    parent = new PropPanel(new MockProp());
    parent.add(input);

    MockProp prop = (MockProp)parent.getProp();
    ActionEvent event = new ActionEvent(input.getComponent(), 1, "blah");
    ((JButton)input.getComponent()).getActionListeners()[0].actionPerformed(event);

    assertNotNull(prop.pressedButton);
    assertEquals(input.getComponent(), ((ActionEvent)prop.pressedButton).getSource());
  }
  
//  public void testBackgroundIsTransparentByDefault() throws Exception
//  {
//    assertEquals(0, input.getComponent().getBackground().getAlpha());
//  }

  public void testSettingParentToNullPutsKeyboardFocusOnTheStageFrame() throws Exception
  {
    Context.instance().frameManager = new MockFrameManager();
    StageFrame frame = new StageFrame(new MockStage());
    frame.load(parent);
    input.mouseClicked(new MouseEvent(input.input, 1, 2, 3, 4, 5, 6, false));
    assertEquals(input.input, Context.instance().keyboardFocusManager.getFocuedComponent());

    input.setParent(null);
    assertEquals(frame, Context.instance().keyboardFocusManager.getFocuedComponent());
  }

  private void addMouseMotionListener()
  {
    input.getComponent().addMouseMotionListener(new MouseMotionListener(){
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
    input.getComponent().addMouseListener(new MouseListener(){
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
