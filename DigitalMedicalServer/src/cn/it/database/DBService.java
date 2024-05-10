package cn.it.database;

import java.io.File;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.tools.Diagnostic;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
//import org.omg.CORBA.PUBLIC_MEMBER;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class DBService {
	private QueryRunner qr = new  QueryRunner();
	//注册的账号加入数据库
	public void registUser(String access,String password,String phoneNumber, 
			String type,String walletDID,String endpoint,String privateKey,String publicKey,String age,String doctorInfo) {
		
		try {
			qr.update(DBCPUtil.getConnection(),"insert into user(access,password,phoneNumber,type,walletDID,endpoint,privateKey,publicKey,age,doctorInfo)values(?,?,?,?,?,?,?,?,?,?)",
					access,password,phoneNumber,type,walletDID,endpoint,privateKey,publicKey,age,doctorInfo);
			

		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		
	}
	//登录某一access,返回walletDID和privateKey
	public String loginUser(String access,String password) {
		
		
		User user = null;
		try {
			 user =  qr.query(DBCPUtil.getConnection(), "select * from  user where access = ? and password = ?",new BeanHandler<User>(User.class),access,password);
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		if(user == null) return "error";
		else {
		Map<String,String> map = new HashMap<String,String>();
		map.put("walletDID", user.getWalletDID());
		map.put("privateKey", user.getPrivateKey());
		JSONObject jsonObject2 = JSONObject.fromObject(map);
		String string2 = jsonObject2.toString();
		System.out.println(string2);
		return string2;
		}
	}
	//查询个人信息
	public String selectUser(String access,String password) {
		
		User user = null;
		try {
			 user =  qr.query(DBCPUtil.getConnection(), "select * from  user where access = ? and password = ?",new BeanHandler<User>(User.class),access,password);
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		if(user == null) 
			return "error";
		else {
			Map<String,String> map = new HashMap<String,String>();
			map.put("phoneNumber", user.getPhoneNumber());
			map.put("type", user.getType());
			map.put("walletDID", user.getWalletDID());
			map.put("age", user.getAge());
			JSONObject jsonObject2 = JSONObject.fromObject(map);
			String string2 = jsonObject2.toString();
			System.out.println(string2);
			return string2;
		}
	}
	
	//保存文件,根据access和password判断真伪，获取walletDID和privateKey。
	//存储创建的POEID、文件hash值、文件名、文件路径，还有该文件的采集数据的起止时间。

	public String saveFile(String access ,String password,String POEID,String hash,String filePath,String startTime,String fileName, String endTime ) {
		
		
		User user = null;
		try {
			 user =  qr.query(DBCPUtil.getConnection(), "select * from  user where access = ?",new BeanHandler<User>(User.class),access);
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		//System.out.println(user.toString());
		if(user!=null && user.getPassword().equals(password)) {
			try {
				qr.update(DBCPUtil.getConnection(), "insert into heart(access,POEID,hash,filePath,startTime,fileName,endTime)values(?,?,?,?,?,?,?)"
						,access,POEID,hash,filePath,startTime,fileName,endTime);
			} catch (SQLException e) {
				
				e.printStackTrace();
			}
			return"Success";
		}
		else {
			return "Error";
		}
	}
	//查询历史心率数据,得到所有POEID、hash、文件名、路径，
	public String getHeartData(String access,String password) {
		
		User user = null;
		
		
	try {
		user =  qr.query(DBCPUtil.getConnection(), "select * from  user where access = ? ",new BeanHandler<User>(User.class),access);
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}	
	//System.out.println(user.toString());
		if(user!=null && password.equals(user.getPassword())) {
			
			List<Heart> list =null;
			try {
				 list = qr.query(DBCPUtil.getConnection(), "select * from heart where access = ?",new BeanListHandler<Heart>(Heart.class),user.getAccess());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			com.alibaba.fastjson.JSONArray jarray = (com.alibaba.fastjson.JSONArray) com.alibaba.fastjson.JSONArray.toJSON(list);
			String jString = jarray.toJSONString();
			return jString;
		}
		else {
			return "error";
		}
	}
	
public String getHeartData(String walletDID) {
		
		User user = null;
		
		
	try {
		user =  qr.query(DBCPUtil.getConnection(), "select * from  user where walletDID = ? ",new BeanHandler<User>(User.class),walletDID);
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}	
	//System.out.println(user.toString());
			
			List<Heart> list =null;
			try {
				 list = qr.query(DBCPUtil.getConnection(), "select * from heart where access = ?",new BeanListHandler<Heart>(Heart.class),user.getAccess());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			com.alibaba.fastjson.JSONArray jarray = (com.alibaba.fastjson.JSONArray) com.alibaba.fastjson.JSONArray.toJSON(list);
			String jString = jarray.toJSONString();
			return jString;
		
	}
public String getAccess(String walletDID) {
	
	User user = null;
	
	
try {
	user =  qr.query(DBCPUtil.getConnection(), "select * from  user where walletDID = ? ",new BeanHandler<User>(User.class),walletDID);
} catch (SQLException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}	
		return user.getAccess();
	
}
	
	//查询所有医生列表，获取所有医生的个人信息和WalletDID
	public String selectDoctor() {
		
		List<User> list = null;
		try {
			list = qr.query(DBCPUtil.getConnection(),"select * from user where type = 'doctor'",new BeanListHandler<User>(User.class));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JSONArray jsonArray =JSONArray.fromObject(list);
		for(int i = 0;i<jsonArray.size();i++) {
			JSONObject jObject = jsonArray.getJSONObject(i);
			jObject.discard("password");
			jObject.discard("endpoint");
			jObject.discard("privateKey");
			jObject.discard("publicKey");
		
		}
		String string1 =jsonArray.toString();
		return string1;
		
	}
	
	//2.请求过程：根据access和password判断真伪，然后得到walletDID和privateKey，
	public String requestForTreatment(String access ,String password  ) {
		
		
		User user = null;
		try {
			 user =  qr.query(DBCPUtil.getConnection(), "select * from  user where access = ? and password = ?",new BeanHandler<User>(User.class),access,password);
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		if(user == null) return "error";
		else {
		Map<String,String> map = new HashMap<String,String>();
		map.put("walletDID", user.getWalletDID());
		map.put("privateKey", user.getPrivateKey());
		JSONObject jsonObject2 = JSONObject.fromObject(map);
		String string2 = jsonObject2.toString();
		return string2;
		}
	}
	//查询某一用户的诊疗记录
	public String selectDiaRecords(String patientWalletDID) {
		
		List<DiagnosticData> list = null;
		try {
			list = qr.query(DBCPUtil.getConnection(), "select * from diagnosticdata where patientWalletDID =?",new BeanListHandler<DiagnosticData>(DiagnosticData.class),patientWalletDID);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		com.alibaba.fastjson.JSONArray jarray = (com.alibaba.fastjson.JSONArray) com.alibaba.fastjson.JSONArray.toJSON(list);
		String jString = jarray.toJSONString();
		return jString;
	}
	
	//医生获取等待医生诊断的心率数据的文件名，文件路径，起止时间
	public String selectHeartRate(String doctorWalletDID) {
		DiagnosticData dia = null;
		try {
			dia = qr.query(DBCPUtil.getConnection(), "select * from diagnosticdata where doctorWalletDID =? and checked ='false'",new BeanHandler<DiagnosticData>(DiagnosticData.class),doctorWalletDID);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Heart heart = null;
		try {
			heart = qr.query(DBCPUtil.getConnection(), "select * from heart where startTime =? and endTime =?",new BeanHandler<Heart>(Heart.class),dia.getFromTime(),dia.getToTime());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Map<String,String> map = new HashMap<String,String>();
		map.put("fromTime", dia.getFromTime());
		map.put("toTime", dia.getToTime());
		map.put("fileName",heart.getFileName());
		map.put("filePath", heart.getFilePath());
		JSONObject jsonObject2 = JSONObject.fromObject(map);
		String string2 = jsonObject2.toString();
		return string2;
		
	}
	
	
	
	
	//通过access和fileName查询poeid
		public String getPOEID(String access,String fileName) {

				
				Heart  heart = null;
				try {
					 heart = qr.query(DBCPUtil.getConnection(), "select * from heart where access = ? and fileName =? ",new BeanHandler<Heart>(Heart.class),access,fileName);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				JSONObject jObject = JSONObject.fromObject(heart);
				String poeid =jObject.getString("POEID");
				return poeid;
		
		}
		
		//得到预约列表
		public  String getReservation(String doctorWalleteDID) {

			List<DiagnosticData> list =null;
			try {
				 list = qr.query(DBCPUtil.getConnection(), "select * from diagnosticdata where doctorWalletDID = ? and checked = 'false'",new BeanListHandler<DiagnosticData>(DiagnosticData.class),doctorWalleteDID);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			com.alibaba.fastjson.JSONArray jarray = (com.alibaba.fastjson.JSONArray) com.alibaba.fastjson.JSONArray.toJSON(list);
			String jString = jarray.toJSONString();
			return jString;
		}
		//用户请求诊疗后存储诊疗记录
		public void saveDiaInfo(String patientWalletDID,String doctorWalletDID,String fromTime,String toTime,String transactionID,String treatFee,String additionalInfomation,String diaTime,String diaResult,String whetherToConsult,String reservationResult,String checked,String reserved) {
			
			
			try {
			qr.update(DBCPUtil.getConnection(),"insert into diagnosticdata(patientWalletDID,doctorWalletDID,fromTime,toTime,transactionID,treatFee,additionalInfomation,diaTime,diaResult,whetherToConsult,reservationResult,checked,reserved) values(?,?,?,?,?,?,?,?,?,?,?,?,?)"
						,patientWalletDID,doctorWalletDID,fromTime,toTime,transactionID,treatFee,additionalInfomation,diaTime,diaResult,whetherToConsult,reservationResult,checked,reserved);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//医生诊断 
		//后修改诊疗记录
		public void saveDiaInfo1(String patientWalletDID,String doctorWalletDID,String fromTime,String toTime,String transactionID,String treatFee,String additionalInfomation,String diaTime,String diaResult,String whetherToConsult,String reservationResult,String checked,String reserved) {
			
			
			try {
				qr.update(DBCPUtil.getConnection(),"update diagnosticdata set patientWalletDID = ?,doctorWalletDID=?,fromTime=?,toTime=?,transactionID=?,treatFee=?,additionalInfomation=?,diaTime=?,diaResult=?,whetherToConsult=?,reservationResult=?,checked=? ,reserved =?where fromTime =?"
						,patientWalletDID,doctorWalletDID,fromTime,toTime,transactionID,treatFee,additionalInfomation,diaTime,diaResult,whetherToConsult,reservationResult,checked,reserved,fromTime);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//根据patientwalletdid，doctorwalletdid，diatime，找出条目修改checked
		public void modifyChecked(String patientWalletDID,String doctorWalletDID,String diaTime,String checked) {
			
			try {
				qr.update(DBCPUtil.getConnection(),"update diagnosticdata set checked = ?where patientWalletDID=? and doctorWalletDID =? and diaTime =?"
						,checked,patientWalletDID,doctorWalletDID,diaTime);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		public void doctorTreat(String patientWalletDID,String doctorWalletDID,String diaTime,String result,String whetherToConsult,String checked) {
			
			try {
				qr.update(DBCPUtil.getConnection(),"update diagnosticdata set checked = ?,diaResult=?,whetherToConsult=?where patientWalletDID=? and doctorWalletDID =? and diaTime =?"
						,checked,result,whetherToConsult,patientWalletDID,doctorWalletDID,diaTime);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		public void doctorReserve(String patientWalletDID,String doctorWalletDID,String diaTime,String result,String checked) {
			
			try {
				qr.update(DBCPUtil.getConnection(),"update diagnosticdata set checked = ?,reservationResult=?where patientWalletDID=? and doctorWalletDID =? and diaTime =?"
						,checked,result,patientWalletDID,doctorWalletDID,diaTime);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	//根据patientwalletdid，doctorwalletdid，diatime，找出条目修改reserved
			public void modifyReserved(String patientWalletDID,String doctorWalletDID,String diaTime,String reserved) {
				
				try {
					qr.update(DBCPUtil.getConnection(),"update diagnosticdata set reserved = ?where patientWalletDID=? and doctorWalletDID =? and diaTime =?"
							,reserved,patientWalletDID,doctorWalletDID,diaTime);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}


}
