package limelight;

import org.jruby.Ruby;
import org.jruby.RubyInstanceConfig;
import org.jruby.RubyString;
import org.jruby.runtime.GlobalVariable;

import java.io.FileInputStream;
import java.io.InputStream;

public class Main
{
  public static String LIMELIGHT_HOME = System.getProperty("limelight.home");

  public static void main(String[] args) throws Exception
  {
    String mainRubyFile = LIMELIGHT_HOME + "/ruby_src/lib/limelight.rb";
//    args = adjustArgs(args, mainRubyFile);

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

    RubyInstanceConfig config = new RubyInstanceConfig();
    config.processArguments(args);

    String sceneName = LIMELIGHT_HOME + "/startup_production";
    if(config.getScriptFileName() != null)
      sceneName = config.getScriptFileName();

    Ruby runtime = Ruby.newInstance(config);
    runtime.defineGlobalConstant("LIMELIGHT_STARTUP_PRODUCTION", RubyString.newString(runtime, sceneName));
    runtime.defineVariable(new GlobalVariable(runtime, "LIMELIGHT_STARTUP_PRODUCTION", RubyString.newString(runtime, "examples/sandbox")));

    InputStream in = new FileInputStream(mainRubyFile);
    runtime.runFromMain(in, mainRubyFile);
  }

  private static String[] adjustArgs(String[] args, String mainRubyFile)
  {
    int argLength = args.length;
    String[] adjustedArgs = new String[argLength + 1];
    for (int i = 0; i < args.length; i++)
      adjustedArgs[i] = args[i];
    adjustedArgs[argLength] = adjustedArgs[argLength - 1];
    adjustedArgs[argLength - 1] = mainRubyFile;

    args = adjustedArgs;
    return args;
  }
}