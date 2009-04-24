//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight;

import limelight.audio.RealAudioPlayer;
import limelight.background.AnimationLoop;
import limelight.background.CacheCleanerLoop;
import limelight.background.PanelPainterLoop;
import limelight.caching.TimedCache;
import limelight.io.FileUtil;
import limelight.io.TempDirectory;
import limelight.ui.Panel;
import limelight.ui.model.AlertFrameManager;
import limelight.ui.model.InertFrameManager;
import org.jruby.Ruby;
import org.jruby.javasupport.JavaEmbedUtils;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;

public class Main
{
  private Ruby runtime;
  private boolean contextIsConfigured;
  private Context context;
  public static boolean startupProvided;
  private String productionName;

  public static void main(String[] args) throws Exception
  {
    new Main().start(args);
  }

  public static void initializeContext()
  {
    new Main().configureContext();
  }

  public static void initializeTestContext()
  {
    new Main().configureContext();
  }

  public void start(String[] args) throws Exception
  {
    try
    {
      context = Context.instance();
      boolean usingStartupNotifications = context.isOsx() && context.runningAsApp;
      if(usingStartupNotifications)
        StartupListener.register();
      configureSystemProperties();

      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      configureContext();

      if(args.length > 0)
        productionName = args[0];

      startJruby();

      if(!usingStartupNotifications)
        Context.instance().studio.open(getStartupProduction());
      else if(!startupProvided)
        StartupListener.instance.startupPerformed(getStartupProduction());
    }
    catch(Throwable e)
    {
      handleError(e);
    }
  }

  private void startJruby()
  {
    runtime = JavaEmbedUtils.initialize(new ArrayList());

    StringBuffer startupRuby = new StringBuffer();
    startupRuby.append("require '").append(FileUtil.pathTo(context.limelightHome, "lib", "init")).append("'").append("\n");
    startupRuby.append("require 'limelight/studio'").append("\n");
    startupRuby.append("Limelight::Studio.install").append("\n");
    ByteArrayInputStream input = new ByteArrayInputStream(startupRuby.toString().getBytes());
    
    runtime.runFromMain(input, "limelight_pseudo_main");
  }

  public static void handleError(Throwable e)
  {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    PrintWriter writer = new PrintWriter(byteArrayOutputStream);
    e.printStackTrace(writer);
    writer.flush();
    JOptionPane.showMessageDialog(new JFrame(), new String(byteArrayOutputStream.toByteArray()), "Limelight Error", JOptionPane.WARNING_MESSAGE);
  }

  private String getStartupProduction()
  {
    String productionName = context.limelightHome + "/productions/startup";
    if(productionProvided())
      productionName = this.productionName;
    return productionName;
  }

  private boolean productionProvided()
  {
    return productionName != null;
  }

  private void configureSystemProperties()
  {
//    System.setProperty("apple.laf.useScreenMenuBar", "true");
    System.setProperty("jruby.base", "");
//    System.setProperty("jruby.home", context.limelightHome + "/jruby");
    System.setProperty("jruby.lib", context.limelightHome + "/jruby/lib");
    if(context.isWindows())
    {
      System.setProperty("jruby.shell", "cmd.exe");
      System.setProperty("jruby.script", "jruby.bat org.jruby.Main");
    }
    else
    {
      System.setProperty("jruby.shell", "/bin/sh");
      System.setProperty("jruby.script", "jruby");
    }
  }

  public void configureContext()
  {
//VerboseRepaintManager.install();
    if(contextIsConfigured)
      return;
    Context context = Context.instance();

    context.keyboardFocusManager = new KeyboardFocusManager().installed();
    initializeTempDirectory();
    context.frameManager = new AlertFrameManager();
    context.audioPlayer = new RealAudioPlayer();

    context.bufferedImageCache = new TimedCache<Panel, BufferedImage>(1);
    context.bufferedImagePool = new BufferedImagePool(1);

    context.panelPanter = new PanelPainterLoop().started();
    context.animationLoop = new AnimationLoop().started();
    context.cacheCleaner = new CacheCleanerLoop().started();

    contextIsConfigured = true;
  }

  public void configureTestContext()
  {
    if(contextIsConfigured)
      return;
    Context context = Context.instance();

    context.keyboardFocusManager = new KeyboardFocusManager().installed();
    initializeTempDirectory();
    context.frameManager = new InertFrameManager();
    context.audioPlayer = new RealAudioPlayer();

    context.bufferedImageCache = new TimedCache<Panel, BufferedImage>(1);
    context.bufferedImagePool = new BufferedImagePool(1);

    context.panelPanter = new PanelPainterLoop();
    context.animationLoop = new AnimationLoop();
    context.cacheCleaner = new CacheCleanerLoop();

    contextIsConfigured = true;
  }

  public static void initializeTempDirectory()
  {
    Context.instance().tempDirectory = new TempDirectory();
  }
}