//package com.hema.client;
//
//import io.netty.buffer.ByteBuf;
//import io.netty.buffer.Unpooled;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//
//
///**
// * 存储TCP链接
// *
// * @author admin
// */
//@Component
//public class GatewayService {
//    private static final Logger log = LoggerFactory.getLogger(GatewayService.class);
//
//    /**
//     * 获取字符串
//     *
//     * @param buf
//     * @return
//     */
//    public static String getMessage(ByteBuf buf) {
//        byte[] con = new byte[buf.readableBytes()];
//        buf.readBytes(con);
//        return HexStringUtil.bytesToHexString(con);
//    }
//
//    /**
//     * 组装bytebuf
//     *
//     * @param resp
//     * @return
//     */
//    public static ByteBuf getSendByteBuf(byte[] resp) {
//        ByteBuf pingMessage = Unpooled.buffer();
//        pingMessage.writeBytes(resp);
//        return pingMessage;
//    }
//
//
//}
