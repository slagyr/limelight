//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.io;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Random;

import static org.junit.Assert.*;

public class DirectoryZipperTest
{
  private static final String ROOT_DIR = "/limelight/zipper/test/rootdir";
  private FakeFileSystem fs;

  @Before
  public void setUp() throws Exception
  {
    fs = FakeFileSystem.installed();
  }

  public void makeTestRoot()
  {
    fs.createTextFile(fs.join(ROOT_DIR, "root.txt"), "root");
    fs.createTextFile(fs.join(ROOT_DIR, "childdir", "child.txt"), "child");
    fs.createTextFile(fs.join(ROOT_DIR, "childdir", "grandchilddir", "grandchild.txt"), "grand child" );
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

    assertEquals(fs.join(outputDir, "rootdir") + "/", newZipper.getDirectoryPath());
    assertEquals("rootdir", newZipper.getProductionName());
    assertEquals("root", fs.readTextFile(fs.join(ROOT_DIR, "root.txt")));
    assertEquals("child", fs.readTextFile(fs.join(ROOT_DIR, "childdir", "child.txt")));
    assertEquals("grand child", fs.readTextFile(fs.join(ROOT_DIR, "childdir", "grandchilddir", "grandchild.txt")));
  }
  
  @Test
  public void zipAndUnzipWithRealFS() throws Exception
  {
    FileSystem fs = FileSystem.installed();
    final String tmp = fs.join(System.getProperty("java.io.tmpdir"), new Random().nextInt(10000000) + "");
    String source = fs.join(tmp, "zipSource");
    fs.createTextFile(fs.join(source, "one.txt"), "one");
    fs.createTextFile(fs.join(source, "two", "two.txt"), "two");
    fs.createTextFile(fs.join(source, "one", "two", "three.txt"), "three");
    final DirectoryZipper zipper = DirectoryZipper.fromDir(source);
    zipper.zipTo(new FileOutputStream(fs.join(tmp, "zip.zip")));

    final DirectoryZipper unzipper = DirectoryZipper.fromZip(new FileInputStream(fs.join(tmp, "zip.zip")));
    String dest = fs.join(tmp, "zipDest");
    unzipper.unzip(dest);
    assertEquals("one", fs.readTextFile(fs.join(dest, "one.txt")));
    assertEquals("two", fs.readTextFile(fs.join(dest, "two", "two.txt")));
    assertEquals("three", fs.readTextFile(fs.join(dest, "one", "two", "three.txt")));
  }

//  public void testZipIty() throws Exception
//  {
//    makeTestRoot();
//    DirectoryZipper zipper = DirectoryZipper.fromDir(ROOT_DIR);
//
//    FileOutputStream output = new FileOutputStream("/tmp/OUCH.zip");
//    zipper.zipTo(output);
//  }
  
//  @Test
//  public void realUnzip() throws Exception
//  {
//    FileSystem.installed();
//    final DirectoryZipper zipper = DirectoryZipper.fromZip(new FileInputStream("C:\\Projects\\hosemonster\\ui\\production.llp"));
//    zipper.unzip("C:\\Users\\8thlight\\tmp");
//
//  }
}
