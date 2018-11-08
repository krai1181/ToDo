package com.avi.todo;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class MyTask extends AsyncTask<Void,Void,Void> {
    private Context context;
    private static final String TAG = "MyTask";

    public MyTask(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.d(TAG, "onPreExecute: ");
    }

    @Override
    protected Void doInBackground(Void... voids) {
        Log.d(TAG, "doInBackground: ");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        Log.d(TAG, "onPostExecute: ");
    }
}
