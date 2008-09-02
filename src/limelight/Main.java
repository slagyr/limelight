//- Copyright 2008 8th Light, Inc.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight;

import limelight.io.Downloader;
import limelight.io.FileUtil;
import limelight.io.TempDirectory;
import limelight.ui.Panel;
import limelight.ui.painting.VerboseRepaintManager;
import limelight.ui.model.FrameManager;
import limelight.task.TaskEngine;
import limelight.task.RecurringTask;
import limelight.caching.TimedCache;
import limelight.audio.RealAudioPlayer;
import org.jruby.Ruby;
import org.jruby.RubyInstanceConfig;

import javax.swing.*;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.awt.image.BufferedImage;

public class
    Main
{
  public static String LIMELIGHT_HOME = System.getProperty("limelight.home");
  private RubyInstanceConfig config;
  private Ruby runtime;
  private boolean contextIsConfigured;

  public static void main(String[] args) throws Exception
  {
    new Main().run(args);
  }

  public static void initializeContext() throws Exception
  {
    new Main().configureContext();
  }

  public void run(String[] args) throws Exception
  {
    configureSystemProperties();
    processArgs(args);
    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    runtime = Ruby.newInstance(config);
    configureContext();
    startJrubyRuntime(getStartupProduction());
  }

  private void startJrubyRuntime(String startupProduction) throws FileNotFoundException
  {
    StringBuffer startupRuby = new StringBuffer();
    startupRuby.append("require '").append(FileUtil.pathTo(LIMELIGHT_HOME, "lib", "init")).append("'").append("\n");
    startupRuby.append("require 'limelight/producer'").append("\n");
    startupRuby.append("Limelight::Producer.open('").append(startupProduction).append("')").append("\n");

    ByteArrayInputStream input = new ByteArrayInputStream(startupRuby.toString().getBytes());
    runtime.runFromMain(input, "limelight_pseudo_main");
  }

  private String getStartupProduction()
  {
    String productionName = LIMELIGHT_HOME + "/productions/startup";
    if(config.getScriptFileName() != null)
      productionName = config.getScriptFileName();
    return productionName;
  }

  private void processArgs(String[] args)
  {
    config = new RubyInstanceConfig();
    config.processArguments(args);
  }

  private void configureSystemProperties()
  {
    System.setProperty("jruby.base", "");
    System.setProperty("jruby.home", LIMELIGHT_HOME + "/jruby");
    System.setProperty("jruby.lib", LIMELIGHT_HOME + "/jruby/lib");
    if(System.getProperty("mrj.version") == null)  // WINDOWS
    {
      System.setProperty("jruby.shell", "cmd.exe");
      System.setProperty("jruby.script", "jruby.bat org.jruby.Main");
    }
    else // OS X
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
    context.tempDirectory = new TempDirectory();
    context.downloader = new Downloader(context.tempDirectory);
    context.taskEngine = new TaskEngine().started();
    context.bufferedImageCache = new TimedCache<Panel, BufferedImage>(1);
    context.frameManager = new FrameManager();
    context.audioPlayer = new RealAudioPlayer();
    context.bufferedImagePool = new BufferedImagePool(1);

    addBufferedImageCacheCleanerTask(context);
    addBufferedImagePoolCleanerTask(context);
    context.taskEngine.add(new PanelPainterTask());

    contextIsConfigured = true;
  }

  private void addBufferedImageCacheCleanerTask(Context context)
  {
    context.taskEngine.add(new RecurringTask("Buffered Image Cache Cleaner", 1) {
      protected void doPerform()
      {
        try
        {
          Context.instance().bufferedImageCache.clean();
        }
        catch(Exception e)
        {
          e.printStackTrace();
        }
      }
    });
  }


  private void addBufferedImagePoolCleanerTask(Context context)
  {
    context.taskEngine.add(new RecurringTask("Buffered Image Pool Cleaner", 1) {
      protected void doPerform()
      {
        try
        {
          Context.instance().bufferedImagePool.clean();
        }
        catch(Exception e)
        {
          e.printStackTrace();
        }
      }
    });
  }

}