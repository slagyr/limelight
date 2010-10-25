//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.io;

import limelight.util.TestUtil;
import org.junit.Test;

import java.io.File;

import static junit.framework.Assert.assertEquals;

public class FileUtilTest
{
  @Test
  public void buildPathEmpty() throws Exception
	{
		assertEquals("", FileUtil.join());
	}

  @Test
	public void buildPathOneElement() throws Exception
	{
		assertEquals("a", FileUtil.join("a"));
	}

  @Test
	public void buildPathThreeElements() throws Exception
	{
		String separator = System.getProperty("file.separator");
		assertEquals("a" + separator + "b" + separator + "c", FileUtil.join("a", "b", "c"));
	}

  @Test
  public void baseName() throws Exception
  {
    assertEquals("foo", FileUtil.baseName("foo"));
    assertEquals("foo", FileUtil.baseName("bar/foo"));
    assertEquals("foo", FileUtil.baseName("bar/foo.txt"));  
  }

  @Test
  public void filename() throws Exception
  {
    assertEquals("/", FileUtil.filename("/"));
    assertEquals("C:\\", FileUtil.filename("C:\\"));
    assertEquals("one", FileUtil.filename("/one"));
    assertEquals("two", FileUtil.filename("/one/two"));
    assertEquals("two", FileUtil.filename("one/two"));
    assertEquals("two.txt", FileUtil.filename("one/two.txt"));
    assertEquals("two", FileUtil.filename("one/two/"));
  }
}
