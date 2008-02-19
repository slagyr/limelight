package limelight.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class TextBoxPanel extends ComponentPanel
{
  private JTextField textBox;

  public TextBoxPanel()
  {
    textBox = new TextBox(this);
  }

  public Component getComponent()
  {
    return textBox;
  }

  public void snapToSize()
  {
    Rectangle potentialArea = getParent().getChildConsumableArea();
    setSize(potentialArea.width, potentialArea.height);
  }

  public void paintOn(Graphics2D graphics)
  {
    textBox.paint(graphics);
  }

  public JTextField getTextBox()
  {
    return textBox;
  }

  static private class TextBox extends JTextField
  {
    private Panel owner;

    public TextBox(Panel owner)
    {
      this.owner = owner;
      setOpaque(true); 
    }

    public Graphics getGraphics()
    {
      return owner.getClippedGraphics();
    }
  }
}
