//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight;

public class Blah
{
//  static String src = "" +
//      "begin\n" +
//      " Java::java.lang.Thread.currentThread.handle = '_HERE_'\n" +
//      "rescue Exception => e\n" +
//      " puts e\n" +
//      " puts e.backtrace\n" +
//      "end";

  //
  public static void main(String[] args) throws InterruptedException
  {
//    new Main().configureContext();
//    Object one = Context.instance.runtimeFactory.spawn(src.replace("_HERE_", "ONE"));
//    Object two = Context.instance.runtimeFactory.spawn(src.replace("_HERE_", "TWO"));
//    Object three = Context.instance.runtimeFactory.spawn(src.replace("_HERE_", "THREE"));
//
//    Thread.sleep(1000 * 10);
//    Context.instance.runtimeFactory.terminate(one);
//    Thread.sleep(1000 * 10);
//    Context.instance.runtimeFactory.terminate(two);
//    Thread.sleep(1000 * 10);
//    Context.instance.runtimeFactory.terminate(three);
//    Thread.sleep(1000 * 10);

    Object lock = new Object();

    synchronized(lock)
    {
      for(int i = 0; i < 1000; i++)
      {
        System.err.println("sleeping " + i);
        lock.wait(1000);
        System.err.println("awake " + i);
      }
    }

  }
}
