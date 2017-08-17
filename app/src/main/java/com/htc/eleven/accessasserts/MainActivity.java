package com.htc.eleven.accessasserts;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
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
    private final String internalFile = "data_internel.txt";
    private final String externalFile = "data_externel.txt";
    private String externalFilePath = Environment.getExternalStorageDirectory().getPath() + File.separator +externalFile;

    private EditText et = null;
    private TextView ret = null;

    private final int requestCode = 1;

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


        /**
         * button which was used to read/write external data under /storage/emulated/0/ folder.
         * */

        findViewById(R.id.ReadExternalStorage).setOnClickListener(this);
        findViewById(R.id.WriteExternalStorage).setOnClickListener(this);

        // read/write external storage need request permission dynamically in code.

        String[] permissions = new String[]{"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE"};
        requestPermissions(permissions, requestCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // obtain requestPermission() result, and judge if should we go ahead.
        if(requestCode == this.requestCode) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] != 0) {
                    finish();
                }
            }
        }
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

                    // read and print to log.
//                    String buf;
//                    while ((buf=bufferedReader.readLine()) != null) {
//                        System.out.println(buf);
//                    }

                    // read and post content to TextView.
                    char[] data = new char[input.available()];
                    bufferedReader.read(data);

                    String content = new String(data);
                    ret.setText(content);
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

                // read and print content to log.
//                String buf;
//                try {
//                    while((buf=bufferedReader.readLine()) != null) {
//                        System.out.println(buf);
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                try {
//                    bufferedReader.close();
//                    inputStreamReader.close();
//                    inputStream.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }

                // read and post content to TextView.
                char[] data = new char[0];
                try {
                    data = new char[inputStream.available()];
                    bufferedReader.read(data);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String content = new String(data);
                ret.setText(content);
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

                    Toast.makeText(MainActivity.this,"Write successfully !", Toast.LENGTH_LONG).show();
                    // close to release.
                    outputStreamWriter.close();
                    fileOutputStream.close();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.WriteExternalStorage:
                try {
                    // write at the beginning of the file.
//                    FileOutputStream fileOutputStream = new FileOutputStream(externalFilePath);

                    // write at the end of the file.
                    FileOutputStream fileOutputStream = new FileOutputStream(externalFilePath, true);
                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);

                    outputStreamWriter.write(et.getText().toString());

                    Toast.makeText(MainActivity.this,"Write to external path: "+externalFilePath+" successfully!",Toast.LENGTH_LONG).show();
                    outputStreamWriter.flush();
                    outputStreamWriter.close();
                    fileOutputStream.close();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.ReadExternalStorage:
                File file = new File(externalFilePath);
                if(file.exists()) {
                    try {
                        FileInputStream fis = new FileInputStream(file);
                        InputStreamReader isr = new InputStreamReader(fis);

                        char [] data = new char[fis.available()];
                        isr.read(data);

                        isr.close();
                        fis.close();

                        String str = new String(data);
                        ret.setText(str);

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }
}
