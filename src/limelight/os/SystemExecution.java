package limelight.os;

public interface SystemExecution
{
  public java.lang.Process exec(java.lang.String[] strings) throws java.io.IOException;
  public java.lang.Process exec(java.lang.String s) throws java.io.IOException;
}
