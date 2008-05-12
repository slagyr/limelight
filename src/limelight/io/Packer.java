package limelight.io;

import limelight.Context;

import java.io.File;
import java.io.FileInputStream;

public class Packer
{
  public String unpack(String packagePath) throws Exception
  {
    FileInputStream input = new FileInputStream(packagePath);
    DirectoryZipper zipper = DirectoryZipper.fromZip(input);
    File destination = Context.instance().tempDirectory.createNewDirectory();
    zipper.unzip(destination.getAbsolutePath());
    return zipper.getDirectoryPath();
  }

  public void pack(String productionPath)
  {

  }
}
