//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.io;

import limelight.util.StringUtil;

import java.io.*;

public class FileUtil
{
  public static String seperator()
  {
    return System.getProperty("file.separator");
  }

  public static String buildPath(String... parts)
	{
    return removeDuplicateSeprators(StringUtil.join(seperator(), parts));
	}

  private static String removeDuplicateSeprators(String path)
  {
    return path.replace(seperator() + seperator(), seperator());
  }

  public static String buildOnPath(String base, String... parts)
  {
    return removeDuplicateSeprators(base + seperator() + buildPath(parts));
  }

  public static String pathTo(String... parts)
  {
    return buildPath(parts);
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

  public static void appendToFile(String filename, String content)
  {
   	try
		{
			FileOutputStream fileOutput = new FileOutputStream(filename, true);
			fileOutput.write(content.getBytes());
			fileOutput.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
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
        //okay
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

  public static void copyBytes(InputStream input, OutputStream output) throws Exception
  {
    BufferedInputStream bufferedInput = new BufferedInputStream(input);
    StreamReader reader = new StreamReader(bufferedInput);
    BufferedOutputStream bufferedOutput = new BufferedOutputStream(output);
    while(!reader.isEof())
      bufferedOutput.write(reader.readBytes(1000));
    bufferedOutput.flush();
  }
}
