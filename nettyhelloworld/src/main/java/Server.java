import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class Server {

  public static void main(String[] args) throws InterruptedException {

    NioEventLoopGroup boss = new NioEventLoopGroup();
    NioEventLoopGroup worker = new NioEventLoopGroup();

    ServerBootstrap serverBootstrap = new ServerBootstrap();
    serverBootstrap
        .group(boss, worker)
        .channel(NioServerSocketChannel.class)
        .option(ChannelOption.SO_BACKLOG, 1024)
        .childOption(ChannelOption.SO_SNDBUF, 32 * 1024)
        .option(ChannelOption.SO_RCVBUF, 32 * 1024)
        .childOption(ChannelOption.SO_KEEPALIVE, true)
        .childHandler(new ChannelInitializer<SocketChannel>() {

          @Override
          protected void initChannel(SocketChannel channel) {
            channel.pipeline().addLast(new ServerHandler());
          }
        });

    ChannelFuture future = serverBootstrap.bind(9999).sync();
    future.channel().closeFuture().sync();
    worker.shutdownGracefully();
    boss.shutdownGracefully();
  }
}
