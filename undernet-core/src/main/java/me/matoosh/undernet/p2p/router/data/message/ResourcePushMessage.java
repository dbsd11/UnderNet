package me.matoosh.undernet.p2p.router.data.message;

import me.matoosh.undernet.p2p.router.data.resource.Resource;

/**
 * Message containing resource and its publish info.
 * Created by Mateusz Rębacz on 26.09.2017.
 */

public class ResourcePushMessage implements MsgBase {
    /**
     * The resource to be pushed.
     */
    private Resource resource;

    /**
     * Creates a new resource push message given the pushed resource.
     * @param resource
     */
    public ResourcePushMessage(Resource resource) {

    }

    /**
     * Convert the message data to byte[].
     *
     * @return
     */
    @Override
    public byte[] toByte() {
        return new byte[0];
    }

    /**
     * Convert the byte[] to the message.
     *
     * @param data
     */
    @Override
    public void fromByte(byte[] data) {

    }
}
