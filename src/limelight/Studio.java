//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight;

import limelight.Context;
import limelight.RuntimeFactory;
import limelight.LimelightException;
import limelight.ui.api.Production;
import limelight.ui.api.UtilitiesProduction;
import limelight.io.FileUtil;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.io.PrintWriter;
import java.io.ByteArrayOutputStream;


public class Studio
{
  private static Studio instance;
  private List<ProductionWrapper> index;
  public Thread shutdownThread;
  private boolean isShutdown;
  private boolean isShuttingDown;
  private RuntimeFactory.BirthCertificate utilitiesCertificate;
  private boolean publishingOnDRb;
  private int nextDRbPort;

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
    index = new LinkedList<ProductionWrapper>();
  }

  public Production open(String productionPath)
  {
    try
    {
      String src = RuntimeFactory.openProductionSrc(productionPath);
      RuntimeFactory.BirthCertificate certificate = Context.instance().runtimeFactory.spawn(src);
      if(certificate != null)
      {
        ProductionWrapper wrapper = wrapIt(certificate);
        add(wrapper);
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

    for(ProductionWrapper wrappers : wrappers())
      wrappers.production.close();

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

  public void add(ProductionWrapper wrapper)
  {
    adjustNameIfNeeded(wrapper.production);
    synchronized(index)
    {
      index.add(wrapper);
    }
  }

  public Production get(String name)
  {
    for(ProductionWrapper wrapper : wrappers())
    {
      if(name.equals(wrapper.production.getName()))
        return wrapper.production;
    }
    return null;
  }

  public boolean shouldAllowShutdown()
  {
    for(ProductionWrapper wrapper : wrappers())
    {
      if(!wrapper.production.allowClose())
        return false;
    }
    return true;
  }

  public void productionClosed(Production production)
  {
    ProductionWrapper wrapper = wrapperFor(production);
    if(wrapper == null)
      return;

    synchronized(index)
    {
      index.remove(wrapper);
    }

    wrapper.production = null;
    Context.instance().runtimeFactory.terminate(wrapper.certificate);
    if(index.isEmpty())
      Context.instance().shutdown();
  }

  public List<Production> getProductions()
  {
    ArrayList<Production> result = new ArrayList<Production>();
    for(ProductionWrapper certificate : index)
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
    return (UtilitiesProduction) utilitiesCertificate.production;
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

  private ArrayList<ProductionWrapper> wrappers()
  {
    ArrayList<ProductionWrapper> wrappers = new ArrayList<ProductionWrapper>();
    synchronized(index)
    {
      wrappers.addAll(index);
    }
    return wrappers;
  }

  private ProductionWrapper wrapperFor(Production production)
  {
    for(ProductionWrapper wrapper : wrappers())
    {
      if(production == wrapper.production)
        return wrapper;
    }
    return null;
  }

  private ProductionWrapper wrapIt(RuntimeFactory.BirthCertificate certificate)
  {
    ProductionWrapper wrapper = new ProductionWrapper(certificate);
    if(publishingOnDRb)
      wrapper.production.publish_on_drb(nextDRbPort++);
    return wrapper;
  }

  public void publishProductionsOnDRb(int drbPort)
  {
    publishingOnDRb = true;
    nextDRbPort = drbPort;
  }

  public static class ProductionWrapper
  {
    public Production production;
    public RuntimeFactory.BirthCertificate certificate;
    public int drbPort;

    public ProductionWrapper(RuntimeFactory.BirthCertificate certificate)
    {
      this.certificate = certificate;
      production = certificate.production;
    }
  }
}
