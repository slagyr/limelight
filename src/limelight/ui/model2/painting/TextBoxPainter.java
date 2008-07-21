package limelight.ui.model2.painting;

import limelight.ui.*;
import limelight.ui.model2.PropPanel;
import limelight.ui.model2.TextAccessor;
import limelight.ui.model2.inputs.TextBoxPanel;

import javax.swing.*;
import java.awt.*;

public class TextBoxPainter extends Painter
{
  private JTextField textField;

  public TextBoxPainter(PropPanel panel)
  {
    super(panel);
    TextBoxPanel textPanel = new TextBoxPanel();
    panel.add(textPanel);
    panel.sterilize();
//    panel.setLayout(new InputLayout());
    panel.setTextAccessor(new TextAccessor() {

      public void setText(String text)
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
//    PropEventListener listener = new PropEventListener(panel.getProp());
//    textField.addKeyListener(listener);
//    textField.addMouseListener(listener);
//    textField.addFocusListener(listener);
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
