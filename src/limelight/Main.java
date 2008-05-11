package limelight;

import limelight.io.TempDirectory;
import org.jruby.Ruby;
import org.jruby.RubyInstanceConfig;
import org.jruby.RubyString;
import org.jruby.runtime.GlobalVariable;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class Main
{
  public static String LIMELIGHT_HOME = System.getProperty("limelight.home");
  private RubyInstanceConfig config;
  private Ruby runtime;

  public static void main(String[] args) throws Exception
  {
    new Main().run(args);
  }

  public void run(String[] args) throws Exception
  {
    configureSystemProperties();
    processArgs(args);
    runtime = Ruby.newInstance(config);
    injectStartupProduction(getStartupProduction());
    configureContext();
    startJrubyRuntime();
  }

  private void startJrubyRuntime() throws FileNotFoundException
  {
    String mainRubyFile = LIMELIGHT_HOME + "/lib/limelight.rb";
    InputStream in = new FileInputStream(mainRubyFile);
    runtime.runFromMain(in, mainRubyFile);
  }

  private void injectStartupProduction(String productionName)
  {
    runtime.defineGlobalConstant("LIMELIGHT_STARTUP_PRODUCTION", RubyString.newString(runtime, productionName));
    runtime.defineVariable(new GlobalVariable(runtime, "LIMELIGHT_STARTUP_PRODUCTION", RubyString.newString(runtime, "examples/sandbox")));
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
    Context context = Context.instance();
    context.tempDirectory = new TempDirectory();
  }
}