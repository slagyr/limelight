//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.model;

import limelight.events.EventHandler;
import limelight.model.api.StageProxy;
import limelight.ui.events.panel.SceneOpenedEvent;
import limelight.ui.events.stage.StageClosedEvent;
import limelight.ui.events.stage.StageClosingEvent;
import limelight.ui.model.StageKeyListener;
import limelight.ui.model.StageMouseListener;
import limelight.ui.model.Scene;

import java.awt.*;

public abstract class Stage
{
  protected StageMouseListener mouseListener;
  protected StageKeyListener keyListener;
  protected Scene scene;
  private EventHandler eventHandler = new EventHandler();
  private boolean vital = true;
  private String name;
  private Theater theater;
  private StageProxy proxy;
  private String defaultSceneName;
  private boolean closing;
  private boolean visible;

  protected Stage(String name)
  {
    this.name = name;
    mouseListener = new StageMouseListener(this);
    keyListener = new StageKeyListener(this);
  }

  protected abstract void doOpen();

  protected abstract void doClose();

  public abstract boolean isOpen();

  protected abstract void doSetVisible(boolean value);

  public abstract int getWidth();

  public abstract int getHeight();

  public abstract Graphics getGraphics();

  public abstract Cursor getCursor();

  public abstract void setCursor(Cursor cursor);

  public abstract Insets getInsets();

  public abstract void setFramed(boolean framed);

  public abstract boolean isFramed();

  public abstract void setAlwaysOnTop(boolean value);

  public abstract boolean isAlwaysOnTop();

  public void open()
  {
    if(isOpen())
      return;

    doOpen();

    if(scene != null)
      new SceneOpenedEvent().dispatch(scene);

    show();
  }

  public void close()
  {
    if(closing)
      return;
    closing = true;
    new StageClosingEvent().dispatch(this);
    hide();
    setScene(null);
    doClose();
    new StageClosedEvent().dispatch(this);
  }


  public String getName()
  {
    return name;
  }

  public EventHandler getEventHandler()
  {
    return eventHandler;
  }

  public StageKeyListener getKeyListener()
  {
    return keyListener;
  }

  public StageMouseListener getMouseListener()
  {
    return mouseListener;
  }

  public Scene getScene()
  {
    return scene;
  }

  public void setScene(Scene newScene)
  {
    if(scene != null && scene.getStage() == this)
      scene.setStage(null);

    scene = newScene;

    if(scene != null)
    {
      newScene.setStage(this);
      if(isOpen())
        new SceneOpenedEvent().dispatch(newScene);
    }
  }

  public boolean shouldAllowClose()
  {
    return scene == null || scene.shouldAllowClose();
  }

  public boolean isVital()
  {
    return vital;
  }

  public void setVital(boolean value)
  {
    vital = value;
  }

  public Theater getTheater()
  {
    return theater;
  }

  public void setTheater(Theater theater)
  {
    this.theater = theater;
  }

  public StageProxy getProxy()
  {
    return proxy;
  }

  public void setProxy(StageProxy proxy)
  {
    this.proxy = proxy;
  }

  public String getDefaultSceneName()
  {
    return defaultSceneName;
  }

  public void setDefaultSceneName(String defaultSceneName)
  {
    this.defaultSceneName = defaultSceneName;
  }

  public void show()
  {
    setVisible(true);
  }

  public void hide()
  {
    setVisible(false);
  }

  public boolean isVisible()
  {
    return visible;
  }

  public void setVisible(boolean visible)
  {
    doSetVisible(visible);
    this.visible = visible;
  }
}
