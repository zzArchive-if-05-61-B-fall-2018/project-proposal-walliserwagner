package smartshoppinglist.at.smartshoppinglist.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Connectivity extends Thread{

    private boolean isConnected = false;
    private String hostip;
    private int port;

    public boolean isConnected() {
        return isConnected;
    }

    public Connectivity(String ip, int port){
        hostip = ip;
        this.port = port;
    }

    @Override
    public void run() {
        while(true) {
            try {
                try (Socket soc = new Socket()) {
                    soc.connect(new InetSocketAddress(hostip, port), 1000);
                }
                isConnected = true;
            } catch (IOException ex) {
                isConnected = false;
            }
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
