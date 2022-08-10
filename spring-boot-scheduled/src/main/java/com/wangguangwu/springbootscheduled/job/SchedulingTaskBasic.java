package com.wangguangwu.springbootscheduled.job;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author wangguangwu
 */
@Component
public class SchedulingTaskBasic {

    @Scheduled(cron = "*/5 * * * * ?")
    private void printNow() {
        System.out.println("此定时任务每 5s 执行一次，当前时间：[" + System.currentTimeMillis() + "]");
    }

}
