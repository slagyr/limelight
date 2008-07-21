package limelight.ui.model2.inputs;

import junit.framework.TestCase;
import limelight.ui.model2.PropPanel;
import limelight.ui.api.MockProp;
import limelight.util.Box;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseListener;

public class TextBoxPanelTest extends TestCase
{
  private TextBoxPanel panel;
  private PropPanel parent;
  private boolean dragged;
  private boolean pressed;
  private boolean released;
  private boolean clicked;

  public void setUp() throws Exception
  {
    panel = new TextBoxPanel();
    parent = new PropPanel(new MockProp());
    parent.add(panel);
  }

  public void testGetsParentsStyle() throws Exception
  {
    assertSame(parent.getStyle(), panel.getStyle());
  }

  public void testHasJtextBox() throws Exception
  {
    assertEquals(panel.getTextBox().getClass(), JTextField.class);    
  }
  
  public void testGetBoundingBox() throws Exception
  {
    panel.getTextBox().setSize(100, 100);

    Box box = panel.getBoundingBox();
    assertEquals(100, box.width);
    assertEquals(100, box.height);
    assertSame(box, panel.getBoxInsidePadding());
    assertSame(box, panel.getChildConsumableArea());
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

  private void addMouseMotionListener()
  {
    panel.getTextBox().addMouseMotionListener(new MouseMotionListener(){
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
    panel.getTextBox().addMouseListener(new MouseListener(){
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
