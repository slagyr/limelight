package limelight;

import limelight.io.FileSystem;
import org.junit.internal.RealSystem;
import org.junit.runner.JUnitCore;

public class TestRunner
{
  public static void main(String[] args) throws ClassNotFoundException
  {
    String[] classNames = loadClassNames();
    JUnitCore.runMainAndExit(new RealSystem(), classNames);
  }

  private static String[] loadClassNames()
  {
    final FileSystem fs = new FileSystem();
    if(!fs.exists(".testClasses"))
      throw new RuntimeException(".testClasses file is missing");

    final String classesContent = fs.readTextFile(".testClasses");
    return classesContent.split("\n");
  }
}
