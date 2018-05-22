package me.matoosh.undernet.p2p.router.data.resource;

import me.matoosh.undernet.p2p.node.Node;
import me.matoosh.undernet.p2p.router.data.NetworkID;
import me.matoosh.undernet.p2p.router.data.message.ResourceMessage;

import java.io.Serializable;

/**
 * Represents a stored resource.
 * Created by Mateusz Rębacz on 25.09.2017.
 */

public abstract class Resource implements Serializable {
    /**
     * The network id of this resource.
     */
    private NetworkID networkID;

    /**
     * The owner of the resource.
     */
    private NetworkID owner;

    /**
     * Calculates the network id of the resource based on its contents.
     */
    public abstract void calcNetworkId();
    /**
     * Returns the type of the resource. E.g file resource.
     * @return
     */
    public abstract ResourceType getResourceType();

    /**
     * Handles the sending of a resource.
     */
    public abstract void send(Node recipient, IResourceActionListener resourceActionListener);

    /**
     * Handles the receiving of a resource.
     * @param sender
     */
    public abstract void receive(ResourceMessage message, Node sender, IResourceActionListener resourceActionListener);

    @Override
    public String toString() {
        return "Resource{" +
                "networkID=" + networkID +
                '}';
    }

    /**
     * Checks whether the resource is present in the self node.
     * @return
     */
    public abstract boolean isLocal();

    /**
     * Gets the resource's friendly display name.
     * @return
     */
    public abstract String getDisplayName();

    /**
     * Sets the network id.
     * @param id
     */
    public void setNetworkID(NetworkID id) {
        if(id.isValid()) {
            this.networkID = id;
        } else {
            ResourceManager.logger.info("Can't set resource id to: {}, invalid ID", id);
        }
    }
    /**
     * Gets the network id.
     * @return
     */
    public NetworkID getNetworkID() {
        return this.networkID;
    }
    /**
     * Sets the owner.
     * @param owner
     */
    public void setOwner(NetworkID owner) {
        this.owner = owner;
    }
    /**
     * Gets the owner.
     */
    public NetworkID getOwner() {
        return this.owner;
    }

    /**
     * Listens for the finishing of a resource action.
     */
    public interface IResourceActionListener {
        /**
         * Called when the action is finished.
         * @param other the node associated with the action.
         */
        public void onFinished(Node other);
    }
}
