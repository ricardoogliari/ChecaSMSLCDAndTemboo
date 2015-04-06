package com.box.personal.ricardo.receiversmstemboo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.provider.Telephony;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;


public class MainActivity extends ActionBarActivity {

    private EditText edtIp;
    private EditText edtEnvio;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //mesmo código
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtIp = (EditText) findViewById(R.id.edtIp);
        edtEnvio = (EditText) findViewById(R.id.edtEnvio);

        //<action android:name="android.provider.Telephony.SMS_RECEIVED" />
        IntentFilter iFilter = new IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION);
        registerReceiver(receiver, iFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    public void mandar(View v){
        new Assincrono().execute();
    }

    class Assincrono extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            BufferedWriter escritorLinhas;
            OutputStreamWriter escritorCaracteres;
            OutputStream escritorSocket;

            Socket s;
            try {
                s = new Socket(edtIp.getText().toString(), 8080);

                escritorSocket = s.getOutputStream();
                escritorCaracteres = new OutputStreamWriter(escritorSocket);
                escritorLinhas = new BufferedWriter(escritorCaracteres);
                escritorLinhas.write(edtEnvio.getText().toString());
                escritorLinhas.flush();
                escritorLinhas.close();

                s.close();
                return "foi";
            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "UnknownHostException: ";
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "IOException: "+e;
            }
        }

    }
}
