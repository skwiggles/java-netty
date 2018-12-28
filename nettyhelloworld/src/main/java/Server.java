import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class Server {

  public static void main(String[] args) throws InterruptedException {

    // handler request from client
    EventLoopGroup boss = new NioEventLoopGroup();
    // process request from client
    EventLoopGroup worker = new NioEventLoopGroup();

    ServerBootstrap serverBootstrap = new ServerBootstrap();
    serverBootstrap
        .group(boss, worker)
        .channel(NioServerSocketChannel.class)  // server mode
        .option(ChannelOption.SO_BACKLOG, 1024)  // tcp buffer
        .childOption(ChannelOption.SO_SNDBUF, 32 * 1024)  // sender buffer size
        .option(ChannelOption.SO_RCVBUF, 32 * 1024)  // receiver buffer size
        .childOption(ChannelOption.SO_KEEPALIVE, true)  // keep connection alive
        .childHandler(new ChannelInitializer<SocketChannel>() {

          @Override
          protected void initChannel(SocketChannel channel) {
            // use server handler to process request
            channel.pipeline().addLast(new ServerHandler());
          }
        });

    ChannelFuture future = serverBootstrap.bind(9999).sync();
    future.channel().closeFuture().sync();
    worker.shutdownGracefully();
    boss.shutdownGracefully();
  }
}
