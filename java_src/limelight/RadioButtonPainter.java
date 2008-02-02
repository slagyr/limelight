package limelight;

import limelight.ui.TextAccessor;

import javax.swing.*;
import java.awt.*;

public class RadioButtonPainter extends Painter
{
  private JRadioButton radioButton;

  public RadioButtonPainter(Panel panel)
  {
    super(panel);
    panel.add(buildTextBox());
    panel.sterilize();
    panel.setLayout(new InputLayout());
    panel.setTextAccessor(new TextAccessor() {
      public void setText(String text) throws LimelightException
      {
        radioButton.setText(text);
      }

      public String getText()
      {
        return radioButton.getText();
      }
    });
  }

  private JRadioButton buildTextBox()
  {
    radioButton = new JRadioButton();
    BlockEventListener listener = new BlockEventListener(panel.getBlock());
    radioButton.addKeyListener(listener);
    radioButton.addMouseListener(listener);
    radioButton.addActionListener(listener);
    radioButton.addFocusListener(listener);
    radioButton.addChangeListener(listener);
    return radioButton;
  }

  public void paint(Graphics2D graphics)
  {
  }

  public JRadioButton getRadioButton()
  {
    return radioButton;
  }
}
