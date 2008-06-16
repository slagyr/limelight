//- Copyright 2008 8th Light, Inc.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model;

import limelight.ui.api.Stage;

import javax.swing.*;
import java.awt.*;

public class Frame extends JFrame
{
  private Stage stage;

  public Frame(Stage stage)
  {
    this.stage = stage;
    setLayout(null);
    setIconImage(new ImageIcon(System.getProperty("limelight.home") + "/bin/icon_48.gif").getImage());
//    System.out.println("System.getProperty(\"mrj.version\") = " + System.getProperty("mrj.version"));
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

  public Stage getStage()
  {
    return stage;
  }

  public void alert(String message)
  {
    JOptionPane.showMessageDialog(this, message, "Limelight Alert", JOptionPane.WARNING_MESSAGE);
  }
}
