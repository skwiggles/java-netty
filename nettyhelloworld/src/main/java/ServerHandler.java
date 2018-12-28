import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.io.UnsupportedEncodingException;

public class ServerHandler extends ChannelInboundHandlerAdapter {

  @Override
  public void channelActive(ChannelHandlerContext ctx) {

    System.out.println("Server active channel");
  }

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg)
      throws UnsupportedEncodingException {

    ByteBuf in = (ByteBuf) msg;
    byte[] bytes = new byte[in.readableBytes()];
    in.readBytes(bytes);
    String body = new String(bytes, "utf-8");
    System.out.println("Server received data: " + body);

    String response = "Server reply to client " + body;
    ctx.writeAndFlush(Unpooled.copiedBuffer(response.getBytes()));
  }

  @Override
  public void channelReadComplete(ChannelHandlerContext ctx) {

    System.out.println("Server read complete");
  }

}
