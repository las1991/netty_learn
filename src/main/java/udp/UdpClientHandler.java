package udp;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;

/**
 * @version 1.0
 * @Description
 * @Author：andy
 * @CreateDate：2016/2/23
 */
public class UdpClientHandler extends SimpleChannelInboundHandler<DatagramPacket> {

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception {
        System.out.println(msg.content().toString(CharsetUtil.UTF_8));
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

}
