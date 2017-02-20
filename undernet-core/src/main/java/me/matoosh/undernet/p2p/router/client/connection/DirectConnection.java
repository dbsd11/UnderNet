package me.matoosh.undernet.p2p.router.client.connection;

import java.io.IOException;

import me.matoosh.undernet.p2p.node.KnownNode;
import me.matoosh.undernet.p2p.router.client.Client;

/**
 * Created by Mateusz Rębacz on 20.02.2017.
 */

public class DirectConnection extends Connection {

    public DirectConnection(Client client, KnownNode node, Thread thread) throws Exception {
        super(client, node, thread);
    }

    @Override
    public void init() throws ConnectionException {

    }

    @Override
    public void session() {

    }
    @Override
    public void drop() {

    }
}
