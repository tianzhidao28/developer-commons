/**
 *
 * Copyright (c) 2016, rocyuan, admin@rocyuan.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.rocyuan.commons.utils;

import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.springframework.test.util.AssertionErrors.assertTrue;

/**
 * JUnit推荐的github上的一个并发测试工具类
 * Created by rocyuan on 15/12/29.
 */
public class ConcurrentTestUtils {


    /**
     * [任务并发测试]
     * @param jobMsg 任务名
     * @param jobLists 任务列表
     * @param maxTimeout 最大等大时间
     * @param maxThreadPoolSize
     */
    public static void assertConcurrent(String jobMsg, final List<Runnable> jobLists, final long maxTimeout, final int maxThreadPoolSize) throws InterruptedException {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start(jobMsg);

        final int jobNums = jobLists.size();
        final List<Throwable> exceptions = Collections.synchronizedList(new ArrayList<Throwable>());

        final ExecutorService threadPool = Executors
                .newFixedThreadPool(jobNums > maxThreadPoolSize ? maxThreadPoolSize : jobNums);


        final CountDownLatch afterInitBlocker = new CountDownLatch(1);
        final CountDownLatch allDone = new CountDownLatch(jobNums);

        try {
            for (final Runnable submittedTestRunnable : jobLists) {
                threadPool.submit(new Runnable() {
                    public void run() {
                        try {
                            System.out.println("Thread:"+Thread.currentThread().getName()+" : init ..." );
                            afterInitBlocker.await();   // 相当于加了个阀门等待所有任务都准备就绪
                            System.out.println("Thread:"+Thread.currentThread().getName()+" : start ..." );
                            submittedTestRunnable.run();
                            System.out.println("Thread:"+Thread.currentThread().getName()+" : end ..." );
                        } catch (final Throwable e) {
                            exceptions.add(e);
                        } finally {
                            allDone.countDown();
                        }
                    }
                });
            }


            System.out.println("要开始并发执行了...");
            afterInitBlocker.countDown();
            System.out.println("并发执行已经开始了...");

            assertTrue(jobMsg + " timeout! More than" + maxTimeout + "seconds",
                    allDone.await(maxTimeout, TimeUnit.SECONDS));



        }catch (Exception e) {

        } finally {
            threadPool.shutdownNow();
        }



        stopWatch.stop();
        System.out.println(stopWatch.prettyPrint());


    }

    public static void main(String[] args) throws InterruptedException {


        List<Runnable> tasks = new ArrayList<Runnable> (10000);
        for(int i = 0; i < 10000; i++) {
            tasks.add(new Runnable() {

                @Override
                public void run() {
                    try {
                        Thread.sleep(20);
                    }
                    catch(InterruptedException _) {

                    }
                }

            });
        }

        ConcurrentTestUtils.assertConcurrent("1024tasks", tasks, 10, 1000);


    }
}
