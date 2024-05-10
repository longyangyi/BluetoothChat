package cn.it.database;

import java.sql.Timestamp;
import java.util.Date;

public class Heart {
		
		
	private String access;
	private String POEID;
	private String hash;
	private String filePath;
	private String startTime;
	private String endTime;
	private String fileName;
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getAccess() {
		return access;
	}
	public void setAccess(String access) {
		this.access = access;
	}
	public String getPOEID() {
		return POEID;
	}
	public void setPOEID(String pOEID) {
		POEID = pOEID;
	}
	public String getHash() {
		return hash;
	}
	public void setHash(String hash) {
		this.hash = hash;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	@Override
	public String toString() {
		return "Heart [access=" + access + ", POEID=" + POEID + ", hash=" + hash + ", filePath=" + filePath
				+ ", startTime=" + startTime + ", endTime=" + endTime + "]";
	}
	
		
		
		
		
	

}
