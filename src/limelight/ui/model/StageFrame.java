//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.ui.model;

import limelight.Context;
import limelight.background.PanelPainterLoop;
import limelight.model.Stage;

import java.awt.*;

public class StageFrame extends Frame
{
  public static boolean hiddenMode = false;

  private Stage stage;
  private boolean fullscreen;
  private GraphicsDevice graphicsDevice; //used for testing
  private Insets screenInsets; //used for testing
  private boolean kiosk;
  private Dimension sizeBeforeFullScreen;
  private Point locationBeforeFullScreen;
  private Dimension previousSize;

  public StageFrame(Stage stage)
  {
    this.stage = stage;
  }

  public Stage getStage()
  {
    return stage;
  }

  @Override
  public void doLayout()
  {
    final Scene root = stage.getScene();
    if(root != null && isWindowResizing())
      root.getDefaultLayout().doLayout(root);
    else
      super.doLayout();

    refresh();
  }



  public void superDoLayout()
  {
    super.doLayout();
  }

  @Override
  public void paint(Graphics g)
  {
    final Scene root = stage.getScene();
    if(root != null)
    {
      if(isWindowResizing() && Context.instance().bufferedImagePool != null) // MDM - Check the bufferedImagePool because errors get printed during test runs.
      {
        PanelPainterLoop.doPaintJob(root, root.getAbsoluteBounds(), (Graphics2D) g);
      }
      else
      {
        root.addDirtyRegion(root.getAbsoluteBounds());
      }
    }
  }

  @Override
  public void setVisible(boolean visible)
  {
    if(hiddenMode || visible == isVisible())
      return;

    super.setVisible(visible);

    if(visible)
      enterKioskOrFullscreenIfNeeded();
    else
      exitKioskOrFullscreenIfNeeded();
  }

  public void setFullScreen(boolean setting)
  {
    if(fullscreen != setting)
    {
      fullscreen = setting;
      if(fullscreen && isVisible() && this != getGraphicsDevice().getFullScreenWindow())
        turnFullScreenOn();
      else if(this == getGraphicsDevice().getFullScreenWindow() && !kiosk)
        turnFullscreenOff();
    }
  }

  public boolean isFullScreen()
  {
    return fullscreen;
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
          turnFullScreenOn();
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

  public boolean isKiosk()
  {
    return kiosk;
  }

  public void setScreenInsets(Insets insets)  // Used for testing
  {
    screenInsets = insets;
  }

  public void setGraphicsDevice(GraphicsDevice device)  // Used for testing
  {
    this.graphicsDevice = device;
  }

  public GraphicsDevice getGraphicsDevice()
  {
    if(graphicsDevice != null)
      return graphicsDevice;
    return getGraphicsConfiguration().getDevice();
  }

  public limelight.util.Box getUsableScreenBounds()
  {
    GraphicsConfiguration config = getGraphicsDevice().getDefaultConfiguration();
    limelight.util.Box bounds = new limelight.util.Box(config.getBounds());

    Insets insets = screenInsets == null ? Toolkit.getDefaultToolkit().getScreenInsets(config) : screenInsets;

    bounds.shave(insets.top, insets.right, insets.bottom, insets.left);

    return bounds;
  }

  // MDM - When the window is resizing, the layout and painting have to take place
  // immediately.  Otherwise, the window flashes and flickers annoyingly.
  // The best way to tell if the window is being resized (as far as I can tell), is to
  // check the currentEvent in the EventQueue. I didn't see anything in particular about
  // the event that tells us what's happening.
  private boolean isWindowResizing()
  {
    return EventQueue.getCurrentEvent() != null;
  }

  public void refresh()
  {
    final Scene root = stage.getScene();
    if(root != null)
    {
      if(previousSize == null || !previousSize.equals(getSize()))
      {
        root.consumableAreaChanged();
      }
      else
      {
        root.markAsNeedingLayout();
      }
    }
    previousSize = getSize();
  }

  private void enterKioskOrFullscreenIfNeeded()
  {
    if(fullscreen || kiosk)
      turnFullScreenOn();
    if(kiosk)
      Context.instance().os.enterKioskMode();
  }

  public void exitKioskOrFullscreenIfNeeded()
  {
    if(fullscreen || kiosk)
      turnFullscreenOff();
    if(kiosk)
      Context.instance().os.exitKioskMode();
  }

  private void turnFullScreenOn()
  {
    sizeBeforeFullScreen = getSize();
    locationBeforeFullScreen = getLocation();
    getGraphicsDevice().setFullScreenWindow(this);
  }

  private void turnFullscreenOff()
  {
    getGraphicsDevice().setFullScreenWindow(null);
    if(sizeBeforeFullScreen != null)
      super.setSize(sizeBeforeFullScreen);
    if(locationBeforeFullScreen != null)
      super.setLocation(locationBeforeFullScreen);
  }
}
