package com.example.administrator.httpurlconnection;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
//声明

public class httpurlconnection_activity extends AppCompatActivity implements View.OnClickListener
{
    Button sendRequest;
    EditText edit;
    TextView responseText;
    //建立新变量

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.httpurlconnection_activity_layout);
        sendRequest=(Button)findViewById(R.id.send_request);
        edit=(EditText)findViewById(R.id.editText);
        responseText=(TextView)findViewById(R.id.response_text);
        //赋值
        sendRequest.setOnClickListener(this);
        //点击监听
    }

    @Override
    public void onClick(View v)
    {
        switch(v.getId())
        {
            case R.id.send_request:
                sendRequestWithHttpURLConnection();
                //开启子线程
                break;
            default:
                break;
        }
    }

    private void sendRequestWithHttpURLConnection()
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                HttpURLConnection connection=null;
                BufferedReader reader=null;
                //建立新变量
                try
                {
                    URL url=new URL(edit.getText().toString());
                    //这里的网址可自选
                    connection=(HttpURLConnection)url.openConnection();
                    connection.setRequestMethod("GET");
                    //HttpURLConnection的用法
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    //设置
                    InputStream in=connection.getInputStream();
                    reader=new BufferedReader(new InputStreamReader(in));
                    StringBuilder response=new StringBuilder();
                    String line;
                    while((line=reader.readLine())!=null)
                    {
                        response.append(line);
                    }
                    //得值
                    ShowResponse(response.toString());
                    //显示
                }catch(Exception e)
                {
                    e.printStackTrace();
                }finally
                {
                    if(reader!=null)
                    {
                        try
                        {
                            reader.close();
                        }catch(IOException e)
                        {
                            e.printStackTrace();
                        }
                    }
                    if(connection!=null)
                    {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    private void ShowResponse(final String response)
    {
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                responseText.setText(response);
            }
        });
    }

}
