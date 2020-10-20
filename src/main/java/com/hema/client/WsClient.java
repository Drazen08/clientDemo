package com.hema.client;

import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.util.Scanner;


public class WsClient implements Runnable{
	public final static  String [] per =  {
			"PERMISSION_COMMON",
			"PERMISSION_WORKSHEET",
			"PERMISSION_DASHBOARD",
			"PERMISSION_DATASOURCE",
			"PERMISSION_FILTER",
			"PERMISSION_PROCESS",
			"PERMISSION_RULE_MANAGE",
			"PERMISSION_POINT",
			"PERMISSION_COMPONENT",
			"PERMISSION_COMPONENT_TYPE",
			"PERMISSION_RULE_ALLOT",
			"NO_DIFFERENT_DATA_PERMISSION",
			"PERMISSION_DASHBOARD_MANAGE",
			"PERMISSION_DASHBOARD_PREVIEW",
			"PERMISSION_DASHBOARD_FILTER",
			"PERMISSION_BIG_SCREEN_MANAGE",
			"PERMISSION_BIG_SCREEN_PREVIEW",
			"PERMISSION_PROCESS_MANAGE",
			"PERMISSION_PROCESS_PREVIEW",
			"PERMISSION_REPORT_MANAGE",
			"PERMISSION_REPORT_PREVIEW",
			"PERMISSION_WORKSHEET_MANAGE",
			"PERMISSION_WORKSHEET_PREVIEW",
			"PERMISSION_ASSETS_MANAGE",
			"PERMISSION_ASSETS_PREVIEW",
			"PERMISSION_DEVICE_MANAGE",
			"PERMISSION_DEVICE_PREVIEW",
			"PERMISSION_DL_MANAGE",
			"PERMISSION_DL_PREVIEW",
			"PERMISSION_PLATFORM_MANAGER",
			"PERMISSION_RULES_ENGINE",
			"PERMISSION_COMPONENT_MANAGE"

	};
	private String processId;
	private String XAUTHUSER;
	private String XAUTHTENANT;
	private String orgId;

	private WebSocketClient client ;
	private WebSocketStompClient stompClient ;
	private StompSessionHandler stompSessionHandler;
	private String url ;
	// ws://platform.gradiot.com/hugh_txp/292/esugcpmg/websocket

	// "SUBSCRIBE↵ack:auto↵id:sub-0↵destination:/user/queue/point↵↵

	/**
	 * 0: "SEND↵destination:/msg/trade↵content-length:1012↵↵{"processId":"3539313546121216","X-AUTH-USER":"MzEwMTYwODU4NDIxNzY4OA==","X-AUTH-TENANT":"Mw==","permissions":["PERMISSION_COMMON","PERMISSION_WORKSHEET","PERMISSION_DASHBOARD","PERMISSION_DATASOURCE","PERMISSION_FILTER","PERMISSION_PROCESS","PERMISSION_RULE_MANAGE","PERMISSION_POINT","PERMISSION_COMPONENT","PERMISSION_COMPONENT_TYPE","PERMISSION_RULE_ALLOT","NO_DIFFERENT_DATA_PERMISSION","PERMISSION_DASHBOARD_MANAGE","PERMISSION_DASHBOARD_PREVIEW","PERMISSION_DASHBOARD_FILTER","PERMISSION_BIG_SCREEN_MANAGE","PERMISSION_BIG_SCREEN_PREVIEW","PERMISSION_PROCESS_MANAGE","PERMISSION_PROCESS_PREVIEW","PERMISSION_REPORT_MANAGE","PERMISSION_REPORT_PREVIEW","PERMISSION_WORKSHEET_MANAGE","PERMISSION_WORKSHEET_PREVIEW","PERMISSION_ASSETS_MANAGE","PERMISSION_ASSETS_PREVIEW","PERMISSION_DEVICE_MANAGE","PERMISSION_DEVICE_PREVIEW","PERMISSION_DL_MANAGE","PERMISSION_DL_PREVIEW","PERMISSION_PLATFORM_MANAGER","PERMISSION_RULES_ENGINE","PERMISSION_COMPONENT_MANAGE"],"groupIds":[],"orgIds":["3"],"isSuperAdmin":true}"
	 */
	public WsClient(String url,String processId,String tenant,String userId,String orgId) {
		this.url = url;
		this.processId = processId;

		final String base64Tenant = ByteArrUtil.getBase64(tenant);
		final String base64User = ByteArrUtil.getBase64(userId);
		this.XAUTHTENANT = base64Tenant;
		this.XAUTHUSER = base64User;

		this.orgId = orgId;
	}


	@Override
	public void run() {

		this.client = new StandardWebSocketClient();
		this.stompClient = new WebSocketStompClient(client);
		this.stompClient.setMessageConverter(new MappingJackson2MessageConverter());
//		this.stompClient.setMessageConverter(new SimpleMessageConverter());
		this.stompSessionHandler = new MyStompSessionHandler(this.processId,this.XAUTHTENANT,XAUTHUSER,this.orgId);
		this.stompClient.connect(url, stompSessionHandler);
		new Scanner(System.in).nextLine(); // Don't close immediately.
	}
}
