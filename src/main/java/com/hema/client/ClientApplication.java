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


        SampleResult results = new SampleResult();
        results.setSampleLabel("测试！");

//        ClientHandler handler = new ClientHandler();
//
//        ClientDemo clientDemo = new ClientDemo(handler,host,Integer.valueOf(port));
        WsClient wsClient = new WsClient(url,"3539315614388224","3");
        Thread t1 = new Thread(wsClient);
        t1.start();
        results.setSuccessful(true);
        results.setDataType(SampleResult.TEXT);
        return results;
    }

    public static void main(String[] args) {
        WsClient wsClient = new WsClient("ws://192.168.10.211:8500/hugh_txp/websocket","3881647704887296",
                "111111");
        Thread t1 = new Thread(wsClient);
        t1.start();
    }
}
