//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.model;

import limelight.styles.RichStyle;
import limelight.ui.model.Scene;
import limelight.util.OptionsMap;
import limelight.util.ResourceLoader;
import sun.org.mozilla.javascript.internal.ObjToIntMap;

import java.util.HashMap;
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
  public String loadedScenePath;
  public String loadStylesPath;
  public Map<String,Object> loadedSceneOptions;

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
    loadedScenePath = scenePath;
    loadedSceneOptions = options;
    return stubbedScene;
  }

  @Override
  public Map<String,RichStyle> loadStyles(String path, Map<String, RichStyle> extendableStyles)
  {
    loadStylesPath = path;
    return new HashMap<String, RichStyle>();
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

  @Override
  public Object send(String name, Object... args)
  {
    lastMethodCalled = name;
    lastMethodCallArgs = args;
    return null;
  }
}
