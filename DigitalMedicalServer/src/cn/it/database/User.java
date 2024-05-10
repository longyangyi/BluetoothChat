package cn.it.database;


import java.util.Date;

/**
 * 定义用户类
 */
/**
 * @author yzhao
 *
 */
public class User {
	private String access;
	private String password;
	private String phoneNumber;
	private String type;
	private String walletDID;
	private String endpoint;
	private String privateKey;
	private String publicKey;
	private String age;
	private String doctorInfo;
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getAccess() {
		return access;
	}
	public void setAccess(String access) {
		this.access = access;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getWalletDID() {
		return walletDID;
	}
	public void setWalletDID(String walletDID) {
		this.walletDID = walletDID;
	}
	public String getEndpoint() {
		return endpoint;
	}
	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}
	public String getPrivateKey() {
		return privateKey;
	}
	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}
	public String getPublicKey() {
		return publicKey;
	}
	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}
	public String getDoctorInfo() {
		return doctorInfo;
	}
	public void setDoctorInfo(String doctorInfo) {
		this.doctorInfo = doctorInfo;
	}
	@Override
	public String toString() {
		return "User [access=" + access + ", password=" + password + ", phoneNumber=" + phoneNumber + ", type=" + type
				+ ", walletDID=" + walletDID + ", endpoint=" + endpoint + ", privateKey=" + privateKey + ", publicKey="
				+ publicKey + ", age=" + age + ", doctorInfo=" + doctorInfo + "]";
	}
	
	

}
