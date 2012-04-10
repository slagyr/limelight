package limelight.util;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class NullLock implements Lock
{
  public static Lock instance = new NullLock();

  public void lock()
  {
  }

  public void lockInterruptibly() throws InterruptedException
  {
  }

  public boolean tryLock()
  {
    return true;
  }

  public boolean tryLock(long l, TimeUnit timeUnit) throws InterruptedException
  {
    return true;
  }

  public void unlock()
  {
  }

  public Condition newCondition()
  {
    return null;
  }
}
