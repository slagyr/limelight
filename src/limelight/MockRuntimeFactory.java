//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

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
