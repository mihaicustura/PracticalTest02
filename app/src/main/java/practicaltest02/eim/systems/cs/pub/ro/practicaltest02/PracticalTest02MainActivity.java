package practicaltest02.eim.systems.cs.pub.ro.practicaltest02;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class PracticalTest02MainActivity extends AppCompatActivity {

    // Server widgets
    private EditText serverPortEditText = null;
    private Button startButton = null;

    // Client widgets
    private EditText clientUrlEditText = null;
    //private EditText clientPortEditText = null;
    //private EditText cityEditText = null;
    //private Spinner informationTypeSpinner = null;
    private Button goButton = null;
    private TextView serverResponseTextView = null;

    private ServerThread serverThread = null;
    private ClientThread clientThread = null;

    private ConnectButtonClickListener startButtonClickListener = new ConnectButtonClickListener();
    private class ConnectButtonClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            String serverPort = serverPortEditText.getText().toString();
            if (serverPort == null || serverPort.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Server port should be filled!", Toast.LENGTH_SHORT).show();
                return;
            }
            serverThread = new ServerThread(Integer.parseInt(serverPort));
            if (serverThread.getServerSocket() == null) {
                Log.e(Constants.TAG, "[MAIN ACTIVITY] Could not create server thread!");
                return;
            }
            serverThread.start();
        }

    }

    private GetWeatherForecastButtonClickListener goButtonClickListener = new GetWeatherForecastButtonClickListener();
    private class GetWeatherForecastButtonClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            /*String clientAddress = clientAddressEditText.getText().toString();
            String clientPort = clientPortEditText.getText().toString();
            if (clientAddress == null || clientAddress.isEmpty()
                    || clientPort == null || clientPort.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Client connection parameters should be filled!", Toast.LENGTH_SHORT).show();
                return;
            }*/
            String url = clientUrlEditText.getText().toString();
            if (url == null || url.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Client connection parameters should be filled!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (serverThread == null || !serverThread.isAlive()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] There is no server to connect to!", Toast.LENGTH_SHORT).show();
                return;
            }
            /*String city = cityEditText.getText().toString();
            String informationType = informationTypeSpinner.getSelectedItem().toString();
            if (city == null || city.isEmpty()
                    || informationType == null || informationType.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Parameters from client (city / information type) should be filled", Toast.LENGTH_SHORT).show();
                return;
            }*/

            serverResponseTextView.setText(Constants.EMPTY_STRING);

            clientThread = new ClientThread(
                    "localhost", url, serverThread.getPort(), serverResponseTextView
            );
            clientThread.start();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(Constants.TAG, "[MAIN ACTIVITY] onCreate() callback method has been invoked");
        setContentView(R.layout.activity_practical_test02_main);

        serverPortEditText = (EditText)findViewById(R.id.server_port_edit_text);
        startButton = (Button)findViewById(R.id.start_button);
        startButton.setOnClickListener(startButtonClickListener);

        clientUrlEditText = (EditText)findViewById(R.id.client_url_edit_text);
        //clientPortEditText = (EditText)findViewById(R.id.client_port_edit_text);
        //cityEditText = (EditText)findViewById(R.id.city_edit_text);
        //informationTypeSpinner = (Spinner)findViewById(R.id.information_type_spinner);
        goButton = (Button)findViewById(R.id.go_button);
        goButton.setOnClickListener(goButtonClickListener);
        serverResponseTextView = (TextView)findViewById(R.id.server_response_text_view);
    }

    @Override
    protected void onDestroy() {
        Log.i(Constants.TAG, "[MAIN ACTIVITY] onDestroy() callback method has been invoked");
        if (serverThread != null) {
            serverThread.stopThread();
        }
        super.onDestroy();
    }

}
