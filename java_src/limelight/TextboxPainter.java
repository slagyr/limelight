package limelight;

import javax.swing.*;
import java.awt.*;

public class TextboxPainter extends Painter
{
  public TextboxPainter(Panel panel)
  {
    super(panel);
    panel.add(buildTextbox());
    panel.sterilize();
    panel.setLayout(new InputLayout());
  }

  private JTextField buildTextbox()
  {
    JTextField field = new JTextField();
    BlockEventListener listener = new BlockEventListener(panel.getBlock());
    field.addKeyListener(listener);
    field.addMouseListener(listener);
    return field;
  }

  public void paint(Graphics2D graphics)
  {
  }
}
