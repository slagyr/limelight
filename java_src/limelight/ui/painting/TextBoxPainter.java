package limelight.ui.painting;

import limelight.ui.*;
import limelight.LimelightException;

import javax.swing.*;
import java.awt.*;

public class TextBoxPainter extends Painter
{
  private JTextField textField;

  public TextBoxPainter(limelight.ui.Panel panel)
  {
    super(panel);
    panel.add(buildTextBox());
    panel.sterilize();
    panel.setLayout(new InputLayout());
    panel.setTextAccessor(new TextAccessor() {

      public void setText(String text) throws LimelightException
      {
        textField.setText(text);
      }

      public String getText()
      {
        return textField.getText();
      }
    });
  }

  private JTextField buildTextBox()
  {
    textField = new JTextField();
    PropEventListener listener = new PropEventListener(panel.getProp());
    textField.addKeyListener(listener);
    textField.addMouseListener(listener);
    textField.addFocusListener(listener);
    return textField;
  }

  public void paint(Graphics2D graphics)
  {
  }

  public JTextField getTextField()
  {
    return textField;
  }
}
