package com.example.freehtwoo.subscriberirecruit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;

/**
 * Created by FreeHtwoO on 5/31/15.

 In order not lock the UI the POST has to be done in an different thread so this class that
 the POST can be performed
extends Async*/

public class Networking extends AsyncTask<Void,Void,String> {
    public final static String Message_KEY = "unquie_meessage_key";
    public final static String Message_KEY2 = "unquie_meessage_key_result";
    public String firstname;
    public String lastname;
    public String email;
    public Context context;
    public int language;

    //All the parameters needed are send in the constructor, their validity has been checked in SubscriberActivity
    public Networking(String firstname, String lastname, String email, Context context, int language) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.context = context;
        this.language=language;
    }

    //This methods performs the POST
    // Now there were a few libraries I could have used for this task but, I decided to
    // do with basic java, as it would provided a better understanding of what is
    // going on and a better learning experience, although most of the classes used are deprecated
    @Override
    protected String doInBackground(Void... params) {

        HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
        HttpParams parameters = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(parameters, 3000);
        HttpConnectionParams.setSoTimeout(parameters, 5000);
        DefaultHttpClient client = new DefaultHttpClient(parameters);
        SchemeRegistry registry = new SchemeRegistry();
        SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
        socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
        registry.register(new Scheme("https", socketFactory, 443));
        SingleClientConnManager mgr = new SingleClientConnManager(client.getParams(), registry);


        DefaultHttpClient httpClient = new DefaultHttpClient(mgr, client.getParams());
        HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
        HttpPost httpPost = new HttpPost("https://test-api.smart-trial.dk/api/public/signup/5566e151817a62021b1ea809");
        httpPost.setHeader("content-type", "application/x-www-form-urlencoded");


        ArrayList<NameValuePair> nameValuePairArrayList = new ArrayList<NameValuePair>(3);
        nameValuePairArrayList.add(new BasicNameValuePair("firstname", firstname));
        nameValuePairArrayList.add(new BasicNameValuePair("lastname", lastname));
        nameValuePairArrayList.add(new BasicNameValuePair("email", email));


        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairArrayList));
        } catch (UnsupportedEncodingException e) {
            return e.toString();
        }
        try {
            HttpResponse response = httpClient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == 400) {
                //I know that 400 is send also when there are no names provided, but due to the check
                // on SubscriberPage email in use is the only 400 response possible
                return "email in use";
            } else if (response.getStatusLine().getStatusCode() == 200) {
                return "worked";
            }
            return "did not work";
        } catch (ClientProtocolException e) {
            return "ClientProtocolException";
        } catch (IOException e) {
            return "timeout";
        }
    }


    @Override
      protected void  onPostExecute(String result) {
        Intent intent = new Intent(context, ResultActivity.class);
        intent.putExtra(Message_KEY, language);
        intent.putExtra(Message_KEY2, result);
        context.startActivity(intent);
    }
}


