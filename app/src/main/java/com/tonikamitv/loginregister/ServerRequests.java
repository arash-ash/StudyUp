package com.tonikamitv.loginregister;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.util.ArrayList;


public class ServerRequests {

    // for showing a loading bar
    ProgressDialog progressDialog;
    // time in ms before connection timeout
    public static final int CONNECTION_TIMEOUT = 1000 * 15;
    public static final String SERVER_ADDRESS = "http://pro-android.comlu.com/";

    //constructor using Context interface which is implemented by the
    // android system and can access environment variables
    public ServerRequests(Context context) {
        // instentiate the progress dialog
        progressDialog = new ProgressDialog(context);
        // prevents user from cancelling the progress bar
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Processing...");
        progressDialog.setMessage("Please wait...");
    }

    public void storeUserDataInBackground(User user,
                                          GetUserCallback userCallBack) {
        progressDialog.show();
        // executes the store user data task in background
        new StoreUserDataAsyncTask(user, userCallBack).execute();
    }

    public void fetchUserDataAsyncTask(User user, GetUserCallback userCallBack) {
        progressDialog.show();
        // executes the fetch user data in background
        new fetchUserDataAsyncTask(user, userCallBack).execute();
    }


    // it extends the AsyncTask which has to be a subclass that stores the users data.
    //AsyncTask enables proper and easy use of the UI thread. This class allows to perform
    //background operations and publish results on the UI thread without having to manipulate
    //threads and/or handlers.
    public class StoreUserDataAsyncTask extends AsyncTask<Void, Void, Void> {
        User user;
        GetUserCallback userCallBack;

        // constructor have to use GetUserCallback in order to get informed when
        //the task is finished performing
        public StoreUserDataAsyncTask(User user, GetUserCallback userCallBack) {
            this.user = user;
            this.userCallBack = userCallBack;
        }

        // the tasks to do in background. Press Ctrl+O to override methods
        @Override
        protected Void doInBackground(Void... params) {
            // NameValuePair is a datatype consiting of a key and a value
            //dataToSend containes all the information about the user
            //that we want to send to the server
            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("name", user.name));
            dataToSend.add(new BasicNameValuePair("department", user.department));
            dataToSend.add(new BasicNameValuePair("username", user.username));
            dataToSend.add(new BasicNameValuePair("password", user.password));

            // allows to change the attributes of the Http request
            HttpParams httpRequestParams = getHttpRequestParams();

            // sets the Http client that allows make requests
            HttpClient client = new DefaultHttpClient(httpRequestParams);

            // holds the data that we are trying to post to server
            //by calling the Register.php
            HttpPost post = new HttpPost(SERVER_ADDRESS
                    + "Register.php");

            try {
                // it encodes the data and post it to the server and then executes
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                client.execute(post);
            } catch (Exception e) {
                // givees exception error if anything goes wrong
                e.printStackTrace();
            }

            return null;
        }

        private HttpParams getHttpRequestParams() {
            HttpParams httpRequestParams = new BasicHttpParams();
            // sets the time for connection timeout
            HttpConnectionParams.setConnectionTimeout(httpRequestParams,
                    CONNECTION_TIMEOUT);
            // sets the time to wait before receiving anything from the server
            HttpConnectionParams.setSoTimeout(httpRequestParams,
                    CONNECTION_TIMEOUT);
            return httpRequestParams;
        }

        // when AsyncTask is finished
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // stops the progress dialog
            progressDialog.dismiss();
            // announces that the activity is done
            userCallBack.done(null);
        }

    }

    // similar to StoreUserDataAsyncTask but has to return User
    public class fetchUserDataAsyncTask extends AsyncTask<Void, Void, User> {
        User user;
        GetUserCallback userCallBack;

        public fetchUserDataAsyncTask(User user, GetUserCallback userCallBack) {
            this.user = user;
            this.userCallBack = userCallBack;
        }

        @Override
        protected User doInBackground(Void... params) {
            // creates the dataToSend of the entered username and password
            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("username", user.username));
            dataToSend.add(new BasicNameValuePair("password", user.password));

            // similar to before
            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams,
                    CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams,
                    CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS
                    + "FetchUserData.php");

            User returnedUser = null;

            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                // when executing the post we receive a http response
                HttpResponse httpResponse = client.execute(post);

                // extracts the information
                HttpEntity entity = httpResponse.getEntity();
                String result = EntityUtils.toString(entity);
                // decodes the information to JSON object
                JSONObject jObject = new JSONObject(result);

                if (jObject.length() != 0){
                    // gives confirmation message
                    Log.v("happened", "2");
                    String name = jObject.getString("name");
                    String department = jObject.getString("department");

                    // created the user with found information
                    returnedUser = new User(name, department, user.username,
                            user.password);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return returnedUser;
        }

        // similar to before except that returns a User when
        //background task is finished
        @Override
        protected void onPostExecute(User returnedUser) {
            super.onPostExecute(returnedUser);
            progressDialog.dismiss();
            // the user is returned to the userCallBack
            userCallBack.done(returnedUser);
        }
    }
}
