package com.box.personal.ricardo.receiversmstemboo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by ricardo on 04/04/2015.
 */
public class VerificaSMS extends BroadcastReceiver{

    String numero = "";

    @Override
    public void onReceive(Context context, Intent intent) {

        final Bundle bundle = intent.getExtras();

        try {

            if (bundle != null) {

                final Object[] pdusObj = (Object[]) bundle.get("pdus");

                for (int i = 0; i < pdusObj.length; i++) {

                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();

                    String senderNum = phoneNumber;
                    String message = currentMessage.getDisplayMessageBody();

                    Log.i("SmsReceiver", "senderNum: " + senderNum + "; message: " + message);

                    if (message.indexOf("amor") > 0){
                        numero = " -1"+senderNum+"-";
                    } else {
                        numero = " -0"+senderNum+"-";
                    }
                    new Assincrono().execute();

                    // Show alert
                    int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(context, "senderNum: "+ senderNum + ", message: " + message, duration);
                    toast.show();

                } // end for loop
            } // bundle is null

        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" +e);

        }

    }

    class Assincrono extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            BufferedWriter escritorLinhas;
            OutputStreamWriter escritorCaracteres;
            OutputStream escritorSocket;

            Socket s;
            try {
                s = new Socket("192.168.1.5", 8080);

                escritorSocket = s.getOutputStream();
                escritorCaracteres = new OutputStreamWriter(escritorSocket);
                escritorLinhas = new BufferedWriter(escritorCaracteres);
                escritorLinhas.write(numero);
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
