package limelight.os;

import java.io.IOException;

public class RuntimeExecution implements SystemExecution
{

  public Process exec(String[] strings) throws IOException
  {
    Runtime rt = Runtime.getRuntime();
    return rt.exec(strings);
  }

  public Process exec(String s) throws IOException
  {
    Runtime rt = Runtime.getRuntime();
    return rt.exec(s);
  }
}
