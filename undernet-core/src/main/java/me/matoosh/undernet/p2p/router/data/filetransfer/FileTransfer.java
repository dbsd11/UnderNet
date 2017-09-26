package me.matoosh.undernet.p2p.router.data.filetransfer;

import java.io.File;
import java.io.FileInputStream;

/**
 * Represents a single active file transfer.
 * Created by Mateusz Rębacz on 26.09.2017.
 */

public class FileTransfer {
    /**
     * Id of the transfer.
     */
    public short id;

    /**
     * The save stream.
     */
    public FileInputStream saveStream;

    public FileTransfer(File saveTo, short id) {
        this.id = id;
        //TODO: Open save stream.
    }
}
