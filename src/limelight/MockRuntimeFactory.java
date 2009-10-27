package limelight;

import limelight.ui.api.Production;

public class MockRuntimeFactory extends RuntimeFactory
{
  public Exception spawnException;
  public Production spawnedProduction;

  public BirthCertificate spawn(String src) throws Exception
  {
    if(spawnException != null)
      throw spawnException;

    return new BirthCertificate(spawnedProduction);
  }
}
