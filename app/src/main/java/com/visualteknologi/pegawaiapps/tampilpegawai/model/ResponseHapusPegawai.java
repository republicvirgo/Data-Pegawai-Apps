package com.visualteknologi.pegawaiapps.tampilpegawai.model;

import com.google.gson.annotations.SerializedName;

public class ResponseHapusPegawai{

	@SerializedName("pesan")
	private String pesan;

	@SerializedName("status")
	private boolean status;

	public void setPesan(String pesan){
		this.pesan = pesan;
	}

	public String getPesan(){
		return pesan;
	}

	public void setStatus(boolean status){
		this.status = status;
	}

	public boolean isStatus(){
		return status;
	}

	@Override
 	public String toString(){
		return 
			"ResponseHapusPegawai{" + 
			"pesan = '" + pesan + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}