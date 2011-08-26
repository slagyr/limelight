//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.io;

import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import static junit.framework.Assert.assertEquals;

public class StreamReaderTest
{
	private PipedOutputStream output;
	private StreamReader reader;
	private String readResult;
	private byte[] byteResult;
	private Thread thread;

  @Before
  public void setUp() throws Exception
	{
		output = new PipedOutputStream();
		reader = new StreamReader(new PipedInputStream(output));
	}

  @After
	public void tearDown() throws Exception
	{
		output.close();
		reader.close();
	}

	private void writeToPipe(String value) throws Exception
	{
		byte[] bytes = value.getBytes();
		output.write(bytes);
	}

  @Test
	public void readLine() throws Exception
	{
		startReading(new ReadLine());
		writeToPipe("a line\r\n");
		finishReading();
		assertEquals("a line", readResult);
	}

  @Test
  public void readLineBytes() throws Exception
	{
		startReading(new ReadLineBytes());
		writeToPipe("a line\r\n");
		finishReading();
		assertEquals("a line", new String(byteResult));
	}

  @Test
	public void bufferCanGrow() throws Exception
	{
		startReading(new ReadLine());
		for(int i = 0; i < 1001; i++)
			writeToPipe(i + ",");
		writeToPipe("\r\n");

		finishReading();
		assertEquals(true, readResult.indexOf("1000") > 0);
	}

  @Test
	public void readNumberOfBytesAsString() throws Exception
	{
		startReading(new ReadCount(100));
		StringBuffer buffer = new StringBuffer();
		for(int i = 0; i < 100; i++)
		{
			buffer.append("*");
			writeToPipe("*");
		}
		finishReading();

		assertEquals(buffer.toString(), readResult);
	}

  @Test
	public void readNumberOfBytes() throws Exception
	{
		startReading(new ReadCountBytes(100));
		StringBuffer buffer = new StringBuffer();
		for(int i = 0; i < 100; i++)
		{
			buffer.append("*");
			writeToPipe("*");
		}
		finishReading();

		assertEquals(buffer.toString(), new String(byteResult));
	}

  @Test
	public void readNumberOfBytesWithClosedInput() throws Exception
	{
		startReading(new ReadCountBytes(100));

		for(int i = 0; i < 50; i++)
			writeToPipe("*");
		output.close();
		finishReading();

		assertEquals("bytes consumed", 50, reader.numberOfBytesConsumed());
		assertEquals("bytes returned", 50, byteResult.length);
	}

  @Test
	public void readingZeroBytes() throws Exception
	{
		startReading(new ReadCount(0));
		finishReading();
		assertEquals("", readResult);
	}

  @Test
	public void readUpTo() throws Exception
	{
		checkReadUoTo("--boundary", "some bytes--boundary", "some bytes");
	}

  @Test
	public void readUpToNonEnd() throws Exception
	{
		checkReadUoTo("--bound", "some bytes--boundary", "some bytes");
	}

  @Test
	public void readBytesUpTo() throws Exception
	{
		startReading(new ReadUpToBytes("--boundary"));
		writeToPipe("some bytes--boundary");
		finishReading();

		assertEquals("some bytes", new String(byteResult));
	}

  @Test
	public void readUpTo2() throws Exception
	{
		checkReadUoTo("--bob", "----bob\r\n", "--");
	}

  @Test
	public void readUpTo3() throws Exception
	{
		checkReadUoTo("12345", "112123123412345", "1121231234");
	}

	private void checkReadUoTo(String boundary, String input, String expected) throws Exception
	{
		startReading(new ReadUpTo(boundary));
		writeToPipe(input);
		finishReading();

		assertEquals(expected, readResult);
	}

  @Test
	public void copyBytesUpTo() throws Exception
	{
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		writeToPipe("some bytes--boundary");
		reader.copyBytesUpTo("--boundary", outputStream);
		assertEquals("some bytes", outputStream.toString());
	}

  @Test
	public void eofReadCount() throws Exception
	{
		writeToPipe("abcdefghijklmnopqrstuvwxyz");
		output.close();
		assertEquals(false, reader.isEof());
		reader.read(10);
		assertEquals(false, reader.isEof());
		reader.read(16);
		assertEquals(false, reader.isEof());
		reader.read(1);
		assertEquals(true, reader.isEof());
	}

  @Test
  public void readAll() throws Exception
  {
    final String value = "This is one complete message";
    writeToPipe(value);
    output.close();

    assertEquals(value, reader.readAll());
  }

  @Test
  public void readAllWithBigContent() throws Exception
  {
    StringBuffer buffer = new StringBuffer();
    for(int i = 0; i < 1000; i++)
      buffer.append("This is a piece of a much bigger message.\n");

		startReading(new ReadAll());
    writeToPipe(buffer.toString());
    output.close();
		finishReading();

    assertEquals(buffer.toString(), readResult);
  }

  @Test
	public void eofReadLine() throws Exception
	{
		writeToPipe("one line\ntwo lines\nthree lines");
		output.close();
		assertEquals(false, reader.isEof());
		reader.readLine();
		assertEquals(false, reader.isEof());
		reader.readLine();
		assertEquals(false, reader.isEof());
		reader.readLine();
		assertEquals(true, reader.isEof());
	}

  @Test
	public void eofReadUpTo() throws Exception
	{
		writeToPipe("mark one, mark two, the end");
		output.close();
		assertEquals(false, reader.isEof());
		reader.readUpTo("one");
		assertEquals(false, reader.isEof());
		reader.readUpTo("two");
		assertEquals(false, reader.isEof());
		reader.readUpTo("three");
		assertEquals(true, reader.isEof());
	}


  @Test
	public void bytesConsumed() throws Exception
	{
		writeToPipe("One line\r\n12345abc-boundary");
		assertEquals(0, reader.numberOfBytesConsumed());

		reader.readLine();
		assertEquals(10, reader.numberOfBytesConsumed());

		reader.read(5);
		assertEquals(15, reader.numberOfBytesConsumed());

		reader.readUpTo("-boundary");
		assertEquals(27, reader.numberOfBytesConsumed());
	}

  @Test
	public void earlyClosingStream() throws Exception
	{
		startReading(new ReadCount(10));
		output.close();
		finishReading();
		assertEquals("", readResult);
	}

	private void startReading(ReadThread thread)
	{
		this.thread = thread;
		this.thread.start();
	}

	private void finishReading() throws Exception
	{
		thread.join();
	}

	abstract class ReadThread extends Thread
	{
		public void run()
		{
			try
			{
				doRead();
			}
			catch(Exception e)
			{
        Exception exception = e;
			}
		}

		public abstract void doRead() throws Exception;
	}

	class ReadLine extends ReadThread
	{
		public void doRead() throws Exception
		{
			readResult = reader.readLine();
		}
	}

	class ReadCount extends ReadThread
	{
		private final int amount;

		public ReadCount(int amount)
		{
			this.amount = amount;
		}

		public void doRead() throws Exception
		{
			readResult = reader.read(amount);
		}
	}

	class ReadAll extends ReadThread
	{
		public void doRead() throws Exception
		{
			readResult = reader.readAll();
		}
	}

	class ReadUpTo extends ReadThread
	{
		private final String boundary;

		public ReadUpTo(String b)
		{
			boundary = b;
		}

		public void doRead() throws Exception
		{
			readResult = reader.readUpTo(boundary);
		}
	}

	class ReadLineBytes extends ReadThread
	{
		public void doRead() throws Exception
		{
			byteResult = reader.readLineBytes();
		}
	}

	class ReadCountBytes extends ReadThread
	{
		private final int amount;

		public ReadCountBytes(int amount)
		{
			this.amount = amount;
		}

		public void doRead() throws Exception
		{
			byteResult = reader.readBytes(amount);
		}
	}

	class ReadUpToBytes extends ReadThread
	{
		private final String boundary;

		public ReadUpToBytes(String b)
		{
			boundary = b;
		}

		public void doRead() throws Exception
		{
			byteResult = reader.readBytesUpTo(boundary);
		}
	}

}
