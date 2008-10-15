//- Copyright 2008 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model;

import limelight.Context;
import limelight.ui.Panel;
import limelight.ui.api.Stage;

import javax.swing.*;
import java.awt.*;

public class Frame extends JFrame
{
  private Stage stage;
  protected RootPanel root;
  private Insets insets;

  protected Frame()
  {
  }

  public Frame(Stage stage)
  {
    this.stage = stage;
    setContentPane(new LimelightContentPane(this));

    Context.instance().frameManager.watch(this);
    setIconImage(new ImageIcon(Context.instance().limelightHome + "/bin/icons/icon_48.gif").getImage());
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
    if(root != null)
    {
      root.getPanel().setNeedsLayout();
    }
  }

  public void load(Panel child)
  {
    if(root != null)
      root.destroy();
    getContentPane().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
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

  public RootPanel getRoot()
  {
    return root;
  }

  public int getVerticalInsetWidth()
  {
    if(insets == null)
      calculateInsets();
    return insets.top + insets.bottom;
  }

  public int getHorizontalInsetWidth()
  {
    if(insets == null)
      calculateInsets();
    return insets.left + insets.right;
  }

  private void calculateInsets()
  {
    Dimension size = getSize();
    setSize(0, 0);
    setVisible(true);
    insets = getInsets();
    setVisible(false);
    setSize(size);
    if(getJMenuBar() != null)
      insets.top += getJMenuBar().getHeight();
  }

  private class LimelightContentPane extends JPanel
  {
    private final Frame frame;

    public LimelightContentPane(Frame frame)
    {
      this.frame = frame;
    }

    public void paint(Graphics g)
    {
      if(frame.getRoot() != null)
      {
        frame.getRoot().addDirtyRegion(frame.getRoot().getPanel().getAbsoluteBounds());
      }
    }
  }
}
