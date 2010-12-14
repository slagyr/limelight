//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.model;

import limelight.About;
import limelight.Context;
import limelight.builtin.BuiltInStyles;
import limelight.events.EventHandler;
import limelight.model.api.ProductionProxy;
import limelight.model.events.*;
import limelight.styles.RichStyle;
import limelight.styles.Styles;
import limelight.ui.model.Scene;
import limelight.util.ResourceLoader;
import limelight.util.Util;
import limelight.util.Version;

import java.util.HashMap;
import java.util.Map;

public abstract class Production
{
  private String name;
  private boolean allowClose = true;
  protected ResourceLoader resourceLoader;
  private ProductionProxy proxy;
  private Theater theater;
  private Version minumumLimelightVersion = Version.ZERO;
  private EventHandler eventHandler = new EventHandler();
  private boolean open;
  private Thread closingThread;
  private HashMap<String,RichStyle> styles;

  public Production(String path)
  {
    try
    {
      name = Context.fs().baseName(path);
    }
    catch(Exception e)
    {
      // Is this really needed?
    }
    resourceLoader = ResourceLoader.forRoot(path);
    theater = new Theater(this);
  }

  protected abstract void illuminate();
  protected abstract void loadLibraries();
  protected abstract void loadStages();
  protected abstract Scene loadScene(String scenePath, Map<String, Object> options);
  protected abstract Map<String,RichStyle> loadStyles(String path, Map<String, RichStyle> extendableStyles);
  protected abstract void prepareToOpen();
  protected abstract void finalizeClose();

  public void open()
  {
    open(Util.toMap());
  }

  public void open(Map<String, Object> options)
  {
    prepareToOpen();
    illuminateProduction();
    if(!canProceedWithCompatibility())
    {
      close();
      return;
    }
    loadProduction();
    openDefaultScenes(options);

    open = true;
  }

  public boolean isOpen()
  {
    return open;
  }

  public void close()
  {
    if(!open)
      return;
    open = false;

    final Production production = this;
    closingThread = new Thread(new Runnable()
    {
      public void run()
      {
        Thread.yield();
        new ProductionClosingEvent().dispatch(production);
        theater.close();
        new ProductionClosedEvent().dispatch(production);
        finalizeClose();
      }
    });
    closingThread.start();
  }

  public EventHandler getEventHandler()
  {
    return eventHandler;
  }

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public boolean allowClose()
  {
    return allowClose;
  }

  public void setAllowClose(boolean value)
  {
    allowClose = value;
  }

  public ResourceLoader getResourceLoader()
  {
    return resourceLoader;
  }

  //TODO MDM Git rid of me
  public ProductionProxy getProxy()
  {
    return proxy;
  }
  
  //TODO MDM Git rid of me
  public void setProxy(ProductionProxy proxy)
  {
    this.proxy = proxy;
    theater.setProxy(proxy.getTheater());
  }

  public void attemptClose()
  {
    if(allowClose())
      finalizeClose();
  }

  public Theater getTheater()
  {
    return theater;
  }

  public void setTheater(Theater theater)
  {
    this.theater = theater;
  }

  public String getMinimumLimelightVersion()
  {
    return minumumLimelightVersion.toString();
  }

  public void setMinimumLimelightVersion(String version)
  {
    minumumLimelightVersion = new Version(version);
  }

  public Thread getClosingThread()
  {
    return closingThread;
  }

  public boolean isLimelightVersionCompatible()
  {
    Version required = new Version(getMinimumLimelightVersion());
    return required.isLessThanOrEqual(About.version);
  }

  public boolean canProceedWithCompatibility()
  {
    return isLimelightVersionCompatible() || Context.instance().studio.canProceedWithIncompatibleVersion(getName(), getMinimumLimelightVersion());
  }

  public void illuminateProduction()
  {
    illuminate();
    new ProductionCreatedEvent().dispatch(this);
  }

  public void loadProduction()
  {
    loadLibraries();
    loadStages();
    final Map<String, RichStyle> productionStyles = loadStyles(getResourceLoader().getRoot(), styles);
    styles = Styles.merge(productionStyles, BuiltInStyles.all());
    new ProductionLoadedEvent().dispatch(this);
  }

  public void openScene(String scenePath, Stage stage, Map<String, Object> options)
  {
    Scene scene = loadScene(scenePath, options);
    final Map<String, RichStyle> sceneStyles = loadStyles(scene.getResourceLoader().getRoot(), styles);
    scene.setStyles(Styles.merge(sceneStyles, styles));
    stage.setScene(scene);
    stage.open();
  }

  public void openDefaultScenes(Map<String, Object> options)
  {
    for(Stage stage : theater.getStages())
    {
      if(stage.getDefaultSceneName() != null)
        openScene(stage.getDefaultSceneName(), stage, options);
    }
    new ProductionOpenedEvent().dispatch(this);
  }

  public abstract Object send(String name, Object... args);

  public Map<String, RichStyle> getStyles()
  {
    return styles;
  }
}
