package com.htc.eleven.accessasserts;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button readAsserts = null;
    private Button readRaw  = null;

    private final String fileName = "eleven.txt";
    private final String internalFile = "data.txt";

    private EditText et = null;
    private TextView ret = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * button which was used to read file under asserts folder.
         * */
        readAsserts = (Button) findViewById(R.id.btnReadAssertFile);
        readAsserts.setOnClickListener(this);

        /**
        * button which was user to read file under res/raw folder.
        * */
        readRaw = (Button) findViewById(R.id.btnReadRawFile);
        readRaw.setOnClickListener(this);

        /**
         * buttons which was used to read/write internal data under /data/data/app_name/ folder.
         * */
        findViewById(R.id.ReadInternalStorage).setOnClickListener(this);
        findViewById(R.id.WriteInternalStorage).setOnClickListener(this);
        et= (EditText) findViewById(R.id.et);
        ret = (TextView) findViewById(R.id.ShowContent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnReadAssertFile:
            {
                try {
                    InputStream input = getResources().getAssets().open(fileName);
                    InputStreamReader reader = new InputStreamReader(input);
                    BufferedReader bufferedReader = new BufferedReader(reader);

                    String buf;
                    while ((buf=bufferedReader.readLine()) != null) {
                        System.out.println(buf);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
                break;

            case R.id.btnReadRawFile:
            {
                InputStream inputStream = getResources().openRawResource(R.raw.info);
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                String buf;
                try {
                    while((buf=bufferedReader.readLine()) != null) {
                        System.out.println(buf);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    bufferedReader.close();
                    inputStreamReader.close();
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
                break;

            case R.id.ReadInternalStorage:
                try {
                    FileInputStream fileInputStream = openFileInput(internalFile);

                    InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);

                    char [] buf = new char[fileInputStream.available()];

                    inputStreamReader.read(buf);

                    String data = new String(buf);
                    ret.setText(data);

                    inputStreamReader.close();
                    fileInputStream.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.WriteInternalStorage:
                try {
                    // here use openFileOutput and transfer Context.MODE_PRIVATE argument !!!
                    FileOutputStream fileOutputStream = openFileOutput(internalFile, Context.MODE_PRIVATE);
                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);

                    outputStreamWriter.write(et.getText().toString());

                    // flush string into disc file.
                    outputStreamWriter.flush();
                    fileOutputStream.flush();

                    // close to release.
                    outputStreamWriter.close();
                    fileOutputStream.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Toast.makeText(MainActivity.this,"Write successfully !", Toast.LENGTH_LONG).show();
                break;
        }
    }
}
