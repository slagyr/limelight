//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.model;

import limelight.Context;
import limelight.LimelightException;
import limelight.builtin.BuiltinBeacon;
import limelight.events.Event;
import limelight.events.EventAction;
import limelight.io.*;
import limelight.model.events.ProductionClosedEvent;
import limelight.model.events.ProductionEvent;
import limelight.model.api.UtilitiesProduction;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.io.PrintWriter;
import java.io.ByteArrayOutputStream;

public class Studio
{
  Production productionStub; // Used for testing

  private static Studio instance;
  private final List<Production> index;
  public Thread shutdownThread;
  private boolean isShutdown;
  private boolean isShuttingDown;
  private UtilitiesProduction utilitiesProduction;
  private Packer packer = new Packer();
  private FileSystem fs;

  public static Studio install()
  {
    Context.instance().studio = instance();
    return instance();
  }

  public static void uninstall()
  {
    Context.instance().studio = instance = null;
  }

  public static Studio instance()
  {
    if(instance == null)
      instance = new Studio();
    return instance;
  }

  public Studio()
  {
    index = new LinkedList<Production>();
    fs = Context.fs();
  }

  public Production open(String productionPath)
  {
    try
    {
      productionPath = processProductionPath(productionPath);
      Production production = productionStub == null ? instantiateProduction(productionPath) : productionStub;
      production.open();
      add(production);
      return production;
    }
    catch(Exception e)
    {
e.printStackTrace();
      alert(e);
      shutdownIfEmpty();
      return null;
    }
  }

  private Production instantiateProduction(String productionPath)
  {
    String className = calculateProductionClassName(productionPath);
    try
    {
      Class productionClass = Class.forName(className);
      final Constructor constructor = productionClass.getConstructor(String.class);
      return (Production)constructor.newInstance(productionPath);
    }
    catch(Exception e)
    {
      e.printStackTrace();
      throw new RuntimeException();
    }
  }

  public String calculateProductionClassName(String path)
  {
    if(fs.exists(fs.join(path, "production.rb")))
      return "limelight.ruby.RubyProduction";
    else if(fs.exists(fs.join(path, "production.clj")))
      return "limelight.clojure.ClojureProduction";
    else if(fs.exists(fs.join(path, "production.xml")))
      return "limelight.java.JavaProduction";
    else
      throw new LimelightException("Can't determine what language to use to load production: " + path);
  }

  public void shutdown()
  {
    if(isShutdown || isShuttingDown || !shouldAllowShutdown())
      return;

    isShuttingDown = true;

    for(Production production: index)
      production.finalizeClose();

    if(utilitiesProduction != null)
      utilitiesProduction.close();

    isShutdown = true;

    shutdownThread = new Thread()
    {
      public void run()
      {
        Context.instance().shutdown();
      }
    };
    shutdownThread.start();
  }

  public void add(Production production)
  {
    adjustNameIfNeeded(production);
    synchronized(index)
    {
      index.add(production);
    }
    production.getEventHandler().add(ProductionClosedEvent.class, ProductionClosedHandler.instance);
  }

  public Production get(String name)
  {
    for(Production production: index)
    {
      if(name.equals(production.getName()))
        return production;
    }
    return null;
  }

  public boolean shouldAllowShutdown()
  {
    for(Production production: index)
    {
      if(!production.allowClose())
        return false;
    }
    return true;
  }

  public List<Production> getProductions()
  {
    return new ArrayList<Production>(index);
  }

  public boolean isShutdown()
  {
    return isShutdown;
  }

  public UtilitiesProduction utilitiesProduction()
  {
    if(utilitiesProduction == null)
    {
//      String path = FileUtil.pathTo(Context.instance().limelightHome, "ruby", "lib", "limelight", "builtin", "utilities_production");
      String path = fs.join(BuiltinBeacon.getBuiltinProductionsPath(), "utilities");
System.err.println("utilities path = " + path);
      try
      {
        Production production = productionStub == null ? instantiateProduction(path) : productionStub;
        production.open();
        add(production);
        utilitiesProduction = new UtilitiesProduction(production);
      }
      catch(Exception e)
      {
        e.printStackTrace();
        shutdown();
      }
    }
    return utilitiesProduction;
  }

  public void stubUtilitiesProduction(Production stub)
  {
    utilitiesProduction = new UtilitiesProduction(stub);
  }

  private void adjustNameIfNeeded(Production production)
  {
    String name = production.getName();
    name = name == null ? "" : name.trim();
    if(name.length() == 0)
      name = "anonymous";

    String baseName = name;
    for(int i = 2; nameTaken(name); i++)
      name = baseName + "_" + i;

    production.setName(name);
  }

  private void productionClosed(Production production)
  {
    synchronized(index)
    {
      index.remove(production);
    }

    if(index.isEmpty())
      Context.instance().shutdown();
  }

  private void alert(Exception error)
  {
    try
    {
      ByteArrayOutputStream output = new ByteArrayOutputStream();
      PrintWriter writer = new PrintWriter(output);
      error.printStackTrace(writer);
      writer.flush();
      String message = new String(output.toByteArray());
      utilitiesProduction().alert(message);
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
  }

  public boolean canProceedWithIncompatibleVersion(String name, String minimumLimelightVersion)
  {
    return utilitiesProduction().shouldProceedWithIncompatibleVersion(name, minimumLimelightVersion);
  }

  private boolean nameTaken(String name)
  {
    return get(name) != null;
  }

  public void setPacker(Packer packer)
  {
    this.packer = packer;
  }

  public String processProductionPath(String productionPath)
  {
    if(fs.isDirectory(productionPath))
      return productionPath;
    else if(".llp".equals(fs.fileExtension(productionPath)))
      return unpackLlp(productionPath);
    else if(".lll".equals(fs.fileExtension(productionPath)))
      return downloadLll(productionPath);
    else
      throw new LimelightException("I don't know how to open this production: " + productionPath);
  }

  private String downloadLll(String productionPath)
  {
    try
    {
      String url = fs.readTextFile(productionPath).trim();
      String result = Downloader.get(url);
      return unpackLlp(result);
    }
    catch(Exception e)
    {
      throw new LimelightException("Failed to download or unpack .lll: " + productionPath, e);
    }
  }

  private String unpackLlp(String productionPath)
  {
    String destinationDir = fs.join(Data.productionsDir(), "" + System.currentTimeMillis());
    fs.createDirectory(destinationDir);
    return packer.unpack(productionPath, destinationDir);
  }

  private static class ProductionClosedHandler implements EventAction
  {
    private static ProductionClosedHandler instance = new ProductionClosedHandler();
    public void invoke(Event event)
    {
      Studio.instance().productionClosed(((ProductionEvent)event).getProduction());
    }
  }

  private void shutdownIfEmpty()
  {
    if(index.isEmpty())
      shutdown();
  }
}
