package limelight.ui.model2.inputs;

import limelight.ui.model2.BasePanel;
import limelight.ui.model2.RootPanel;
import limelight.ui.model2.updates.Updates;
import limelight.ui.*;
import limelight.ui.Panel;
import limelight.util.Box;
import limelight.styles.Style;
import limelight.MyKeyboardFocusManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TextBoxPanel extends BasePanel
{
  private JTextField textBox;

  public TextBoxPanel()
  {
    textBox = new JTextField();
//    textBox.focus();
    textBox.setSize(100, textBox.getPreferredSize().height);
  }

  public Box getChildConsumableArea()
  {
    return getBoundingBox();
  }

  public Box getBoxInsidePadding()
  {
    return getBoundingBox();
  }

  public Style getStyle()
  {
    return getParent().getStyle();
  }

  public JTextField getTextBox()
  {
    return textBox;
  }

  public int getWidth()
  {
    return textBox.getWidth();
  }

  public int getHeight()
  {
    return textBox.getHeight();
  }

  public void paintOn(Graphics2D graphics)
  {
//    ((RootPanel)getRoot()).getFrame().add(textBox);
//    textBox.requestFocusInWindow();
//    textBox.setLocation(getAbsoluteLocation());
    textBox.paint(graphics);
  }

  public void mousePressed(MouseEvent e)
  {
    e.setSource(textBox);
    for(MouseListener mouseListener : textBox.getMouseListeners())
      mouseListener.mousePressed(translatedEvent(e));
  }

  public void mouseReleased(MouseEvent e)
  {
    e.setSource(textBox);
    for(MouseListener mouseListener : textBox.getMouseListeners())
      mouseListener.mouseReleased(translatedEvent(e));
  }

  public void mouseClicked(MouseEvent e)
  {
//System.err.println("mouseClicked");
//    textBox.focus();

    FocusListener[] listeners = textBox.getFocusListeners();
    for(FocusListener listener : listeners)
    {
      System.err.println("listener = " + listener + " " + listener.getClass());
    }

    for(MouseListener mouseListener : textBox.getMouseListeners())
      mouseListener.mouseClicked(translatedEvent(e));
    setNeededUpdate(Updates.shallowPaintUpdate);
  }

  public void mouseDragged(MouseEvent e)
  {
    e.setSource(textBox);
    for(MouseMotionListener mouseListener : textBox.getMouseMotionListeners())
      mouseListener.mouseDragged(translatedEvent(e));
  }

  private class MyJTextField extends JTextField
  {
    public void focus()
    {
      processFocusEvent(new FocusEvent(this, FocusEvent.FOCUS_GAINED));
      ((MyKeyboardFocusManager)KeyboardFocusManager.getCurrentKeyboardFocusManager()).focusComponent(this);
    }

    public void repaint()
    {
      setNeededUpdate(Updates.shallowPaintUpdate);
    }

    public void repaint(long tm, int x, int y, int width, int height)
    {
      setNeededUpdate(Updates.shallowPaintUpdate);
    }

    public void repaint(Rectangle r)
    {
      setNeededUpdate(Updates.shallowPaintUpdate);
    }
  }
}
