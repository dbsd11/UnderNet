package me.matoosh.undernet.p2p.router.data.resource;

import me.matoosh.undernet.UnderNet;
import me.matoosh.undernet.event.Event;
import me.matoosh.undernet.event.EventHandler;
import me.matoosh.undernet.event.EventManager;
import me.matoosh.undernet.event.ftp.FileTransferFinishedEvent;
import me.matoosh.undernet.p2p.node.Node;
import me.matoosh.undernet.p2p.router.data.NetworkID;
import me.matoosh.undernet.p2p.router.data.filetransfer.FileInfo;
import me.matoosh.undernet.p2p.router.data.filetransfer.FileTransfer;

import java.io.*;

/**
 * Represents a stored file resource.
 * Created by Mateusz Rębacz on 26.09.2017.
 */

public class FileResource extends Resource {
    /**
     * Information about the file.
     */
    public FileInfo fileInfo;

    /**
     * The file.
     */
    private transient File file;

    /**
     * The file transfer of this resource.
     */
    private transient FileTransfer transfer;

    /**
     * Creates a new file resource given file.
     * @param file
     */
    public FileResource(File file) {
        this.file = file;
        this.fileInfo = new FileInfo(this.file);
    }

    /**
     * Calculates the network id of the resource based on its name.
     */
    @Override
    public void calcNetworkId() {
        if(this.getNetworkID() == null) {
            this.setNetworkID(NetworkID.generateFromString(fileInfo.fileName));
        }
    }

    /**
     * Copies the file if its not in the content directory.
     */
    public boolean copyToContent() {
        if(this.getNetworkID() == null) {
            calcNetworkId();
        }
        if(!file.toString().startsWith(UnderNet.fileManager.getContentFolder().toString())) {
            if(!file.exists()) return false;
            if(!file.canRead()) return false;
            if(file.isHidden()) return false;
            if(file.isDirectory()) return false;

            InputStream is = null;
            OutputStream os = null;
            try {
                is = new FileInputStream(file);
                os = new FileOutputStream(UnderNet.fileManager.getContentFolder() + "/" + this.fileInfo.fileName);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = is.read(buffer)) > 0) {
                    os.write(buffer, 0, length);
                }
            } catch (IOException e) {
                ResourceManager.logger.error("An error occured copying file: " + file.toString() + " to the content directory!", e);
                return false;
            } finally {
                try {
                    is.close();
                    os.close();
                } catch (IOException e) {
                    ResourceManager.logger.error("An error occured while closing the copy streams for file: " + file.toString() + "!", e);
                    return false;
                }
            }
        }

        //Updating the path.
        this.file = new File(UnderNet.fileManager.getContentFolder() + "/" + this.fileInfo.fileName);
        this.fileInfo = new FileInfo(this.file);

        return true;
    }

    /**
     * Returns the type of the resource. E.g file resource.
     *
     * @return
     */
    @Override
    public ResourceType getResourceType() {
        return ResourceType.FILE;
    }

    /**
     * Prepares the file transfer for the receiver.
     * @param recipient
     * @param resourceActionListener
     */
    @Override
    public void send(Node recipient, IResourceActionListener resourceActionListener) {
        //Preparing a file transfer to the pushTo node.
        UnderNet.router.fileTransferManager.prepareFileTranfer(FileResource.this, recipient);
        resourceActionListener.onFinished(recipient);
    }

    /**
     * Requests and receives the file.
     * @param sender
     * @param resourceActionListener
     */
    @Override
    public void receive(final Node sender, final IResourceActionListener resourceActionListener) {
        //Requesting the file transfer.
        transfer = UnderNet.router.fileTransferManager.requestFileTransfer(sender, this);
        EventManager.registerHandler(new EventHandler() {
            @Override
            public void onEventCalled(Event e) {
                FileTransferFinishedEvent transferFinishedEvent = (FileTransferFinishedEvent)e;
                if(transferFinishedEvent.transfer == FileResource.this.transfer) {
                    //Transfer of the resource has finished. The resource is ready to push.
                    resourceActionListener.onFinished(sender);

                    //Unregisters the current handler.
                    EventManager.unregisterHandler(this, FileTransferFinishedEvent.class);
                }
            }
        }, FileTransferFinishedEvent.class);
    }

    @Override
    public String toString() {
        return "FileResource{" +
                "networkID=" + this.getNetworkID() +
                ", fileInfo=" + fileInfo +
                '}';
    }

    /**
     * Checks whether the file is within the content folder.
     * @return
     */
    @Override
    public boolean isLocal() {
        if(this.file == null) {
            this.file = new File(UnderNet.fileManager.getContentFolder() + "/" + this.fileInfo.fileName);
        }
        if(this.file != null && this.file.exists()) {
            return true;
        }
        return false;
    }

    /**
     * Returns the name of the file.
     * @return
     */
    @Override
    public String getDisplayName() {
        return fileInfo.fileName;
    }
}
