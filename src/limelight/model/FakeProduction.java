//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.model;

import limelight.ui.model.Scene;
import limelight.util.ResourceLoader;

import java.util.Map;

public class FakeProduction extends Production
{
  public boolean wasAskedIfAllowedToShutdown;
  public boolean closeFinalized;
  public String lastMethodCalled;
  public Object[] lastMethodCallArgs;
  public int drbPort;
  public boolean closeAttempted;
  public boolean openPrepared;
  public boolean librariesLoaded;
  public boolean stagesLoaded;
  public boolean illuminated;
  public Scene stubbedScene;
  public String openedScenePath;
  public Scene loadStylesScene;

  public FakeProduction(String name)
  {
    super(name);
  }

  public FakeProduction(String name, ResourceLoader loader)
  {
    this(name);
    resourceLoader = loader;
  }

  @Override
  public void illuminate()
  {
    illuminated = true;
  }

  @Override
  public void prepareToOpen()
  {
    openPrepared = true;
  }

  public FakeProduction()
  {
    this("test");
  }

  public boolean allowClose()
  {
    wasAskedIfAllowedToShutdown = true;
    return super.allowClose();
  }

  public void finalizeClose()
  {
    closeFinalized = true;
  }

  @Override
  public void loadLibraries()
  {
    librariesLoaded = true;
  }

  @Override
  public void loadStages()
  {
    stagesLoaded = true;
  }

  @Override
  public Scene loadScene(String scenePath, Map<String, Object> options)
  {
    openedScenePath = scenePath;
    return stubbedScene;
  }

  @Override
  public void loadStyles(Scene scene)
  {
    loadStylesScene = scene;
  }

  public Object callMethod(String name, Object... args)
  {
    lastMethodCalled = name;
    lastMethodCallArgs = args;
    return null;
  }

  public void publish_on_drb(int port)
  {
    drbPort = port;
  }

  @Override
  public void attemptClose()
  {
    closeAttempted = true;
  }
}
