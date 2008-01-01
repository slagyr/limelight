package limelight;

import javax.swing.*;
import java.awt.*;

public class TextBoxPainter extends Painter
{
  private JTextField textField;

  public TextBoxPainter(Panel panel)
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
    BlockEventListener listener = new BlockEventListener(panel.getBlock());
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
