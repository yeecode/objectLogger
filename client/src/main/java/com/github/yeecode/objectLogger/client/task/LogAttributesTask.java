package com.github.yeecode.objectLogger.client.task;

import com.alibaba.fastjson.JSON;
import com.github.yeecode.objectLogger.client.config.ObjectLoggerConfig;
import com.github.yeecode.objectLogger.client.http.HttpUtil;
import com.github.yeecode.objectLogger.client.model.OperationModel;
import com.github.yeecode.objectLogger.client.model.BaseAttributeModel;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

public class LogAttributesTask implements Runnable {
    private HttpUtil httpUtil;
    private String objectName;
    private Integer objectId;
    private String operator;
    private String operationName;
    private String operationAlias;
    private String extraWords;
    private String comment;
    private ObjectLoggerConfig objectLoggerConfig;

    private List<BaseAttributeModel> baseAttributeModelList;

    public LogAttributesTask(String objectName, Integer objectId, String operator, String operationName, String operationAlias,
                             String extraWords, String comment,
                             List<BaseAttributeModel> baseAttributeModelList, ObjectLoggerConfig objectLoggerConfig, HttpUtil httpUtil) {
        this.objectName = objectName;
        this.objectId = objectId;
        this.operator = operator;
        this.operationName = operationName;
        this.operationAlias = operationAlias;
        this.extraWords = extraWords;
        this.comment = comment;
        this.baseAttributeModelList = baseAttributeModelList;
        this.objectLoggerConfig = objectLoggerConfig;
        this.httpUtil = httpUtil;
    }

    @Override
    public void run() {
        try {
            OperationModel operationModel = new OperationModel(objectLoggerConfig.getBusinessAppName(), objectName, objectId, operator,
                    operationName, operationAlias, extraWords, comment, new Date());

            if (!CollectionUtils.isEmpty(baseAttributeModelList)) {
                operationModel.addBaseActionItemModelList(baseAttributeModelList);
            }
            httpUtil.sendLog(JSON.toJSONString(operationModel));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}