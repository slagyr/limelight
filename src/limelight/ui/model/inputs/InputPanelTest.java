package limelight.ui.model.inputs;

import junit.framework.TestCase;
import limelight.ui.model.PropPanel;
import limelight.ui.model.RootPanel;
import limelight.ui.model.MockFrame;
import limelight.ui.api.MockProp;
import limelight.ui.MockPanel;
import limelight.Context;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseListener;
import java.awt.*;

public class InputPanelTest extends TestCase
{
  private TestableInputPanel input;
  private PropPanel parent;
  private boolean dragged;
  private boolean pressed;
  private boolean released;
  private boolean clicked;
  private RootPanel root;
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
    root = new RootPanel(new MockFrame());
    rootPanel = new MockPanel();
    root.setPanel(rootPanel);

    rootPanel.add(parent);
    input2 = new TestableInputPanel();
    input3 = new TestableInputPanel();
    rootPanel.add(input2);
    rootPanel.add(input3);
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
