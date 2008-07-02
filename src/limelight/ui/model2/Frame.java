package limelight.ui.model2;

import limelight.ui.api.Stage;
import limelight.ui.Panel;
import javax.swing.*;

public class Frame extends JFrame
{
  private Stage stage;
  private RootPanel root;

  public Frame(Stage stage)
  {
    this.stage = stage;
    setIconImage(new ImageIcon(System.getProperty("limelight.home") + "/bin/icon_48.gif").getImage());
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
    root.repaint();
  }

  public void load(Panel child)
  {
    if(root != null)
      root.destroy();
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

  public RootPanel getRoot()
  {
    return root;
  }
}
