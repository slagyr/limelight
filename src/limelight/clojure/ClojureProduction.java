package limelight.clojure;

import clojure.lang.*;
import clojure.lang.Compiler;
import limelight.Boot;
import limelight.LimelightException;
import limelight.model.Production;
import limelight.model.api.ProductionProxy;
import limelight.model.api.SceneProxy;
import limelight.ui.model.Scene;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

public class ClojureProduction extends Production
{
  private ProductionProxy proxy;

  public ClojureProduction(String path)
  {
    super(path);
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
    return proxy.loadScene(scenePath, options).getPeer();
  }

  @Override
  protected void loadStyles(Scene scene)
  {
    proxy.loadStyles((SceneProxy) scene.getProxy());
  }

  public static String newProductionSrc = "" +
    "(load \"/limelight/production\")\n" +
    "(limelight.production/new-production peerProduction)";

  @Override
  protected void prepareToOpen()
  {
    try
    {
      Map<String, Object> bindings = new HashMap<String, Object>();
      bindings.put("peerProduction", this);
      proxy = (ProductionProxy)runClojureScript(bindings, newProductionSrc);
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

  public static Object runClojureScript(Map<String, ? extends Object> bindings, String script) throws Exception
  {
    try
    {
      new Binding<String>(script);
      Namespace ns = (Namespace) RT.CURRENT_NS.get();
      Associative mappings = PersistentHashMap.EMPTY;
      mappings = mappings.assoc(RT.CURRENT_NS, RT.CURRENT_NS.get());
      for(Map.Entry<String, ? extends Object> e : bindings.entrySet())
      {
        String varName = e.getKey();
        Symbol sym = Symbol.intern(varName);
        Var var = Var.intern(ns, sym);
        mappings = mappings.assoc(var, e.getValue());
      }
      Var.pushThreadBindings(mappings);
      return Compiler.load(new StringReader(script));
    }
    finally
    {
      Var.popThreadBindings();
    }
  }

  // Example
  public static void main(String[] args) throws Exception
  {
    Boot.boot();
    final ClojureProduction production = new ClojureProduction("clj/sample");
    production.prepareToOpen();
    System.err.println("production.proxy = " + production.proxy);
    production.open();
    System.err.println("opened");
  }
}
