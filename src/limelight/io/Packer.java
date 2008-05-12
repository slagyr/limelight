package limelight.io;

import limelight.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

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

  public void pack(String productionPath) throws Exception
  {
    DirectoryZipper zipper = DirectoryZipper.fromDir(productionPath);
    String productionName = zipper.getProductionName();
    FileOutputStream output = new FileOutputStream(productionName + ".llp");
    zipper.zipTo(output);
  }
}
