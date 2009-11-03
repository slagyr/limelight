//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight;

import limelight.audio.RealAudioPlayer;
import limelight.background.AnimationLoop;
import limelight.background.CacheCleanerLoop;
import limelight.background.PanelPainterLoop;
import limelight.caching.TimedCache;
import limelight.io.TempDirectory;
import limelight.ui.Panel;
import limelight.ui.model.AlertFrameManager;
import limelight.ui.model.InertFrameManager;
import limelight.os.UnsupportedOS;
import limelight.os.MockOS;
import limelight.os.OS;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class Main
{
  private boolean contextIsConfigured;
  private Context context;
  private static String startupProductionPath;

  public static void main(String[] args) throws Exception
  {
    new Main().start(args);
  }

  public static void initializeContext() throws Exception
  {
    new Main().configureContext();
  }

  public static void initializeTestContext() throws Exception
  {
    new Main().configureContext();
  }

  public void start(String[] args) throws Exception
  {
    try
    {
      configureOS();
      configureContext();
      configureSystemProperties();

      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

      if(args.length > 0 && startupProductionPath == null)
        startupProductionPath = args[0];
      
      Context.instance().os.openProduction(getStartupProductionPath());
    }
    catch(Throwable e)
    {
      handleError(e);
    }
  }

  public static void handleError(Throwable e)
  {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    PrintWriter writer = new PrintWriter(byteArrayOutputStream);
    e.printStackTrace(writer);
    writer.flush();
    JOptionPane.showMessageDialog(new JFrame(), new String(byteArrayOutputStream.toByteArray()), "Limelight Error", JOptionPane.WARNING_MESSAGE);
  }

  private String getStartupProductionPath()
  {
    String productionName = context.limelightHome + "/productions/playbills.lll";
    if(productionProvided())
      productionName = startupProductionPath;
    return productionName;
  }

  private boolean productionProvided()
  {
    return startupProductionPath != null;
  }

  public void configureSystemProperties()
  {
    System.setProperty("jruby.interfaces.useProxy", "true");
    Context.instance().os.configureSystemProperties();
  }

  static void configureOS() throws Exception
  {
    Context context = Context.instance();
    String className = "limelight.os.UnsupportedOS";
    if(System.getProperty("os.name").indexOf("Windows") != -1)
      className = "limelight.os.win32.Win32OS";
    else if(System.getProperty("os.name").indexOf("Mac OS X") != -1)
      className = "limelight.os.darwin.DarwinOS";

    try
    {
      Class klass = Thread.currentThread().getContextClassLoader().loadClass(className);
      Constructor constructor = klass.getConstructor();
      context.os = (OS)constructor.newInstance();
    }
    catch(Exception e)
    {
      System.err.println("OS class could not be loaded:" + e);
      context.os = new UnsupportedOS();
    }

    context.os.appIsStarting();   
  }

  public void configureContext() throws Exception
  {
//VerboseRepaintManager.install();
    if(contextIsConfigured)
      return;
    contextIsConfigured = true;

    context = Context.instance();
    if(context.os == null)
      configureOS();

    context.frameManager = new AlertFrameManager();

    installCommonConfigComponents();

    context.panelPanter = new PanelPainterLoop().started();
    context.animationLoop = new AnimationLoop().started();
    context.cacheCleaner = new CacheCleanerLoop().started();

  }

  public void configureTestContext()
  {
    if(contextIsConfigured)
      return;
    contextIsConfigured = true;

    context = Context.instance();
    context.frameManager = new InertFrameManager();
    context.os = new MockOS();

    installCommonConfigComponents();

    context.panelPanter = new PanelPainterLoop();
    context.animationLoop = new AnimationLoop();
    context.cacheCleaner = new CacheCleanerLoop();
  }

  private void installCommonConfigComponents()
  {
    context.keyboardFocusManager = new KeyboardFocusManager().installed();
    initializeTempDirectory();
    context.audioPlayer = new RealAudioPlayer();
    context.bufferedImageCache = new TimedCache<Panel, BufferedImage>(1);
    context.bufferedImagePool = new BufferedImagePool(1);
    context.runtimeFactory = new RuntimeFactory();
    context.studio = new Studio();
  }

  public static void initializeTempDirectory()
  {
    Context.instance().tempDirectory = new TempDirectory();
  }

  public void setContext(Context context)
  {
    this.context = context;
  }

  public static void setStartupPath(String productionPath)
  {
    startupProductionPath = productionPath;
  }
}