//package com.hema.client;
//
//import io.netty.bootstrap.Bootstrap;
//import io.netty.buffer.ByteBuf;
//import io.netty.buffer.Unpooled;
//import io.netty.channel.ChannelFuture;
//import io.netty.channel.ChannelFutureListener;
//import io.netty.channel.ChannelInitializer;
//import io.netty.channel.ChannelOption;
//import io.netty.channel.nio.NioEventLoopGroup;
//import io.netty.channel.socket.SocketChannel;
//import io.netty.channel.socket.nio.NioSocketChannel;
//import io.netty.handler.codec.DelimiterBasedFrameDecoder;
//import io.netty.handler.timeout.IdleStateHandler;
//
//import java.net.InetSocketAddress;
//import java.util.concurrent.TimeUnit;
//
///**
// * @author sunjx
// */
//public class ClientDemo implements Runnable {
//
//    private  static String url = "192.168.20.40";
//    private  static int port = 9001;
//
//    static String uuid = "";
//
//    private ClientHandler handler;
//
//    private static boolean isConnect = false;
//
//    ClientDemo(ClientHandler handler,String host,int nport) {
//        url = host;
//        port = nport;
//        this.handler = handler;
//    }
//
//    public static Bootstrap b = new Bootstrap();
//
//    private static ChannelFutureListener channelFutureListener = null;
//
//    public void starting(){
//        ClientHandler handler = new ClientHandler();
//        ClientDemo clientDemo = new ClientDemo(handler,url,port);
//        Thread t1 = new Thread(clientDemo);
//        t1.start();
//    }
//
//    /**
//     * When an object implementing interface <code>Runnable</code> is used
//     * to create a thread, starting the thread causes the object's
//     * <code>run</code> method to be called in that separately executing
//     * thread.
//     * <p>
//     * The general contract of the method <code>run</code> is that it may
//     * take any action whatsoever.
//     *
//     * @see Thread#run()
//     */
//    @Override
//    public void run() {
//        NioEventLoopGroup group = new NioEventLoopGroup();
//        // Client服务启动器 3.x的ClientBootstrap
//        // 改为Bootstrap，且构造函数变化很大，这里用无参构造。
//        b = new Bootstrap();
//        // 指定EventLoopGroup
//        b.group(group);
//        // 指定channel类型
//        b.channel(NioSocketChannel.class);
//        // 指定Handler
//        b.handler(new ChannelInitializer<SocketChannel>() {
//            @Override
//            protected void initChannel(SocketChannel ch) throws Exception {
//                System.out.println("绑定连接初始化器...");
//                ch.pipeline().addLast("ping", new IdleStateHandler(10, 10, 20, TimeUnit.SECONDS));
//                byte[] bytes = new byte[]{(byte) 0xFF, (byte) 0xFF};
//                ByteBuf delimeter = Unpooled.copiedBuffer(bytes);
//                //以FFFF作为消息的结束标志
//                ch.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, delimeter));
//                //设置消息的处理
//                ch.pipeline().addLast(handler);
//            }
//        });
//        //设置TCP协议的属性
//        b.option(ChannelOption.SO_KEEPALIVE, true);
//        b.option(ChannelOption.TCP_NODELAY, true);
//        b.option(ChannelOption.SO_TIMEOUT, 5000);
//
//        channelFutureListener = new ChannelFutureListener() {
//            @Override
//            public void operationComplete(ChannelFuture f) throws Exception {
//                if (f.isSuccess()) {
//                    isConnect = true;
//                } else {
//                    System.out.println("重新连接服务器失败");
//                    isConnect = false;
//                    //  3秒后重新连接
//                    f.channel().eventLoop().schedule(new Runnable() {
//                        @Override
//                        public void run() {
//                            doConnect();
//                        }
//                    }, 10, TimeUnit.SECONDS);
//                }
//            }
//        };
//        doConnect();
//    }
//
//
//    //  连接到服务端
//    public static void doConnect() {
//        try {
//            Thread.sleep(150L);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        ChannelFuture future = null;
//        try {
//            future = b.connect(new InetSocketAddress(
//                    url, port));
//            future.addListener(channelFutureListener);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//
//}