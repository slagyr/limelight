package limelight.os.darwin;

import limelight.os.IRuntime;

import java.io.IOException;

public class MockRuntime implements IRuntime
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


