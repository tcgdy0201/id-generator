package com.hujiang.foe.id.generator.core;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by 顾冬煜
 * Date : 2016-08-29
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:test-spring.xml" })
public class IdGeneratorTest {

    private Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    @Qualifier("idGenerator")
    private IdGenerator idGenerator;
    @Autowired
    @Qualifier("idGenerator2")
    private IdGenerator idGenerator2;
    @Autowired
    @Qualifier("idGenerator3")
    private IdGenerator idGenerator3;
    @Autowired
    @Qualifier("idGenerator4")
    private IdGenerator idGenerator4;
    @Autowired
    @Qualifier("idGenerator5")
    private IdGenerator idGenerator5;

    @Test
    public void test(){
        String uniqueId = "9999";
        int idLength = 6;
        String strUniqueId = uniqueId.length()>idLength
                ? uniqueId.substring(uniqueId.length() - idLength)
                : StringUtils.leftPad(uniqueId, idLength, "0");
        System.out.println(strUniqueId);
    }

    @Test
    public void test_单线程(){
        for(int i=0;i<10000;i++){

            String id = idGenerator.next();
            System.out.println(id +" "+id.length());
        }
    }

    @Test
    public void test_多线程生成单号() throws InterruptedException {

        Thread t1 = new Thread(()->{
            while (true) {
                log.info("threadName:{} orderNo:{}", Thread.currentThread().getName(), idGenerator.next());
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {

                }
            }
        });
        Thread t2 = new Thread(()->{
            while (true) {
                log.info("threadName:{} orderNo:{}", Thread.currentThread().getName(), idGenerator2.next());
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {

                }
            }
        });
        Thread t3 = new Thread(()->{
            while (true) {
                log.info("threadName:{} orderNo:{}", Thread.currentThread().getName(), idGenerator3.next());
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {

                }
            }
        });
        Thread t4 = new Thread(()->{
            while (true) {
                log.info("threadName:{} orderNo:{}", Thread.currentThread().getName(), idGenerator4.next());
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {

                }
            }
        });
        Thread t5 = new Thread(()->{
            while (true){
                log.info("threadName:{} orderNo:{}",Thread.currentThread().getName(),idGenerator5.next());
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {

                }
            }
        });
        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();

        Thread.currentThread().join();
    }
}
