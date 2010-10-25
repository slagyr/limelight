//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.io;

import limelight.Context;
import limelight.util.TestUtil;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import static org.junit.Assert.*;

public class DirectoryZipperTest
{
  private static final String ROOT_DIR = "/limelight/zipper/test/rootdir";
  private FakeFileSystem fs;

  @Before
  public void setUp() throws Exception
  {
    fs = new FakeFileSystem();
    Context.instance().fs = fs;
  }

  public void makeTestRoot()
  {
    fs.createTextFile(FileUtil.pathTo(ROOT_DIR, "root.txt"), "root");
    fs.createTextFile(FileUtil.pathTo(ROOT_DIR, "childdir", "child.txt"), "child");
    fs.createTextFile(FileUtil.pathTo(ROOT_DIR, "childdir", "grandchilddir", "grandchild.txt"), "grand child" );
  }

  @Test
  public void shouldCreateFromDirectoryPath() throws Exception
  {
    makeTestRoot();

    DirectoryZipper zipper = DirectoryZipper.fromDir(ROOT_DIR);
    assertEquals(ROOT_DIR, zipper.getDirectoryPath());
    assertEquals("rootdir", zipper.getProductionName());
  }

  @Test
  public void shouldInstantiateFromZipContent() throws Exception
  {
    makeTestRoot();
    DirectoryZipper zipper = DirectoryZipper.fromDir(ROOT_DIR);

    ByteArrayOutputStream output = new ByteArrayOutputStream();
    zipper.zipTo(output);

    ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
    DirectoryZipper newZipper = DirectoryZipper.fromZip(input);
    final String outputDir = "/limelight/zipper/test/outdir";
    fs.createDirectory(outputDir);
    newZipper.unzip(outputDir);

    assertEquals(FileUtil.join(outputDir, "rootdir") + "/", newZipper.getDirectoryPath());
    assertEquals("rootdir", newZipper.getProductionName());
    assertEquals("root", fs.readTextFile(FileUtil.pathTo(ROOT_DIR, "root.txt")));
    assertEquals("child", fs.readTextFile(FileUtil.pathTo(ROOT_DIR, "childdir", "child.txt")));
    assertEquals("grand child", fs.readTextFile(FileUtil.pathTo(ROOT_DIR, "childdir", "grandchilddir", "grandchild.txt")));
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
