package limelight.ui.model;

import limelight.ui.api.Stage;
import limelight.ui.Panel;
import limelight.ui.model.updates.Updates;
import limelight.Context;

import javax.swing.*;
import java.awt.*;

public class Frame extends JFrame
{
  private Stage stage;
  protected RootPanel root;

  protected Frame()
  {
  }

  public Frame(Stage stage)
  {
    this.stage = stage;
    setContentPane(new LimelightContentPane(this));
    Context.instance().frameManager.watch(this);
    setIconImage(new ImageIcon(System.getProperty("limelight.home") + "/bin/icon_48.gif").getImage());
    setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
  }

  public void doLayout()
  {
    super.doLayout();
    refresh();
  }

  public void close()
  {
    setVisible(false);
    dispose();
  }

  public void open()
  {
    setVisible(true);
    refresh();
  }

  public void refresh()
  {
    root.getPanel().setNeededUpdate(Updates.layoutAndPaintUpdate);
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

  protected void setStage(Stage stage)
  {
    this.stage = stage;
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

  private class LimelightContentPane extends JPanel
  {
    private Frame frame;

    public LimelightContentPane(Frame frame)
    {
      this.frame = frame;
    }

    public void paint(Graphics g)
    {     
      frame.getRoot().getPanel().setNeededUpdate(Updates.paintUpdate);
    }
  }
}
