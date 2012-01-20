//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.ruby;

import limelight.Context;
import limelight.LimelightException;
import limelight.model.Production;
import limelight.model.api.ProductionProxy;
import limelight.model.api.SceneProxy;
import limelight.styles.RichStyle;
import limelight.ui.model.Scene;
import org.jruby.Ruby;
import org.jruby.RubyInstanceConfig;
import org.jruby.javasupport.JavaEmbedUtils;
import org.jruby.javasupport.JavaSupport;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class RubyProduction extends Production
{
  private static Map<Integer, Ruby> rubies = new HashMap<Integer, Ruby>();
  private static int productionIds = 0;

  public static String newProductionSrc = "" +
    "require 'limelight/limelight_init'\n" +
    "require 'limelight/production'\n" +
    "proxy = Limelight::Production.new(Java::java.lang.Thread.currentThread.production)\n" +
    "Java::java.lang.Thread.currentThread.proxy = proxy\n";

  private Ruby rubyRuntime;
  private int id;
  private ProductionProxy proxy;

  public RubyProduction(String path)
  {
    super(path);
    id = ++productionIds;
  }

  @Override
  public void loadHelper()
  {
  }

  public void setProxy(ProductionProxy proxy)
  {
    this.proxy = proxy;
  }

  public ProductionProxy getProxy()
  {
    return proxy;
  }

  @Override
  public Object send(String name, Object... args)
  {
    return proxy.send(name, args);
  }

  @Override
  public void illuminate()
  {
    proxy.illuminate();
  }

  @Override
  public void loadLibraries()
  {
    proxy.loadLibraries();
  }

  @Override
  public void loadStages()
  {
    proxy.loadStages();
  }

  @Override
  public Scene loadScene(String scenePath, Map<String, Object> options)
  {
//    final RubyHash rubyHash = new RubyHash(rubyRuntime);
//    RubySymbol.newSymbol()
    final SceneProxy proxy = this.proxy.loadScene(scenePath, options);
    return (Scene)proxy.getPeer();
  }

  @Override
  public Map<String,RichStyle> loadStyles(String path, Map<String, RichStyle> extendableStyles)
  {
    return proxy.loadStyles(path, extendableStyles);
  }

  public void prepareToOpen()
  {
    spawnRubyRuntime();
  }

  @Override
  public void finalizeClose()
  {
    rubies.remove(id);
    if(rubyRuntime != null)
    {
      new TearDownRubyThread(rubyRuntime, rubies.values()).start();
    }
  }

  private void spawnRubyRuntime()
  {
    SpawnRubyThread spawnThread = new SpawnRubyThread(newProductionSrc, id);
    spawnThread.production = this;
    spawnThread.start();

    try
    {
      spawnThread.join();
      if(spawnThread.error != null)
        throw spawnThread.error;
      else
        if(spawnThread.proxy != null)
        {
          rubyRuntime = spawnThread.ruby;
          rubies.put(id, rubyRuntime);
          setProxy(spawnThread.proxy);
        }
        else
        {
          spawnThread.ruby.tearDown();
          spawnThread = null;
          gc();
        }
    }
    catch(Exception e)
    {
      throw new LimelightException("Failed to start JRuby Runtime", e);
    }
    finally
    {
      if(spawnThread != null)
        spawnThread.clear();
    }
  }

  private void gc()
  {
    new Thread()
    {
      public void run()
      {
        System.gc();
      }
    }.start();
  }

  private static class SpawnRubyThread extends Thread
  {
    public Production production;
    public ProductionProxy proxy;
    private String src;
    private Ruby ruby;
    private Exception error;

    public SpawnRubyThread(String src, int id)
    {
      super("SpawnRuby_" + id);
      this.src = src;
    }

    public void run()
    {
      try
      {
        List<String> loadPaths = new ArrayList<String>();
        final String rubyLibDir = new File(Context.instance().limelightHome + "/ruby/lib").getCanonicalPath();
        loadPaths.add(rubyLibDir);
        RubyInstanceConfig config = new RubyInstanceConfig();
        config.setObjectSpaceEnabled(true);
        ruby = JavaEmbedUtils.initialize(loadPaths, config);
        InputStream input = new ByteArrayInputStream(src.getBytes());
        ruby.runFromMain(input, Context.instance().limelightHome + "/ruby/" + getName());
      }
      catch(Exception e)
      {
        error = e;
      }
    }

    public Ruby getRuby()
    {
      return ruby;
    }

    public void clear() //Don't keep any references around so the Ruby instance can be GC'ed.
    {
      ruby = null;
      production = null;
      proxy = null;
      error = null;
    }
  }

  public static class TearDownRubyThread extends Thread
  {
    private Ruby ruby;
    private Collection<Ruby> otherRubies;

    public TearDownRubyThread(Ruby ruby, Collection<Ruby> otherRubies)
    {
      this.ruby = ruby;
      this.otherRubies = otherRubies;
    }

    public void run()
    {
      Thread.yield(); // Allow the call stack to finish with any references belonging to the dying Ruby instance
      ruby.tearDown();
      ruby = null;
      clearProxyReferences(); // To make the Ruby instance GC-able. http://jira.codehaus.org/browse/JRUBY-4165
      System.gc();
    }

    private void clearProxyReferences()
    {
      try
      {
        for(Ruby otherRuby : otherRubies)
        {
          JavaSupport javaSupport = otherRuby.getJavaSupport();
          Field field = javaSupport.getClass().getDeclaredField("javaClassCache");
          field.setAccessible(true);
          final Object object = field.get(javaSupport);
          if(object instanceof ConcurrentHashMap)
          {
            ConcurrentHashMap classCache = (ConcurrentHashMap) object;
            for(Object classObject : classCache.keySet())
            {
              Class aClass = (Class)classObject;
              if(aClass.toString().startsWith("class $Proxy"))
                classCache.remove(aClass);
            }
          }
        }
      }
      catch(Exception e)
      {
        e.printStackTrace();
      }
    }
  }
}
