import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.io.UnsupportedEncodingException;

public class ClientHandler extends ChannelInboundHandlerAdapter {

  @Override
  public void channelActive(ChannelHandlerContext ctx) {

    System.out.println("Client active channel");
  }

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg)
      throws UnsupportedEncodingException {
    ByteBuf in = (ByteBuf) msg;
    byte[] bytes = new byte[in.readableBytes()];
    in.readBytes(bytes);

    String body = new String(bytes, "utf-8");
    System.out.println("Client received from server: " + body);
  }

  @Override
  public void channelReadComplete(ChannelHandlerContext ctx) {

    System.out.println("Client read complete");
  }

}
