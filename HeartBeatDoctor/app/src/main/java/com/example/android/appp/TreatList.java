package com.example.android.appp;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;


public class TreatList extends AppCompatActivity {


    Thread responseThread;
    boolean coned=false;
    DataInputStream dis;
    DataOutputStream dos;
    ArrayAdapter<String> listItems;
    ArrayList<JSONObject> responseJsonList=new ArrayList<JSONObject>();
    ListView list;
    Button testButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treat_list);

        list=(ListView)findViewById(R.id.treatListView);
        listItems=new ArrayAdapter<String>(this, R.layout.list_name);
        list.setAdapter(listItems);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String s=(String) parent.getItemAtPosition(position);
                for(int i=0;i<responseJsonList.size();i++){
                    if(responseJsonList.get(i).getString("patientWalletDID").equals(s)){
                        Intent intent=new Intent(TreatList.this,Treat.class);
                        intent.putExtra("extra",responseJsonList.get(i).toString());
                        startActivity(intent);
                        break;
                    }
                }
            }
        });
        getListJSON();
        testButton=(Button)findViewById(R.id.testButton);
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(TreatList.this,Treat.class);
                JSONObject jo=new JSONObject();
                jo.put("type","reqestTreat");

                jo.put("additionalInfomation","8.20傍晚锻炼，心率可能过快");
                jo.put("age","20");
                intent.putExtra("extra",jo.toString());
                startActivity(intent);
            }
        });
    }

    public void getListJSON() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    coned = false;
                    try {
                        coned = ((CommonInstance) getApplication()).getServer().isConnected();
                        coned = !((CommonInstance) getApplication()).getServer().isClosed();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (!coned) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "未连接服务器", Toast.LENGTH_LONG).show();
                            }
                        });
                    } else {

                        try {
                            dis=((CommonInstance)getApplication()).getDataInputStream();
                            dos=((CommonInstance)getApplication()).getDataOutputStream();

                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("type", "requestTreatList");

                            dos.writeUTF(jsonObject.toString());
                            dos.flush();

                            responseThread=new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try{
                                        JSONObject jo=JSON.parseObject(dis.readUTF());
                                        final JSONArray ja=jo.getJSONArray("treatList");
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                for(int i = 0; i<ja.size(); i++){
                                                    responseJsonList.add(ja.getJSONObject(i));
                                                    listItems.add(ja.getJSONObject(i).getString("patientWalletDID"));
                                                }
                                                list.setAdapter(listItems);
                                            }
                                        });

                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }
                                }
                            });
                            responseThread.start();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
