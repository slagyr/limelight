package limelight.ui.painting;

import limelight.ui.*;
import limelight.LimelightException;

import javax.swing.*;
import java.awt.*;

public class ButtonPainter extends Painter
{
  private JButton button;

  public ButtonPainter(limelight.ui.Panel panel)
  {
    super(panel);
    panel.add(buildButton());
    panel.sterilize();
    panel.setLayout(new InputLayout());
    panel.setTextAccessor(new TextAccessor() {

      public void setText(String text) throws LimelightException
      {
        button.setText(text);
      }

      public String getText()
      {
        return button.getText();
      }
    });
  }

  private JButton buildButton()
  {
    button = new JButton();
    BlockEventListener listener = new BlockEventListener(panel.getBlock());
    button.addKeyListener(listener);
    button.addMouseListener(listener);
    button.addActionListener(listener);
    button.addFocusListener(listener);
    button.addChangeListener(listener);
    return button;
  }

  public void paint(Graphics2D graphics)
  {
  }

  public JButton getButton()
  {
    return button;
  }
}
