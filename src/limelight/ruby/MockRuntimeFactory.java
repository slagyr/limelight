//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ruby;

import limelight.LimelightException;
import limelight.model.Production;

public class MockRuntimeFactory extends RuntimeFactory
{
  public LimelightException spawnException;
  public Production spawnedProduction;

  public BirthCertificate spawn(String src)
  {
    if(spawnException != null)
      throw spawnException;

    return new BirthCertificate(spawnedProduction);
  }
}
