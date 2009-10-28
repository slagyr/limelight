//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight;

import org.jruby.javasupport.JavaEmbedUtils;
import org.jruby.javasupport.JavaSupport;
import org.jruby.javasupport.JavaClass;
import org.jruby.Ruby;
import org.jruby.RubyInstanceConfig;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.lang.reflect.Field;

import limelight.ui.api.Production;

public class RuntimeFactory
{
  private static int runtimeId = 0;
  private HashMap<Object, Ruby> index = new HashMap<Object, Ruby>();

  public BirthCertificate spawn(String src) throws Exception
  {
    Runtime runtime = new Runtime(src, nextId());
    runtime.start();

    runtime.join();

    try
    {
      if(runtime.getError() != null)
        throw runtime.getError();
      else if(runtime.getHandle() != null)
      {
        BirthCertificate certificate = new BirthCertificate(runtime.getHandle());
        index.put(certificate.getCode(), runtime.getRuby());
        return certificate;
      }
      else
      {
        runtime.getRuby().tearDown();
        runtime = null;
        gc();
        return null;
      }
    }
    finally
    {
      if(runtime != null)
        runtime.clear();
    }
  }

  public void terminate(BirthCertificate certificate)
  {
    Ruby ruby = index.remove(certificate.getCode());
    certificate.production = null;
    if(ruby != null)
    {
      new RubyTearDownThread(ruby, index).start();
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

  private static int nextId()
  {
    return ++runtimeId;
  }

  public Ruby get(BirthCertificate key)
  {
    return index.get(key.getCode());
  }

  public static class RubyTearDownThread extends Thread
  {
    private Ruby ruby;
    private HashMap<Object, Ruby> index;

    public RubyTearDownThread(Ruby ruby, HashMap<Object, Ruby> index)
    {
      this.ruby = ruby;
      this.index = index;
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
        for(Ruby otherRuby : index.values())
        {
          JavaSupport javaSupport = otherRuby.getJavaSupport();
          Field field = javaSupport.getClass().getDeclaredField("javaClassCache");
          field.setAccessible(true);
          ConcurrentHashMap<Class,JavaClass> classCache =  (ConcurrentHashMap<Class,JavaClass>)field.get(javaSupport);
          for(Class aClass : classCache.keySet())
          {
            if(aClass.toString().startsWith("class $Proxy"))
              classCache.remove(aClass);
          }
        }
      }
      catch(Exception e)
      {
        e.printStackTrace();
      }
    }
  }

  public static class BirthCertificate
  {
    private long code;
    public Production production;

    public BirthCertificate(Production production)
    {
      code = System.currentTimeMillis();
      this.production = production;
    }

    public long getCode()
    {
      return code;
    }

    public String toString()
    {
      return "BirthCertificate(code: " + code + ", production: " + production + ")";
    }
  }

  public static class Runtime extends Thread
  {
    private Production handle;
    private String src;
    private Ruby ruby;
    private Exception error;

    public Runtime(String src, int i)
    {
      super("Runtime_" + i);
      this.src = src;
    }

    public Production getHandle()
    {
      return handle;
    }

    public void setHandle(Production aHandle)
    {
      handle = aHandle;
    }

    public Exception getError()
    {
      return error;
    }

    public void run()
    {
      try
      {
        List<String> loadPaths = new ArrayList<String>();
        loadPaths.add(new File(Context.instance.limelightHome + "/lib").getAbsolutePath());
        RubyInstanceConfig config = new RubyInstanceConfig();
        config.setObjectSpaceEnabled(true);
        ruby = JavaEmbedUtils.initialize(loadPaths, config);
        InputStream input = new ByteArrayInputStream(src.getBytes());
        ruby.runFromMain(input, Context.instance.limelightHome + "/" + getName());
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
      handle = null;
      error = null;
    }
  }

  public static String openProductionSrcTemplate = "" +
      "require 'limelight/limelight_init'\n" +
      "require 'limelight/producer'\n" +
      "producer = Limelight::Producer.new(\"PRODUCTION_PATH\")\n" +
      "production = producer.production\n" +
      "if producer.can_proceed_with_compatibility?\n" +
      " producer.open\n" +
      " Java::java.lang.Thread.currentThread.handle = production\n" +
      "end\n";

  public static String openProductionSrc(String productionPath)
  {
    return openProductionSrcTemplate.replace("PRODUCTION_PATH", productionPath);
//    return withErrorsCaught(openProductionSrcTemplate.replace("PRODUCTION_PATH", productionPath));
  }

  private static String withErrorsCaught(String src)
  {
    return "begin\n" + src + "rescue Exception => e\nputs e; puts e.bactrace\nend";
  }
}
