//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.util;

public class Threads
{
  public static void showAll()
  {
    ThreadGroup root = Thread.currentThread().getThreadGroup().getParent();
    while(root.getParent() != null)
    {
      root = root.getParent();
    }

    visit(root, 0);
  }

  public static void visit(ThreadGroup group, int level)
  {
    int numThreads = group.activeCount();
    Thread[] threads = new Thread[numThreads * 2];
    numThreads = group.enumerate(threads, false);

    String prefix = "";
    for(int i = 0; i < level; i++)
      prefix = prefix + "\t";

    for(int i = 0; i < numThreads; i++)
    {
      Thread thread = threads[i];
      System.err.println(prefix + thread + " " + thread.getState());
    }

    int numGroups = group.activeGroupCount();
    ThreadGroup[] groups = new ThreadGroup[numGroups * 2];
    numGroups = group.enumerate(groups, false);

    for(int i = 0; i < numGroups; i++)
    {
      visit(groups[i], level + 1);
    }
  }
}
