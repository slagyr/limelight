//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.io;

import limelight.Context;
import limelight.LimelightException;

import java.io.InputStream;
import java.io.OutputStream;

public class Packer
{
  public String unpack(String packagePath)
  {
    String destination = Context.instance().tempDirectory.createNewDirectory();
    return unpack(packagePath, destination);
  }

  public String unpack(String packagePath, String destination)
  {
    try
    {
      InputStream input = Context.fs().inputStream(packagePath);
      DirectoryZipper zipper = DirectoryZipper.fromZip(input);
      zipper.unzip(destination);
      return zipper.getDirectoryPath();
    }
    catch(Exception e)
    {
      throw new LimelightException("Failed to unpack production: " + packagePath, e);
    }
  }

  public void pack(String productionPath)
  {
    pack(productionPath, null);
  }

  public void pack(String productionPath, String llpName)
  {
    try
    {
      DirectoryZipper zipper = DirectoryZipper.fromDir(productionPath);
      if(llpName == null)
        llpName = zipper.getProductionName();
      OutputStream output = Context.fs().outputStream(llpName + ".llp");
      zipper.zipTo(output);
    }
    catch(Exception e)
    {
      throw new LimelightException("Failed to pack production: " + productionPath, e);
    }
  }
}
