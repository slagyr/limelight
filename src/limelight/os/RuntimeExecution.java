package limelight.os;

import java.io.IOException;

public class RuntimeExecution
{
  public Process exec(String... strings) throws IOException
  {
    Runtime rt = Runtime.getRuntime();
    return rt.exec(strings);
  }
}
