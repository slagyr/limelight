//- Copyright 2008 8th Light, Inc.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.io;

import limelight.util.StringUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtil
{
  public static String seperator()
  {
    return System.getProperty("file.separator");
  }

  public static String buildPath(String[] parts)
	{
		return StringUtil.join(parts, seperator());
	}

  public static String pathTo(String dir, String file)
  {
    return buildPath(new String[] {dir, file} );
  }

  public static String pathTo(String dir1, String dir2, String file)
  {
    return buildPath(new String[] {dir1, dir2, file} );
  }

  public static String pathTo(String dir1, String dir2, String dir3, String file)
  {
    return buildPath(new String[] {dir1, dir2, dir3, file} );
  }

  public static String currentPath()
  {
    return new File("").getAbsolutePath();
  }

  	public static File createFile(String path, String content)
	{
		return createFile(new File(path), content);
	}

	public static File createFile(File file, String content)
	{
		try
		{
			FileOutputStream fileOutput = new FileOutputStream(file);
			fileOutput.write(content.getBytes());
			fileOutput.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		return file;
	}

  public static File makeDir(String path)
	{
    File dir = new File(path);
    dir.mkdir();
    return dir;
  }

  public static void deleteFileSystemDirectory(String dirPath)
	{
		deleteFileSystemDirectory(new File(dirPath));
	}

	public static void deleteFileSystemDirectory(File current)
	{
		File[] files = current.listFiles();

		for(int i = 0; files != null && i < files.length; i++)
		{
			File file = files[i];
			if(file.isDirectory())
				deleteFileSystemDirectory(file);
			else
				deleteFile(file);
		}
		deleteFile(current);
	}

	public static void deleteFile(String filename)
	{
		deleteFile(new File(filename));
	}

	public static void deleteFile(File file)
	{
		if(!file.exists())
			return;
		if(!file.delete())
			throw new RuntimeException("Could not delete '" + file.getAbsoluteFile() + "'");
		waitUntilFileIsDeleted(file);
	}

  private static void waitUntilFileIsDeleted(File file)
	{
		int checks = 25;
		while(file.exists())
		{
			if(--checks <= 0)
			{
				System.out.println("Breaking out of delete wait");
				break;
			}
			try
			{
				Thread.sleep(200);
			}
			catch(InterruptedException e)
			{
			}
		}
	}

  public static String getFileContent(String path) throws Exception
	{
		File input = new File(path);
		return getFileContent(input);
	}

	public static String getFileContent(File input) throws Exception
	{
		return new String(getFileBytes(input));
	}

	public static byte[] getFileBytes(File input) throws Exception
	{
		long size = input.length();
		FileInputStream stream = new FileInputStream(input);
		byte[] bytes = new StreamReader(stream).readBytes((int)size);
		stream.close();
		return bytes;
	}
}
