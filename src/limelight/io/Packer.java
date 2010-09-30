//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.io;

import limelight.Context;
import limelight.LimelightException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class Packer
{
  public String unpack(String packagePath)
  {
    File destination = Context.instance().tempDirectory.createNewDirectory();
    return unpack(packagePath, destination.getPath());
  }

  public String unpack(String packagePath, String destination)
  {
    try
    {
      FileInputStream input = new FileInputStream(packagePath);
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
      FileOutputStream output = new FileOutputStream(llpName + ".llp");
      zipper.zipTo(output);
    }
    catch(Exception e)
    {
      throw new LimelightException("Failed to pack production: " + productionPath, e);
    }
  }
}
