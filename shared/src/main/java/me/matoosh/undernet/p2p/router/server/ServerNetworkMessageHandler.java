package me.matoosh.undernet.p2p.router.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;
import me.matoosh.undernet.event.EventManager;
import me.matoosh.undernet.event.channel.ChannelClosedEvent;
import me.matoosh.undernet.event.channel.ChannelCreatedEvent;
import me.matoosh.undernet.event.channel.ChannelErrorEvent;
import me.matoosh.undernet.event.channel.message.ChannelMessageReceivedEvent;
import me.matoosh.undernet.p2p.node.Node;
import me.matoosh.undernet.p2p.router.data.message.NetworkMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles data transfered over a channel.
 * Created by Mateusz Rębacz on 21.09.2017.
 */

public class ServerNetworkMessageHandler extends ChannelInboundHandlerAdapter {
    /**
     * The server of this channel handler.
     */
    public Server server;

    //Attribute ids.
    /**
     * Defines the client node attribute id.
     */
    public static final AttributeKey<Node> ATTRIBUTE_KEY_CLIENT_NODE = AttributeKey.valueOf("ClientNode");

    /**
     * The logger of the class.
     */
    public static Logger logger = LoggerFactory.getLogger(ServerNetworkMessageHandler.class);

    public ServerNetworkMessageHandler(Server server) {
        this.server = server;
    }

    /**
     * Called when the channel is ready for data transfer.
     *
     * @param ctx
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //Adding the channel to the server list.
        server.channels.add(ctx.channel());

        //Adding a node object to the connection.
        Node clientNode = new Node();
        clientNode.address = ctx.channel().remoteAddress(); //Setting the node's address.
        clientNode.channel = ctx.channel();
        ctx.channel().attr(ATTRIBUTE_KEY_CLIENT_NODE).set(clientNode);

        //Adding the client node to the connectedNodes list.
        server.router.connectedNodes.add(clientNode);

        //Calling the channel created event.
        EventManager.callEvent(new ChannelCreatedEvent(ctx.channel(), true));
    }

    /**
     * Called when the channel is no longer ready for data transfer.
     *
     * @param ctx
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        //Removing the channel from the server list.
        server.channels.remove(ctx.channel());

        //Removing the client from the connectedNodes list.
        Node clientNode = ctx.channel().attr(ATTRIBUTE_KEY_CLIENT_NODE).get();
        clientNode.channel = null;
        server.router.connectedNodes.remove(clientNode);

        //Calling the channel closed event.
        EventManager.callEvent(new ChannelClosedEvent(ctx.channel(), true));
    }

    /**
     * Called when data has been received from the channel.
     *
     * @param ctx
     * @param msg
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //Reading the incoming content as a NetworkMessage.
        if(msg instanceof NetworkMessage) {
            NetworkMessage networkMessage = (NetworkMessage) msg;
            try {
                EventManager.callEvent(new ChannelMessageReceivedEvent(ctx.channel(), true, networkMessage));
            } finally {
                networkMessage = null; //Releasing the msg from memory.
            }
        }
    }

    /**
     * Calls {@link ChannelHandlerContext#fireExceptionCaught(Throwable)} to forward
     * to the next {@link ChannelHandler} in the {@link ChannelPipeline}.
     * <p>
     * Sub-classes may override this method to change behavior.
     *
     * @param ctx
     * @param cause
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //Logging the exception.
        logger.error("Error with connection from: " + ctx.channel().remoteAddress(), cause);
        EventManager.callEvent(new ChannelErrorEvent(ctx.channel(), true, cause));

        //Closing the connection.
        ctx.close();
    }
}
