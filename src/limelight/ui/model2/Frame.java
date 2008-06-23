package limelight.ui.model2;

import limelight.ui.api.Stage;
import limelight.ui.Panel;

import javax.swing.*;
import java.awt.*;

public class Frame extends JFrame
{
  private Stage stage;
  private RootPanel root;

  public Frame(Stage stage)
  {
    this.stage = stage;
//    setLayout(null);
    setIconImage(new ImageIcon(System.getProperty("limelight.home") + "/bin/icon_48.gif").getImage());
//    System.out.println("System.getProperty(\"mrj.version\") = " + System.getProperty("mrj.version"));
  }

  public void close()
  {
    setVisible(false);
    dispose();
  }

  public void open()
  {
    setVisible(true);
    root.doLayout();
    PaintJob job = new PaintJob(root.getAbsoluteBounds());
    job.paint(root);
    Graphics graphics = getContentPane().getGraphics();
    job.applyTo(graphics);
// NOW PAINT!
  }

  public void load(Panel child)
  {
    root = new RootPanel(this);
    root.setPanel(child);
  }

  public Stage getStage()
  {
    return stage;
  }

  public void alert(String message)
  {
    JOptionPane.showMessageDialog(this, message, "Limelight Alert", JOptionPane.WARNING_MESSAGE);
  }

  public void setSize(int width, int height)
  {
    super.setSize(width, height);
  }
}
