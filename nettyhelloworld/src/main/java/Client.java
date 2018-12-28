import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class Client {

  public static void main(String[] args) throws InterruptedException {

    EventLoopGroup worker = new NioEventLoopGroup();

    try {
      Bootstrap bootstrap = new Bootstrap();
      bootstrap.group(worker);
      bootstrap.channel(NioSocketChannel.class);
      bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
      bootstrap.handler(new ChannelInitializer<SocketChannel>() {

        @Override
        protected void initChannel(SocketChannel channel) {
          channel.pipeline().addLast(new ClientHandler());
        }
      });

      ChannelFuture future = bootstrap.connect("localhost", 9999).sync();
      future.channel().writeAndFlush(Unpooled.copiedBuffer("Hello World".getBytes()));
      future.channel().closeFuture().sync();
    } finally {
      worker.shutdownGracefully();
    }
  }
}
