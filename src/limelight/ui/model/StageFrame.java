//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model;

import limelight.Context;
import limelight.util.Colors;
import limelight.ui.Panel;
import limelight.ui.api.Stage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

public class StageFrame extends JFrame implements KeyListener
{
  private Stage stage;
  protected RootPanel root;
  private Insets insets;
  private boolean fullscreen;
  private boolean hasMenuBar;
  private GraphicsDevice graphicsDevice;
  private boolean kiosk;
  private Dimension size;
  private Point location;

  protected StageFrame()
  {
  }

  public StageFrame(Stage stage)
  {
    this();
    this.stage = stage;
    setContentPane(new LimelightContentPane(this));
    setBackground(Color.WHITE);

    Context.instance().frameManager.watch(this);
    setIconImage(new ImageIcon(Context.instance().limelightHome + "/bin/icons/icon_48.gif").getImage());
    setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    addKeyListener(this);
  }

  public void doLayout()
  {
    super.doLayout();
    refresh();
  }

  public void close()
  {
    setVisible(false);
    exitKioskOrFullscreenIfNeeded();
    dispose();
  }

  public void open()
  {
    if(!hasMenuBar)
      setJMenuBar(null);

    setVisible(true);
//    enterKioskOrFullscreenIfNeeded();
    refresh();
  }

  public void setVisible(boolean visible)
  {
    if(visible == isVisible())
      return;
    super.setVisible(visible);

    if(visible)
      enterKioskOrFullscreenIfNeeded();
    else
      exitKioskOrFullscreenIfNeeded();
  }

  public void refresh()
  {
    if(root != null)
    {
      root.getPanel().setNeedsLayout();
    }
  }

  public void setSize(int width, int height)
  {
    size = new Dimension(width, height);
    super.setSize(width, height);
  }

  public void setSize(Dimension d)
  {
    size = new Dimension(d.width, d.height);
    super.setSize(d);
  }

  public void setLocation(int x, int y)
  {
    location = new Point(x, y);
    super.setLocation(x, y);
  }

  public void setLocation(Point p)
  {
    location = new Point(p.x, p.y);
    super.setLocation(p);
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

  public void setFullScreen(boolean setting)
  {
    if(fullscreen != setting)
    {
      fullscreen = setting;
      if(fullscreen && isVisible())
        getGraphicsDevice().setFullScreenWindow(this);
      else if(this == getGraphicsDevice().getFullScreenWindow())
        turnFullscreenOff();
    }
  }

  private void turnFullscreenOff()
  {
    getGraphicsDevice().setFullScreenWindow(null);
    if(size != null)
      super.setSize(size);
    if(location != null)
      super.setLocation(location);
  }

  public void setGraphicsDevice(GraphicsDevice device)
  {
    this.graphicsDevice = device;
  }

  public GraphicsDevice getGraphicsDevice()
  {
    if(graphicsDevice != null)
      return graphicsDevice;
    return GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
  }

  public boolean isFullScreen()
  {
    return fullscreen;
  }

  public void setHasMenuBar(boolean value)
  {
    hasMenuBar = value;
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

  public void keyTyped(KeyEvent e)
  {
    Panel panel = root.getPanel();
    if(panel != null)
      panel.keyTyped(e);
  }

  public void keyPressed(KeyEvent e)
  {
    Panel panel = root.getPanel();
    if(panel != null)
      panel.keyPressed(e);
  }

  public void keyReleased(KeyEvent e)
  {
    Panel panel = root.getPanel();
    if(panel != null)
      panel.keyReleased(e);
  }

  public void setBackgroundColor(String colorString)
  {
    setBackground(Colors.resolve(colorString));
  }

  public String getBackgroundColor()
  {
    return Colors.toString(getBackground());
  }

  public void setKiosk(boolean value)
  {
    if(kiosk == value)
      return;
    kiosk = value;
    if(isVisible())
    {
      if(kiosk)
      {
        if(getGraphicsDevice().getFullScreenWindow() != this)
          getGraphicsDevice().setFullScreenWindow(this);
        Context.instance().os.enterKioskMode();
      }
      else
      {
        if(!fullscreen && getGraphicsDevice().getFullScreenWindow() == this)
          turnFullscreenOff();
        Context.instance().os.exitKioskMode();
      }
    }
  }

  private void enterKioskOrFullscreenIfNeeded()
  {
    if(fullscreen || kiosk)
      getGraphicsDevice().setFullScreenWindow(this);
    if(kiosk)
      Context.instance().os.enterKioskMode();
  }

  private void exitKioskOrFullscreenIfNeeded()
  {
    if(fullscreen || kiosk)
      turnFullscreenOff();
    if(kiosk)
      Context.instance().os.exitKioskMode();
  }

  public boolean isKiosk()
  {
    return kiosk;
  }

  private class LimelightContentPane extends JPanel
  {
    private final StageFrame frame;

    public LimelightContentPane(StageFrame frame)
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
