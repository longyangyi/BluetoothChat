package com.example.android.appp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;


public class Treat extends AppCompatActivity {

    DataInputStream dis;
    ArrayAdapter<String> dataItems;
    JSONArray dataListJsonArray;
    JSONObject requestJson;
    ListView list;
    TextView age,additionalInfomation,patientWhether;
    EditText text,meetTreatResponse;
    CheckBox box;
    Button send;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treat);

        final String jsonString=getIntent().getExtras().getString("extra");
        requestJson= JSON.parseObject(jsonString);

        additionalInfomation=(TextView)findViewById(R.id.patientAdditionalInfomation);
        age=(TextView)findViewById(R.id.patientAge);

        additionalInfomation.setText(requestJson.getString("additionalInfomation"));
        age.setText(requestJson.getString("age"));

        patientWhether=(TextView)findViewById(R.id.patientWhether);
        patientWhether.setText("病人预约就诊:"+requestJson.getString("reserved"));

        meetTreatResponse=(EditText)findViewById(R.id.meetTreatResponse);
        meetTreatResponse.setText(requestJson.getString("reservationResult"));

        text=(EditText)findViewById(R.id.treatResult);
        box=(CheckBox)findViewById(R.id.checkBox);
        send=(Button)findViewById(R.id.sendButton);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject sendJson=new JSONObject();
                sendJson.put("patientWalletDID",requestJson.getString("patientWalletDID"));
                sendJson.put("doctorWalletDID",requestJson.getString("doctorWalletDID"));
                sendJson.put("diaTime",requestJson.getString("diaTime"));

                sendJson.put("type","treatResult");
                sendJson.put("treatResult",text.getText().toString());
                if(box.isChecked()){
                    sendJson.put("adviceMeetTreat","true");
                }else{
                    sendJson.put("adviceMeetTreat","false");
                }
                final JSONObject joo=sendJson;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        DataOutputStream dos=((CommonInstance)getApplication()).getDataOutputStream();
                        DataInputStream dis=((CommonInstance)getApplication()).getDataInputStream();
                        try{
                            dos.writeUTF(joo.toString());
                            dos.flush();

                            JSONObject jo=JSON.parseObject(dis.readUTF());
                            if(jo.getInteger("status")==0){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(),"诊断成功",Toast.LENGTH_LONG).show();
                                    }
                                });
                            }else{
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(),"诊断失败",Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });

        Button sendMeetTreatButton=(Button)findViewById(R.id.sendMeetTreatButton);
        sendMeetTreatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject sendJson=requestJson;
                sendJson.put("type","meetTreatResult");
                sendJson.put("meetTreatResult",meetTreatResponse.getText().toString());
                final JSONObject joo=sendJson;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        DataOutputStream dos=((CommonInstance)getApplication()).getDataOutputStream();
                        DataInputStream dis=((CommonInstance)getApplication()).getDataInputStream();
                        try{
                            dos.writeUTF(joo.toString());
                            dos.flush();

                            JSONObject jo=JSON.parseObject(dis.readUTF());
                            if(jo.getInteger("status")==0){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(),"发送成功",Toast.LENGTH_LONG).show();
                                    }
                                });
                            }else{
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(),"发送失败",Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });



        dataItems= new ArrayAdapter<String>(this, R.layout.list_name);
        requestHistoricalDataList();

        list=(ListView)findViewById(R.id.dataListView);
        list.setAdapter(dataItems);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String s=(String) parent.getItemAtPosition(position);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(Treat.this, VisualChart.class);
                        JSONObject jo=new JSONObject();
                        jo.put("uploadTime",s);
                        jo.put("patientWalletDID",requestJson.getString("patientWalletDID"));
                        intent.putExtra("extra",jo.toString());/////////////
                        startActivity(intent);
                    }
                });
            }
        });
    }
    public void requestHistoricalDataList(){
        new Thread(new Runnable(){
            public void run(){
                boolean coned=false;
                try{
                    coned=((CommonInstance)getApplication()).getServer().isConnected();
                    coned=!((CommonInstance)getApplication()).getServer().isClosed();
                }catch(Exception e){
                    e.printStackTrace();
                }
                if(!coned){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),"未连接服务器", Toast.LENGTH_LONG).show();
                        }
                    });
                }else{
                    try{
                        JSONObject jsonobject=new JSONObject();
                        jsonobject.put("type", "doctorRequestHistoricalDataList");
                        jsonobject.put("patientWalletDID",requestJson.getString("patientWalletDID"));

                        DataOutputStream dos=((CommonInstance)getApplication()).getDataOutputStream();
                        dos.writeUTF(jsonobject.toString());
                        dos.flush();

                        dis=((CommonInstance)getApplication()).getDataInputStream();

                        final String s=dis.readUTF();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(),s, Toast.LENGTH_LONG).show();
                            }
                        });
                        dataListJsonArray= JSON.parseObject(s).getJSONArray("historicalDataList");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                for(int i=0;i<dataListJsonArray.size();i++){
                                    if(dataListJsonArray.getJSONObject(i).getString("fileName").compareTo(requestJson.getString("fromTime"))>0
                                            &&dataListJsonArray.getJSONObject(i).getString("fileName").compareTo(requestJson.getString("toTime"))<0){
                                        dataItems.add(dataListJsonArray.getJSONObject(i).getString("fileName"));
                                    }

                                }
                            }
                        });
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
