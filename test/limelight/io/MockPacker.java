//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.io;

public class MockPacker extends Packer
{
  public String unpackPackagePath;
  public String unpackResult;
  public String unpackDestination;
  public String packProductionPath;
  public String packLlpName;

  @Override
  public String unpack(String packagePath, String destination)
  {
    unpackPackagePath = packagePath;
    unpackDestination = destination;
    return unpackResult;
  }

  @Override
  public void pack(String productionPath, String llpName)
  {
    packProductionPath = productionPath;
    packLlpName = llpName;
  }
}
