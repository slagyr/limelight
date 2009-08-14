package limelight.os.win32;
import limelight.os.SystemExecution;

import java.io.IOException;

public class MockSystemExecution implements SystemExecution
{
  String[] params;
  
  public boolean receivedExecWith(String[] params)
  {
    for (int i=0; i < this.params.length; i++)
    {
      if (params[i] != this.params[i])
      {
        return false;
      }
    }

    return true;
  }

  public java.lang.Process exec(String[] strings)
  {
    params = strings;
    return null;
  }

  public Process exec(String s) throws IOException
  {
    return null;
  }

}
