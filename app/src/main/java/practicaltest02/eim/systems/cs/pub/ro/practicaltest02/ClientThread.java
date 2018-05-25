package practicaltest02.eim.systems.cs.pub.ro.practicaltest02;

import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by student on 25.05.2018.
 */

public class ClientThread extends Thread {

    private String address;
    private String url;
    private int port;
    private TextView serverResonseTextView;

    private Socket socket;

    public ClientThread(String address,String url, int port, TextView serverResponseTextView) {
        this.address = address;
        this.url = url;
        this.port = port;
        this.serverResonseTextView = serverResponseTextView;
    }

    @Override
    public void run() {
        try {
            socket = new Socket(address, port);
            if (socket == null) {
                Log.e(Constants.TAG, "[CLIENT THREAD] Could not create socket!");
                return;
            }
            BufferedReader bufferedReader = Utilities.getReader(socket);
            PrintWriter printWriter = Utilities.getWriter(socket);
            if (bufferedReader == null || printWriter == null) {
                Log.e(Constants.TAG, "[CLIENT THREAD] Buffered Reader / Print Writer are null!");
                return;
            }

            printWriter.println(url);
            printWriter.flush();

            String serverResponse;
            while ((serverResponse = bufferedReader.readLine()) != null) {
                final String finalizedServerResponse = serverResponse;
                serverResonseTextView.post(new Runnable() {
                    @Override
                    public void run() {
                        serverResonseTextView.append(finalizedServerResponse);
                    }
                //serverResonseTextView.append(finalizedServerResponse);
                });
            }
        } catch (IOException ioException) {
            Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException ioException) {
                    Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
                    if (Constants.DEBUG) {
                        ioException.printStackTrace();
                    }
                }
            }
        }
    }

}
