package com.hema.client;

import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.threads.JMeterVariables;

/**
 * @author sunjx
 * @Date 2019/2/18 13:50
 */
public class ClientApplication extends AbstractJavaSamplerClient {

    @Override
    public SampleResult runTest(JavaSamplerContext javaSamplerContext) {
        JMeterVariables jMeterVariables = javaSamplerContext.getJMeterVariables();
        String url = jMeterVariables.get("url");
        String processId = jMeterVariables.get("processId");
        String tenantId = jMeterVariables.get("tenantId");
        String userId = jMeterVariables.get("userId");
        String orgId = jMeterVariables.get("orgId");

        SampleResult results = new SampleResult();
        results.setSampleLabel("测试！");

        WsClient wsClient = new WsClient(url,processId,tenantId,userId,orgId);
        Thread t1 = new Thread(wsClient);
        t1.start();

        results.setSuccessful(true);
        results.setDataType(SampleResult.TEXT);
        return results;
    }

    public static void main(String[] args) {
        WsClient wsClient = new WsClient("ws://192.168.10.211:8500/hugh_txp/websocket","3881647704887296",
                "111111","111111","123456");
        Thread t1 = new Thread(wsClient);
        t1.start();
    }
}
