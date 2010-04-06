//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.io;

import junit.framework.TestCase;
import limelight.util.TestUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;

public class DirectoryZipperTest extends TestCase
{
  private static final String ROOT_DIR = FileUtil.buildPath(TestUtil.TMP_DIR, "test_rootdir");

  public void setUp() throws Exception
  {
    deleteRoot();
    FileUtil.makeDir(TestUtil.TMP_DIR);
    assertEquals(true, new File(TestUtil.TMP_DIR).exists());
  }

  public void tearDown() throws Exception
  {
    deleteRoot();
  }

  private void deleteRoot()
  {
    if(new File(ROOT_DIR).exists())
      FileUtil.deleteFileSystemDirectory(ROOT_DIR);
  }

  public void makeTestRoot()
  {
    FileUtil.makeDir(ROOT_DIR);
    FileUtil.makeDir(FileUtil.pathTo(ROOT_DIR, "childdir"));
    FileUtil.makeDir(FileUtil.pathTo(ROOT_DIR, "childdir", "grandchilddir"));
    FileUtil.createFile(FileUtil.pathTo(ROOT_DIR, "root.txt"), "root");
    FileUtil.createFile(FileUtil.pathTo(ROOT_DIR, "childdir", "child.txt"), "child");
    FileUtil.createFile(FileUtil.pathTo(ROOT_DIR, "childdir", "grandchilddir", "grandchild.txt"), "grand child" );
  }
  
  public void testShouldCreateFromDirectoryPath() throws Exception
  {
    makeTestRoot();

    DirectoryZipper zipper = DirectoryZipper.fromDir(ROOT_DIR);
    assertEquals(new File(ROOT_DIR).getAbsolutePath(), zipper.getDirectoryPath());
    assertEquals("test_rootdir", zipper.getProductionName());
  }

  public void testShouldInstantiateFromZipcontent() throws Exception
  {
    makeTestRoot();
    DirectoryZipper zipper = DirectoryZipper.fromDir(ROOT_DIR);

    ByteArrayOutputStream output = new ByteArrayOutputStream();
    zipper.zipTo(output);
    deleteRoot();

    ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
    DirectoryZipper newZipper = DirectoryZipper.fromZip(input);
    newZipper.unzip(TestUtil.TMP_DIR);

    assertEquals(new File(ROOT_DIR).getAbsolutePath() + "/", newZipper.getDirectoryPath());
    assertEquals("test_rootdir", newZipper.getProductionName());
    assertEquals("root", FileUtil.getFileContent(FileUtil.pathTo(ROOT_DIR, "root.txt")));
    assertEquals("child", FileUtil.getFileContent(FileUtil.pathTo(ROOT_DIR, "childdir", "child.txt")));
    assertEquals("grand child", FileUtil.getFileContent(FileUtil.pathTo(ROOT_DIR, "childdir", "grandchilddir", "grandchild.txt")));
  }

//  public void testZipIty() throws Exception
//  {
//    makeTestRoot();
//    DirectoryZipper zipper = DirectoryZipper.fromDir(ROOT_DIR);
//
//    FileOutputStream output = new FileOutputStream("/tmp/OUCH.zip");
//    zipper.zipTo(output);
//  }
}
