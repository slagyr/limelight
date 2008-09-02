package limelight.ui.model;

import limelight.ui.api.Stage;
import limelight.ui.Panel;
import limelight.ui.model.updates.Updates;
import limelight.Context;

import javax.swing.*;
import javax.swing.event.AncestorListener;
import javax.swing.border.Border;
import javax.swing.plaf.PanelUI;
import javax.swing.plaf.ComponentUI;
import javax.accessibility.AccessibleContext;
import java.awt.*;
import java.awt.im.InputMethodRequests;
import java.awt.im.InputContext;
import java.awt.image.*;
import java.awt.dnd.DropTarget;
import java.awt.peer.ComponentPeer;
import java.awt.event.*;
import java.util.*;
import java.util.EventListener;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.beans.PropertyChangeListener;
import java.io.PrintStream;
import java.io.PrintWriter;

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
    Context.instance().frameManager.watch(this);
    setIconImage(new ImageIcon(System.getProperty("limelight.home") + "/bin/icon_48.gif").getImage());
    setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    RepaintManager.currentManager(this).setDoubleBufferingEnabled(false); // Solves "(permanent) grey rect" problem on windows
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
}
