package me.matoosh.undernet.event.resource.push;

import me.matoosh.undernet.event.resource.ResourceEvent;
import me.matoosh.undernet.p2p.node.Node;
import me.matoosh.undernet.p2p.router.data.message.ResourceMessage;
import me.matoosh.undernet.p2p.router.data.resource.Resource;

/**
 * Called when a resource push is sent to a neighbor.
 * Created by Mateusz Rębacz on 02.10.2017.
 */

public class ResourcePushSentEvent extends ResourceEvent {
    /**
     * The push content.
     */
    public ResourceMessage pushMessage;
    /**
     * The node the content is sent to.
     */
    public Node recipientNode;

    public ResourcePushSentEvent(Resource resource, ResourceMessage msg, Node recipientNode) {
        super(resource);
        this.pushMessage = msg;
        this.recipientNode = recipientNode;
    }

    /**
     * Executed when the event is called, but before it is delivered to the handlers.
     */
    @Override
    public void onCalled() {

    }
}
