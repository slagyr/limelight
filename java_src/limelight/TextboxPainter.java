package limelight;

import javax.swing.*;
import java.awt.*;

public class TextboxPainter extends Painter
{
  public TextboxPainter(Panel panel)
  {
    super(panel);
    panel.add(new JTextField());
    panel.sterilize();
    panel.setLayout(new InputLayout());
  }

  public void paint(Graphics2D graphics)
  {
  }
}
