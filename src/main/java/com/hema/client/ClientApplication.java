package com.hema.client;

import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

/**
 * @author sunjx
 * @Date 2019/2/18 13:50
 */
public class ClientApplication extends AbstractJavaSamplerClient {

    @Override
    public SampleResult runTest(JavaSamplerContext javaSamplerContext) {
        SampleResult results = new SampleResult();
        results.setSampleLabel("测试！");

        ClientHandler handler = new ClientHandler();
        ClientDemo clientDemo = new ClientDemo(handler);
        Thread t1 = new Thread(clientDemo);
        t1.start();
        results.setSuccessful(true);
        results.setDataType(SampleResult.TEXT);
        return results;
    }
}
