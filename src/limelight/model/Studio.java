//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.model;

import limelight.Context;
import limelight.ruby.RubyProduction;
import limelight.ruby.RuntimeFactory;
import limelight.ui.api.UtilitiesProduction;
import limelight.io.FileUtil;

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
  }

  public Production open(String productionPath)
  {
    try
    {
      Production production = productionStub == null ? new RubyProduction(productionPath) : productionStub;
      production.open();
      add(production);
      return production;
    }
    catch(Exception e)
    {
e.printStackTrace();      
      alert(e);
      if(index.isEmpty())
        shutdown();
      return null;
    }
  }

  public void shutdown()
  {
    if(isShutdown || isShuttingDown || !shouldAllowShutdown())
      return;

    isShuttingDown = true;

    for(Production production: index)
      production.close();

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

  public void productionClosed(Production production)
  {
    synchronized(index)
    {
      index.remove(production);
    }

    production.close();
    if(index.isEmpty())
      Context.instance().shutdown();
  }

  public List<Production> getProductions()
  {
    return new ArrayList<Production>(index);
  }

  public boolean isShutdown()
  {
    return isShutdown;
  }

  public UtilitiesProduction utilitiesProduction() throws Exception
  {
    if(utilitiesProduction == null)
    {
      String path = FileUtil.pathTo(Context.instance().limelightHome, "lib", "limelight", "builtin", "utilities_production");
      try
      {
        utilitiesProduction = new UtilitiesProduction(open(path));
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

  private boolean nameTaken(String name)
  {
    return get(name) != null;
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
      utilitiesProduction().callMethod("alert", message);
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
  }
}
