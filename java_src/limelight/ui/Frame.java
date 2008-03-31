package limelight.ui;

import javax.swing.*;
import java.awt.*;

public class Frame extends JFrame
{
  private Stage stage;

  public Frame(Stage stage)
  {
    this.stage = stage;
    setLayout(null);
  }

  public void doLayout()
  {
    super.doLayout();
  }

  public void close()
  {
    setVisible(false);
    dispose();
  }

  public void open()
  {
    setVisible(true);
    repaint();
  }

  public void load(Component child)
  {
    getContentPane().removeAll();
    add(child);
  }
}
