import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.HashMap;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.it.database.DBService;

import java.text.SimpleDateFormat;

public class MainServer {
	
	static ServerSocket server;	
	WalletUtil walletUtil;
	public String tokensId="267995f0791aca694a4b8f41b0065b735ee5b68e2a6a6a67fef94961b9cf7cb5";
	logUtil log;
	DBService dbService;
	final int treatOnceMoney=5,orderMoney=20;
	public static void main(String[] args) {
		new MainServer().justForTest();
		new MainServer().start();
	}
	public void start() {
		dbService=new DBService();
		log=new logUtil();
		walletUtil=new WalletUtil();
		walletUtil.initWalletClient();
		try {
			server = new ServerSocket(8888);
			log.log("---Server established ,Waiting for clients ...");
			while (true) {
				Socket client = server.accept();
				log.log("---Get a connection :"+client.getRemoteSocketAddress());
				ClientHandlerThread t=new ClientHandlerThread(client);
				t.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	class ClientHandlerThread extends Thread {
		
		Socket client;
		DataOutputStream dos;
		DataInputStream  dis;
		String access=null,walletDID=null,privateKey=null,password=null;
		
		public ClientHandlerThread(Socket s) {
			this.client=s;
		}

		@Override
		public void run() {
			try {
				dis=new DataInputStream(new BufferedInputStream(client.getInputStream()));
				dos=new DataOutputStream(new BufferedOutputStream(client.getOutputStream()));
				while (true) {
					log.log("waiting for message ...");
					String readJsonString=dis.readUTF();
					log.log("json from "+client.getRemoteSocketAddress().toString()+":"+readJsonString);
					JSONObject getJson=JSON.parseObject(readJsonString);
					JSONObject sJson=new JSONObject();
					JSONObject jo=new JSONObject();
					switch(getJson.getString("type")) {
					case "registe":
						JSONObject response=walletUtil.registe(getJson.getString("access"), getJson.getString("password"));
						// save this response in database
						if(response.getInteger("ErrCode")==0) {
							jo.clear();
							jo=walletUtil.transferTokens("did:axn:105260b9-0e5b-4395-b556-936ac3c450ce",
									response.getJSONObject("Payload").getString("id"), 
									"267995f0791aca694a4b8f41b0065b735ee5b68e2a6a6a67fef94961b9cf7cb5",
									"yjrY68YMco7v/OooZSdvd8pzmqSzmL8uCz2/NSyDL5noEmNoxPHDtlm3VqLoOKTCVHCmEpw1s7N7lTOuiW2lMA==",100+"");
							if(jo.getInteger("ErrCode")==0) {
								String doctorInfo="";
								if(getJson.getString("userType").equals("doctor")) {
									doctorInfo=getJson.getString("doctorInfo");
								}
								dbService.registUser(getJson.getString("access"), getJson.getString("password"), 
										getJson.getString("phoneNumber"), getJson.getString("userType"),
										response.getJSONObject("Payload").getString("id"),
										response.getJSONObject("Payload").getString("endpoint"),
										response.getJSONObject("Payload").getJSONObject("key_pair").getString("private_key"),
										response.getJSONObject("Payload").getJSONObject("key_pair").getString("public_key"), 
										getJson.getString("age"),doctorInfo);
							}
							sendJson(jo);
						}else {
							jo.clear();
							jo.put("ErrCode", 1);
							sendJson(jo);
						}
						break;
					case "login":
						String s=dbService.loginUser(getJson.getString("access"), getJson.getString("password"));
						if(s.equals("error")) {
							sJson.put("status", 1);
							sendJson(sJson);
						}else {
							this.access=getJson.getString("access");
							this.password=getJson.getString("password");
							try {
								jo=JSON.parseObject(s);
								this.walletDID=jo.getString("walletDID");
								this.privateKey=jo.getString("privateKey");
							}catch(Exception e) {
								e.printStackTrace();
							}
							sJson.put("status", 0);
							sendJson(sJson);
							sJson.clear();
						}
						break;
					case "requestPersonalInfomation":
						String s2=dbService.selectUser(access, password);
						jo.clear();
						jo=JSON.parseObject(s2);
						//use walletdid to get balance amount from arxanchain
						sJson.put("walletDID", walletDID);
						sJson.put("walletBalance", queryWalletBalanceAmount(walletDID));
						sJson.put("phoneNumber", jo.getString("phoneNumber"));
						sJson.put("introduction", "张医生，安徽省XX医院XX科XX室");
						sJson.put("age", jo.getString("age"));//
						sendJson(sJson);
						sJson.clear();
						break;
					case "acquisitionData":
						String fileName=new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(new Date());
						File fileDir=new File("C:\\digitalMedicalServerFolder\\usersAcquisitionData\\"+access);
						receiveFile(fileDir.toString(),fileName);
						JSONObject POEResponse=walletUtil.createPOE(walletDID,privateKey,fileName);
						//save created POEId in database
						if(POEResponse.getInteger("ErrCode")==0) {
							Thread.sleep(2000);
							JSONObject response2=walletUtil.uploadFile(POEResponse.getJSONObject("Payload").getString("id"), fileDir+"\\"+fileName);
							sendJson(response2);
							if(response2.getInteger("ErrCode")==0) {
								DataInputStream fileDis=new DataInputStream(new BufferedInputStream(new FileInputStream(new File(fileDir+"\\"+fileName))));
								String s3=new String(fileDis.readAllBytes());
								fileDis.close();
								JSONObject fileJson=JSON.parseObject(s3);
								Thread.sleep(2000);
								JSONObject poeInfo=walletUtil.queryPOE(POEResponse.getJSONObject("Payload").getString("id"));
								dbService.saveFile(access, password, 
										POEResponse.getJSONObject("Payload").getString("id"),
										poeInfo.getJSONObject("Payload").getJSONObject("offchain_metadata").getString("contentHash"),
										fileDir+"\\"+fileName, fileJson.getString("heartRateStartTime"),
										fileName, fileJson.getString("heartRateLastTime"));
							}
						}
						
						break;
					case "requestHistoricalDataList":
						
						sJson.put("historicalDataList", JSON.parseArray(dbService.getHeartData(access, password)));
						sendJson(sJson);
						sJson.clear();
						break;
					case "requestHistoricalDataInfomation":
						String POEID=dbService.getPOEID(access, getJson.getString("uploadTime"));
						//use upload time to get POEId from databse
						//getJson.getString("uploadTime");
						sendJson(walletUtil.queryPOE(POEID));
						
						break;
					case "requestHistoricalData":
						//use walletDID and uploadTime(to this server) to get POEId from databse
						//queryPOE(POEId); get hash
						//hash native file to judge whether true
						//if (true) send it to client ,repeat
						File sendf=new File("C:\\digitalMedicalServerFolder\\usersAcquisitionData\\"+access+"\\"+getJson.getString("uploadTime"));
						DataInputStream fileDis=new DataInputStream(new BufferedInputStream(new FileInputStream(sendf)));
						int len=(int)sendf.length();
						dos.writeInt(len);
						dos.flush();
						byte[] bytes=new byte[len];
						fileDis.read(bytes);
						fileDis.close();
						dos.write(bytes, 0, len);
						dos.flush();
						log.log("send a dataFile:"+sendf.toString());
						break;
						
					case "requestDoctorList":
						sJson.put("doctorList", JSON.parseArray(dbService.selectDoctor()));
						sJson.put("treatOnceMoney", treatOnceMoney+"");
						sendJson(sJson);
						sJson.clear();
						break;
						
						
					case "requestTreat":
						
						JSONObject jo2=walletUtil.transferTokens(walletDID,getJson.getString("doctorWalletDID"), 
								tokensId, privateKey, treatOnceMoney+"");
						if(jo2.getInteger("ErrCode")==0) {
							dbService.saveDiaInfo(walletDID, getJson.getString("doctorWalletDID"),
									getJson.getString("fromTime"), getJson.getString("toTime"),
									"", treatOnceMoney+"", getJson.getString("additionalInfomation"), 
									new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(new Date()), "", "", "", "false","false");
							
							sJson.put("status",0);
							sendJson(sJson);
							sJson.clear();
						}else {
							sJson.put("status",1);
							sendJson(sJson);
							sJson.clear();
						}
						
						break;
					case "requestTreatRecord":
						//use access and password to get POEId from databse
						//queryPOE(POEId); get hash
						//hash native file to judge whether true
						//if (true) send it to client ,repeat
						
						sJson.put("treatRecord", JSON.parseArray(dbService.selectDiaRecords(walletDID)));
						sendJson(sJson);
						sJson.clear();
						break;
					case "orderMeetTreat":
						JSONObject jo3=walletUtil.transferTokens(walletDID,getJson.getString("doctorWalletDID"), 
								tokensId, privateKey, orderMoney+"");
						if(jo3.getInteger("ErrCode")==0) {
							dbService.modifyReserved(walletDID, getJson.getString("doctorWalletDID"),getJson.getString("diaTime"), "true");
							dbService.modifyChecked(walletDID,getJson.getString("doctorWalletDID"),  
									getJson.getString("diaTime"), "false");
							sJson.put("status", 0);
							sendJson(sJson);
							sJson.clear();
						}else {
							sJson.put("status", 1);
							sendJson(sJson);
							sJson.clear();
						}
						break;
					case "requestTreatList":
						sJson.put("treatList", JSON.parseArray(dbService.getReservation(walletDID)));
						sendJson(sJson);
						sJson.clear();
						break;
					case "treatResult":
						//dbService.modifyChecked(getJson.getString("patientWalletDID"), walletDID, getJson.getString("diaTime"), "true");
						dbService.doctorTreat(getJson.getString("patientWalletDID"), walletDID, getJson.getString("diaTime"),
								getJson.getString("treatResult"),getJson.getString("adviceMeetTreat"), "true");
						sJson.put("status", 0);
						sendJson(sJson);
						sJson.clear();
						break;
					case "meetTreatResult":
						dbService.doctorReserve(getJson.getString("patientWalletDID"), walletDID, 
								getJson.getString("diaTime"), getJson.getString("meetTreatResult"),"true");
						sJson.put("status", 0);
						sendJson(sJson);
						sJson.clear();
						break;
					case "doctorRequestHistoricalDataList":
						sJson.put("historicalDataList", JSON.parseArray(dbService.getHeartData(getJson.getString("patientWalletDID"))));
						sendJson(sJson);
						sJson.clear();
						break;
					case "doctorRequestHistoricalData":
						//use walletDID and uploadTime(to this server) to get POEId from databse
						//queryPOE(POEId); get hash
						//hash native file to judge whether true
						//if (true) send it to client ,repeat
						File sendf2=new File("C:\\digitalMedicalServerFolder\\usersAcquisitionData\\"+
								dbService.getAccess(getJson.getString("patientWalletDID"))+"\\"+getJson.getString("uploadTime"));
						DataInputStream fileDis2=new DataInputStream(new BufferedInputStream(new FileInputStream(sendf2)));
						int len2=(int)sendf2.length();
						dos.writeInt(len2);
						dos.flush();
						byte[] bytes2=new byte[len2];
						fileDis2.read(bytes2);
						fileDis2.close();
						dos.write(bytes2, 0, len2);
						dos.flush();
						log.log("send a dataFile:"+sendf2.toString());
						break;
						
					default:break;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}finally {
				close();
				log.log("Close a Client("+client.getRemoteSocketAddress().toString()+") !");
			}
		}
		
		public void sendJson (JSONObject jo) {
			try {
				dos.writeUTF(jo.toString());
                dos.flush();
                log.log("Send a Json :"+jo.toString());
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		private void close() {
			if (dos != null) {
				try {
					dos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (dis != null) {
				try {
					dis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (client != null) {
				try {
					client.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		public void receiveFile(String fileDi,String fileName) {
			try {
				int len=dis.readInt();
				log.log("start receiving File ,length:"+len);
				File fileDir=new File(fileDi);
				if(!fileDir.exists()) {
					fileDir.mkdirs();
				}
				//moveFile(f.getAbsoluteFile(),new File(fileDir+"\\"+fileName));
				DataOutputStream fdos=new DataOutputStream(new BufferedOutputStream(new FileOutputStream(new File(fileDir+"\\"+fileName))));
				
				int size=1024;
				byte[] bytes=new byte[size];
				int read,sum=0;
				while(true) {
					read=dis.read(bytes,0,size);
					sum+=read;
					fdos.write(bytes,0,read);
					if(sum>=len) 
						break;
					fdos.flush();
				}
				fdos.close();
				log.log("Receive successfully :"+fileDir+"\\"+fileName);
				
				
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		
	}// Thread end
	

	
	public int queryWalletBalanceAmount(String walletId) {
		JSONObject jo=walletUtil.queryWalletBalance(walletId);
		int amount=jo.getJSONObject("Payload").getJSONObject("colored_tokens").getJSONObject("267995f0791aca694a4b8f41b0065b735ee5b68e2a6a6a67fef94961b9cf7cb5").getInteger("amount");
		return amount;
	}
	
	
	public void justForTest() {
		try{
			//System.out.println(new DBService().getHeartData("did:axn:88b8f1fd-3f3e-4a8c-87ac-335ad7fe01a5"));
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
}
