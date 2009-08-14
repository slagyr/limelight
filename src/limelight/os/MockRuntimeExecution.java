package limelight.os;

import java.io.IOException;

public class MockRuntimeExecution extends RuntimeExecution
{
  public String[] command;

  public Process exec(String... strings) throws IOException
  {
    command = strings;
    return null;
  }
}
