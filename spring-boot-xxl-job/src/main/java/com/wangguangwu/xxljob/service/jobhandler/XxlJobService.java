package com.wangguangwu.xxljob.service.jobhandler;

import com.wangguangwu.xxljob.anno.XxlJobJsonParam;
import com.wangguangwu.xxljob.entity.Param;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author wangguangwu
 */
@Component
public class XxlJobService {

    private final Logger logger = LoggerFactory.getLogger(XxlJobService.class);

    /**
     * 1、简单任务示例（Bean模式）
     */
    @XxlJob("demoJobHandler")
    public ReturnT<String> demoJobHandler() {
        logger.info("## custom job handler run. ##");
        XxlJobHelper.log("XXL-JOB, Hello World.");
        return ReturnT.SUCCESS;
    }

    /**
     * 2.1 获取参数 1
     */
    @XxlJob("paramJobHandler1")
    public ReturnT<String> paramJobHandler1() {
        String jobParam = XxlJobHelper.getJobParam();
        String[] params = StringUtils.isBlank(jobParam) ? new String[]{} : jobParam.split(",");
        for (String param : params) {
            XxlJobHelper.log("param: " + param);
        }
        return ReturnT.SUCCESS;
    }

    /**
     * 2.2 获取参数 2
     */
    @XxlJobJsonParam
    @XxlJob("paramJobHandler2")
    public ReturnT<String> paramJobHandler2(Param param) {
        XxlJobHelper.log("name: " + param.getName() + ", age:" + param.getAge());
        return ReturnT.SUCCESS;
    }
}
