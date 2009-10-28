//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.api;

import limelight.Context;
import limelight.RuntimeFactory;
import limelight.LimelightException;
import limelight.io.FileUtil;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.io.PrintWriter;
import java.io.ByteArrayOutputStream;


public class Studio
{
  private static Studio instance;
  private List<RuntimeFactory.BirthCertificate> index;
  public Thread shutdownThread;
  private boolean isShutdown;
  private boolean isShuttingDown;
  private RuntimeFactory.BirthCertificate utilitiesCertificate;

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
    index = new LinkedList<RuntimeFactory.BirthCertificate>();
  }

  public Production open(String productionPath)
  {
    try
    {
      String src = RuntimeFactory.openProductionSrc(productionPath);
      RuntimeFactory.BirthCertificate certificate = Context.instance().runtimeFactory.spawn(src);
      if(certificate != null)
      {
        add(certificate);
        return certificate.production;
      }
      else
        return null;
    }
    catch(Exception e)
    {
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

    List<RuntimeFactory.BirthCertificate> certificates = new ArrayList<RuntimeFactory.BirthCertificate>(index);
    for(RuntimeFactory.BirthCertificate certificate : certificates)
      certificate.production.close();

    if(utilitiesCertificate != null)
    {
      utilitiesCertificate.production.close();
      if(Context.instance().runtimeFactory != null)
        Context.instance().runtimeFactory.terminate(utilitiesCertificate);
    }
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

  public void add(RuntimeFactory.BirthCertificate certificate)
  {
    adjustNameIfNeeded(certificate.production);
    index.add(certificate);
  }

  public Production get(String name)
  {
    for(RuntimeFactory.BirthCertificate certificate : index)
    {
      if(name.equals(certificate.production.getName()))
        return certificate.production;
    }
    return null;
  }

  public boolean shouldAllowShutdown()
  {
    for(RuntimeFactory.BirthCertificate certificate : index)
    {
      if(!certificate.production.allowClose())
        return false;
    }
    return true;
  }

  public void productionClosed(Production production)
  {
    RuntimeFactory.BirthCertificate certificate = certificateFor(production);
    if(certificate == null)
      return;

    index.remove(certificate);

    Context.instance().runtimeFactory.terminate(certificate);
    if(index.isEmpty())
      Context.instance().shutdown();
  }

  public List<Production> getProductions()
  {
    ArrayList<Production> result = new ArrayList<Production>();
    for(RuntimeFactory.BirthCertificate certificate : index)
      result.add(certificate.production);
    return result;
  }

  public boolean isShutdown()
  {
    return isShutdown;
  }

  public UtilitiesProduction utilitiesProduction() throws Exception
  {
    if(utilitiesCertificate == null)
    {
      String path = FileUtil.pathTo(Context.instance().limelightHome, "lib", "limelight", "builtin", "utilities_production");
      String src = RuntimeFactory.openProductionSrc(path);
      utilitiesCertificate = Context.instance().runtimeFactory.spawn(src);
      utilitiesCertificate.production = new UtilitiesProduction(utilitiesCertificate.production);
    }
    return (UtilitiesProduction)utilitiesCertificate.production;
  }

  public void stubUtilitiesProduction(Production stub)
  {
    utilitiesCertificate = new RuntimeFactory.BirthCertificate(new UtilitiesProduction(stub));
  }

  public void errorIfDuplicateName(String name)
  {
    if(get(name) != null)
      throw new LimelightException("Production name '" + name + "' is already taken");
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

  private RuntimeFactory.BirthCertificate certificateFor(Production production)
  {
    for(RuntimeFactory.BirthCertificate certificate : index)
    {
      if(production == certificate.production)
        return certificate;
    }
    return null;
  }
}
