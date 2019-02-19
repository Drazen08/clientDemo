package com.hema.client;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.nio.ByteBuffer;
import java.util.UUID;

/**
 * @author sunjx
 */
@ChannelHandler.Sharable
public class ClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private static final Logger log = LoggerFactory.getLogger(ClientHandler.class);

    private String uuid;

    /**
     */
    public ClientHandler() {
        this.uuid = ClientDemo.uuid;
    }


    private static boolean active = false;

    private static ChannelHandlerContext context;

    // 机器码
    private static Integer machineCode = 0;

    // 机器码
    private static Integer heartBeat = 1;

    // 状态下标
    private static Integer machineIndex = 0;

    private static Integer cupA = 0;
    private static Integer cupB = 0;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("client channelActive..");
        startActive(ctx);
        context = ctx;
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        ClientDemo.doConnect();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Long a = System.currentTimeMillis();
        ByteBuf buf = (ByteBuf) msg;

        byte[] result = new byte[buf.readableBytes()];
        buf.readBytes(result);
        System.out.println("length = " + result.length);
        log.info("收到命令，帧头为:[" + Integer.toHexString((int) result[0]) + "]");
        // 进行crc8 校验
        ByteBuffer byteBuffer = ByteBuffer.allocate(result.length - 1);
        byteBuffer.put(result, 0, result.length - 1);

        if (CRC8.calcCrc8(byteBuffer.array()) != result[result.length - 1]) {
            log.info("CRC 校验失败:[" + ctx.channel().id().asLongText() + "]");
            return;
        } else {
            log.info("CRC 校验成功");
        }

        switch (result[0]) {
            case (byte) 0x0011:
                // 初次连接应答
                startActiveHandler(ctx, result);
                break;
            case (byte) 0x0012:
                // 心跳应答
                heartBeatHandler(ctx, result);
                break;
            case (byte) 0x0013:
                // 收到制作指令
                doorderHandler(ctx, result);
                break;
            case (byte) 0x0015:
                makeConfirmHandler(ctx, result);
                break;
            default:
                break;
        }
        Long b = System.currentTimeMillis();
        System.out.println("TIME :[" + (b - a) + "]");
        buf.release();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        System.out.println("client channelRead..");
        ByteBuf buf = msg;
        System.out.println("Client received:" + ByteBufUtil.hexDump(buf) + "; The value is:" + GatewayService.getMessage(msg));
        //ctx.channel().close().sync();// client关闭channel连接
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        System.out.println("error!!!");
//        ctx.close();
    }

    /**
     * 初次链接
     *
     * @param ctx
     */
    private void startActive(ChannelHandlerContext ctx) {
        if (StringUtils.isEmpty(uuid)) {
            uuid = UUID.randomUUID().toString().replace("-", "");
        }
        double lat = 36.682784;
        double lon = 117.024967;
        byte[] latb = ByteArrUtil.double2Bytes(lat);
        byte[] lonb = ByteArrUtil.double2Bytes(lon);

        byte[] bf = uuid.getBytes();
        byte[] b = new byte[59];
        byte[] geoHash = "wwe0w3".getBytes();
        b[0] = (byte) 0x0011;
        // 放置uuid
        System.arraycopy(bf, 0, b, 1, 32);
        // 放置geohash
        System.arraycopy(geoHash, 0, b, 33, 6);

        b[39] = ByteArrUtil.intToByteArray1(1);

        System.arraycopy(lonb, 0, b, 40, 8);
        System.arraycopy(latb, 0, b, 48, 8);

        ByteBuffer byteBuffer = ByteBuffer.allocate(b.length - 3);
        byteBuffer.put(b, 0, b.length - 3);

        b[56] = CRC8.calcCrc8(byteBuffer.array());
        b[57] = (byte) 0x00FF;
        b[58] = (byte) 0x00FF;

        ByteBuf pingMessage = Unpooled.buffer();
        pingMessage.writeBytes(b);
        ctx.writeAndFlush(pingMessage);
    }

    /**
     * 初次连接的应答
     *
     * @param ctx    ChannelHandlerContext
     * @param result result
     */
    private void startActiveHandler(ChannelHandlerContext ctx, byte[] result) {
        byte[] code = new byte[2];
        byte[] index = new byte[2];
        System.arraycopy(result, 1, code, 0, 2);
        System.arraycopy(result, 3, index, 0, 2);
        machineCode = ByteArrUtil.byteArrayToInt2pri(code);
        machineIndex = ByteArrUtil.byteArrayToInt2pri(index);
        active = true;
    }

    /**
     * 确认制作
     *
     * @param ctx
     * @param result
     */
    private void makeConfirmHandler(ChannelHandlerContext ctx, byte[] result) {
        int num = ByteArrUtil.byteToInt(result[1]);
        byte[] state = new byte[num];
        for (int i = 0; i < num; i++) {
            state[i] = (byte) 0x0006;
        }
        ByteBuffer bf = ByteBuffer.allocate(5 + 3 * num);
        bf.put((byte) 0x0016);
        bf.put(result[1]);
        bf.put(result, 3, 2 * num);
        bf.put(state);

        byte[] temp = new byte[2 + 3 * num];
        System.arraycopy(bf.array(), 0, temp, 0, 2 + 3 * num);
        bf.put(CRC8.calcCrc8(temp));
        bf.put((byte) 0x00FF);
        bf.put((byte) 0x00FF);

        ByteBuf pingMessage = Unpooled.buffer();
        pingMessage.writeBytes(bf.array());
        ctx.writeAndFlush(pingMessage);
        System.out.println("更新杯子状态！！！！！！！6");
    }

    /**
     * 获取会话
     *
     * @return
     */
    public ChannelHandlerContext getContext() {
        return context;
    }

    /**
     * 心跳包方法
     *
     * @param ctx
     */
    private void heartBeatMethod(ChannelHandlerContext ctx) {
        int i = 2;

        byte[] bytes = new byte[14];
        bytes[0] = (byte) 0x0012;

        // 状态下表
        byte[] statebyte = ByteArrUtil.intToByteArray2(machineIndex);
        System.arraycopy(statebyte, 0, bytes, 1, 2);
//        changeCupNum();
        byte[] cupaBytes = ByteArrUtil.intToByteArray2(0);
        byte[] cupbBytes = ByteArrUtil.intToByteArray2(0);
        // 机器状态
        bytes[3] = ByteArrUtil.intToByteArray1(i);
        bytes[4] = (byte) 0x0000;
        bytes[5] = (byte) 0x0000;
        bytes[6] = (byte) 0x0000;
        // ---------------------
        System.arraycopy(cupaBytes, 0, bytes, 7, 2);
        System.arraycopy(cupbBytes, 0, bytes, 9, 2);
        // ----------------------

        ByteBuffer byteBuffer = ByteBuffer.allocate(bytes.length - 3);
        byteBuffer.put(bytes, 0, bytes.length - 3);

        bytes[11] = CRC8.calcCrc8(byteBuffer.array());

        bytes[12] = (byte) 0x00FF;
        bytes[13] = (byte) 0x00FF;

        ByteBuf pingMessage = Unpooled.buffer();
        pingMessage.writeBytes(bytes);
        ctx.writeAndFlush(pingMessage);
        heartBeat = i;
    }

    /**
     * 模拟杯数变化的方法
     */
    private void changeCupNum() {
        if (cupA < 10) {
            cupA++;
        } else {
            cupA = 0;
        }

        if (cupB < 15) {
            cupB += 2;
        } else {
            cupB = 0;
        }

    }

    /**
     * 制作
     *
     * @param ctx
     * @param result
     */
    private void doorderHandler(ChannelHandlerContext ctx, byte[] result) throws InterruptedException {
        System.out.println(HexStringUtil.bytesToHexString(result));
        int num = 2;
        byte[] state = new byte[num];
        for (int i = 0; i < num; i++) {
            state[i] = (byte) 0x0002;
        }
        byte[] indexs = new byte[4];
        indexs[0] = (byte) 0x00;
        indexs[1] = (byte) 0x00;
        indexs[2] = (byte) 0x00;
        indexs[3] = (byte) 0x01;
        ByteBuffer bf = ByteBuffer.allocate(7 + 3 * num);
        bf.put((byte) 0x0016);
        bf.put(ByteArrUtil.intToByteArray1(2));
        bf.put(indexs);
        bf.put(state);
        bf.put(ByteArrUtil.intToByteArray2(1));

        byte[] temp = new byte[4 + 3 * num];
        System.arraycopy(bf.array(), 0, temp, 0, temp.length);
        bf.put(CRC8.calcCrc8(temp));
        bf.put((byte) 0x00FF);
        bf.put((byte) 0x00FF);

        ByteBuf pingMessage = Unpooled.buffer();
        pingMessage.writeBytes(bf.array());
        ctx.writeAndFlush(pingMessage);
        System.out.println("更新杯子状态！！！！！！！[" + HexStringUtil.bytesToHexString(bf.array()) + "]");
    }


    /**
     * 心跳包应答
     *
     * @param ctx
     * @param result
     */
    private void heartBeatHandler(ChannelHandlerContext ctx, byte[] result) {
        log.info("收到心跳包应答");
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

        if (!active) {
            Thread.sleep(2000);
            startActive(ctx);
            return;
        }
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            switch (e.state()) {
                case WRITER_IDLE:
                    heartBeatMethod(ctx);
                    System.out.println("send ping to server----------");
                    System.out.println("uuid:[" + uuid + "]");
                    break;
                case READER_IDLE:
                    break;
                case ALL_IDLE:
                    break;
                default:
                    break;
            }
        }
    }
}
