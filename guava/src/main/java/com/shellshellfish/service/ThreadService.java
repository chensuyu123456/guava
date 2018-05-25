package com.shellshellfish.service;

import com.google.common.util.concurrent.AbstractExecutionThreadService;
import java.util.List;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class ThreadService extends AbstractExecutionThreadService {

  private CountDownLatch countDownLatch;

  public ThreadService(CountDownLatch countDownLatch) {
    this.countDownLatch = countDownLatch;
  }


  @Override
  protected void run() throws Exception {
    System.out.println("run");
    Thread.sleep(2000);
    throw new Exception("111111");
    //Thread.sleep(5000);
    //countDownLatch.countDown();
  }



}
