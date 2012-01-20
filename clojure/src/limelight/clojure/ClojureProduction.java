//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.clojure;

import clojure.lang.*;
import limelight.Boot;
import limelight.LimelightException;
import limelight.model.Production;
import limelight.model.api.ProductionProxy;
import limelight.styles.RichStyle;
import limelight.ui.model.Scene;
import java.util.Map;

public class ClojureProduction extends Production
{
  private ProductionProxy proxy;

  public ClojureProduction(String path)
  {
    super(path);
  }

  @Override
  public void loadHelper()
  {
    proxy.loadHelper();
  }

  public ProductionProxy getProxy()
  {
    return proxy;
  }

  public void setProxy(ProductionProxy proxy)
  {
    this.proxy = proxy;
  }

  @Override
  protected void illuminate()
  {
    proxy.illuminate();
  }

  @Override
  protected void loadLibraries()
  {
    proxy.loadLibraries();
  }

  @Override
  protected void loadStages()
  {
    proxy.loadStages();
  }

  @Override
  protected Scene loadScene(String scenePath, Map<String, Object> options)
  {
    return (Scene) proxy.loadScene(scenePath, options).getPeer();
  }

  @Override
  protected Map<String, RichStyle> loadStyles(String path, Map<String, RichStyle> extendableStyles)
  {
    return proxy.loadStyles(path, extendableStyles);
  }

  @Override
  protected void prepareToOpen()
  {
    try
    {
      Var newProduction = loadVar("limelight.clojure.production", "new-production");
      proxy = (ProductionProxy)newProduction.invoke(this);
    }
    catch(Exception e)
    {
      throw new LimelightException(e);
    }
  }

  @Override
  protected void finalizeClose()
  {
  }

  @Override
  public Object send(String name, Object... args)
  {
    return proxy.send(name, args);
  }

  protected static Var loadVar(String namespace, String varName)
  {
    try
    {
      Symbol namespaceSymbol = Symbol.intern(namespace);
      Namespace ns = Namespace.find(namespaceSymbol);
      if(ns != null)
        return (Var) ns.getMapping(Symbol.create(varName));

      RT.load(namespace, false);

      ns = Namespace.find(namespaceSymbol);
      if(ns != null)
        return (Var) ns.getMapping(Symbol.create(varName));

      final String coreFilename = nsToFilename(namespace);
      RT.loadResourceScript(coreFilename);
      ns = Namespace.find(namespaceSymbol);
      if(ns != null)
        return (Var) ns.getMapping(Symbol.create(varName));

      throw new RuntimeException("var still not found after load attempts: " + namespace + "/" + varName);
    }
    catch(Exception e)
    {
      e.printStackTrace();
      throw new RuntimeException("Failed to load var:" + namespace + "/" + varName, e);
    }
  }

  public static String nsToFilename(Symbol symbol)
  {
    return nsToFilename(symbol.getName());
  }

  public static String nsToFilename(String name)
  {
    return name.replace('.', '/').replace('-', '_') + ".clj";
  }

  public static String nsToFilename(Namespace ns)
  {
    return nsToFilename(ns.getName());
  }

  // Example
  public static void main(String[] args) throws Exception
  {
    Boot.boot();
//    final String path = "clj/productions/hello-world";
    final String path = "clojure/productions/calc";
    final ClojureProduction production = new ClojureProduction(path);
    production.open();
  }
}
