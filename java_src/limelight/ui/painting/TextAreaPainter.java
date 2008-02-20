package limelight.ui.painting;

import limelight.ui.*;
import limelight.LimelightException;

import javax.swing.*;
import java.awt.*;

public class TextAreaPainter extends Painter
{
  private JTextArea textArea;

  public TextAreaPainter(limelight.ui.Panel panel)
  {
    super(panel);
    panel.add(buildTextArea());
    panel.sterilize();
    panel.setLayout(new InputLayout());
    panel.setTextAccessor(new TextAccessor() {
      public void setText(String text) throws LimelightException
      {
        textArea.setText(text);
      }

      public String getText()
      {
        return textArea.getText();
      }
    });
  }

  private JTextArea buildTextArea()
  {
    textArea = new JTextArea();
    BlockEventListener listener = new BlockEventListener(panel.getBlock());
    textArea.addKeyListener(listener);
    textArea.addMouseListener(listener);
    textArea.addFocusListener(listener);
    return textArea;
  }

  public void paint(Graphics2D graphics)
  {
  }

  public JTextArea getTextArea()
  {
    return textArea;
  }
}
