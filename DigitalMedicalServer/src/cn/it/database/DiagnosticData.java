package cn.it.database;

import java.util.Date;

public class DiagnosticData {
	
	private String patientWalletDID;
	private String doctorWalletDID;
	private String fromTime;
	private String toTime;
	private String transactionID;
	private String treatFee;
	private String additionalInfomation;
	private String diaTime;
	private String diaResult;
	private String whetherToConsult;
	private String reservationResult;
	private String checked;
	private String reserved;//预约的医生回应
	public String getChecked() {
		return checked;
	}
	public void setChecked(String checked) {
		this.checked = checked;
	}
	public String getPatientWalletDID() {
		return patientWalletDID;
	}
	public void setPatientWalletDID(String patientWalletDID) {
		this.patientWalletDID = patientWalletDID;
	}
	public String getDoctorWalletDID() {
		return doctorWalletDID;
	}
	public void setDoctorWalletDID(String doctorWalletDID) {
		this.doctorWalletDID = doctorWalletDID;
	}
	public String getFromTime() {
		return fromTime;
	}
	public void setFromTime(String fromTime) {
		this.fromTime = fromTime;
	}
	public String getToTime() {
		return toTime;
	}
	public void setToTime(String toTime) {
		this.toTime = toTime;
	}
	public String getTransactionID() {
		return transactionID;
	}
	public void setTransactionID(String transactionID) {
		this.transactionID = transactionID;
	}
	public String getTreatFee() {
		return treatFee;
	}
	public void setTreatFee(String treatFee) {
		this.treatFee = treatFee;
	}
	public String getAdditionalInfomation() {
		return additionalInfomation;
	}
	public void setAdditionalInfomation(String additionalInfomation) {
		this.additionalInfomation = additionalInfomation;
	}
	public String getDiaTime() {
		return diaTime;
	}
	public void setDiaTime(String diaTime) {
		this.diaTime = diaTime;
	}
	public String getDiaResult() {
		return diaResult;
	}
	public void setDiaResult(String diaResult) {
		this.diaResult = diaResult;
	}
	public String getWhetherToConsult() {
		return whetherToConsult;
	}
	public void setWhetherToConsult(String whetherToConsult) {
		this.whetherToConsult = whetherToConsult;
	}
	public String getReservationResult() {
		return reservationResult;
	}
	public void setReservationResult(String reservationResult) {
		this.reservationResult = reservationResult;
	}
	public String getReserved() {
		return reserved;
	}
	public void setReserved(String reserved) {
		this.reserved = reserved;
	}
	@Override
	public String toString() {
		return "DiagnosticData [patientWalletDID=" + patientWalletDID + ", doctorWalletDID=" + doctorWalletDID
				+ ", fromTime=" + fromTime + ", toTime=" + toTime + ", transactionID=" + transactionID + ", treatFee="
				+ treatFee + ", additionalInfomation=" + additionalInfomation + ", diaTime=" + diaTime + ", diaResult="
				+ diaResult + ", whetherToConsult=" + whetherToConsult + ", reservationResult=" + reservationResult
				+ "]";
	}
	
	//patientWalletDID,doctorWalletDID,fromTime,toTime,transactionID,treatFee,additionalInfomation,diaTime,diaResult,whetherToConsult,reservationResult
	
	
	
	
}

