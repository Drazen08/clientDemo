package com.hema.client;

import com.alibaba.fastjson.JSONObject;
import org.springframework.messaging.simp.stomp.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class MyStompSessionHandler extends StompSessionHandlerAdapter implements StompSessionHandler {

	private String processId;
	private String XAUTHUSER;
	private String XAUTHTENANT;

	public MyStompSessionHandler(String process,String tenant) {
		this.processId = process;
		this.XAUTHTENANT = tenant;
		this.XAUTHUSER = tenant;
	}

	private StompSession stompSession;

	@Override
	public void afterConnected(StompSession stompSession, StompHeaders stompHeaders) {
		System.out.println("hihihihihih");
		this.stompSession = stompSession;
		this.firstSend();
	}

	@Override
	public void handleException(StompSession stompSession, StompCommand stompCommand, StompHeaders stompHeaders, byte[] bytes, Throwable throwable) {
		System.err.println(throwable.toString());
	}

	@Override
	public void handleTransportError(StompSession stompSession, Throwable throwable) {
		System.err.println(throwable.toString());
	}

	@Override
	public Type getPayloadType(StompHeaders stompHeaders) {
		return List.class;
	}

	@Override
	public void handleFrame(StompHeaders stompHeaders, Object o) {
		ArrayList<Map> result = (ArrayList<Map>) o;
		for (Map map : result) {
			JSONObject jsonObject = new JSONObject(map);
			System.out.println("get response ----------------");
			System.out.println(jsonObject.toJSONString());
		}
	}

	private void firstSend(){
		if(stompSession != null){
			WsItem item = getItem();

			stompSession.subscribe("/user/queue/point", this);
			stompSession.send("/msg/trade", item);
			System.out.println("send ------------");
			System.out.println(item);
		}
	}

	private WsItem getItem(){
		WsItem item = new WsItem();
		item.setProcessId(this.processId);
		item.setXAUTHTENANT(XAUTHTENANT);
		item.setXAUTHUSER(XAUTHUSER);
		item.setOrgIds(Arrays.asList("123456"));
		return item;
	}



}
