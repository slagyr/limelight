package limelight.io;

import limelight.LimelightException;

import java.io.*;

public class IoUtil
{
  public static void copyBytes(InputStream input, OutputStream output)
  {
    try
    {
      BufferedInputStream bufferedInput = new BufferedInputStream(input);
      StreamReader reader = new StreamReader(bufferedInput);
      BufferedOutputStream bufferedOutput = new BufferedOutputStream(output);
      while(!reader.isEof())
        bufferedOutput.write(reader.readBytes(1000));
      bufferedOutput.flush();
    }
    catch(IOException e)
    {
      throw new LimelightException(e);
    }
  }
}
