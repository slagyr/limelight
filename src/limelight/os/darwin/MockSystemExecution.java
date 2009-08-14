package limelight.os.darwin;

import limelight.os.SystemExecution;

import java.io.IOException;

public class MockSystemExecution implements SystemExecution
{
  String command;
  public Process exec(String[] strings) throws IOException
  {
    return null;
  }

  public Process exec(String s) throws IOException
  {
    command = s;
    return null;
  }

  public boolean receivedExecWith(String cmd)
  {
    return (this.command.compareTo(cmd) == 0);
  }
}