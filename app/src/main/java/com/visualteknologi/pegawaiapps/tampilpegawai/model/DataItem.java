package com.visualteknologi.pegawaiapps.tampilpegawai.model;

import com.google.gson.annotations.SerializedName;

public class DataItem{

	@SerializedName("no_hp_pegawai")
	private String noHpPegawai;

	@SerializedName("nama_pegawai")
	private String namaPegawai;

	@SerializedName("alamat_pegawai")
	private String alamatPegawai;

	@SerializedName("email_pegawai")
	private String emailPegawai;

	@SerializedName("id_pegawai")
	private String idPegawai;

	public void setNoHpPegawai(String noHpPegawai){
		this.noHpPegawai = noHpPegawai;
	}

	public String getNoHpPegawai(){
		return noHpPegawai;
	}

	public void setNamaPegawai(String namaPegawai){
		this.namaPegawai = namaPegawai;
	}

	public String getNamaPegawai(){
		return namaPegawai;
	}

	public void setAlamatPegawai(String alamatPegawai){
		this.alamatPegawai = alamatPegawai;
	}

	public String getAlamatPegawai(){
		return alamatPegawai;
	}

	public void setEmailPegawai(String emailPegawai){
		this.emailPegawai = emailPegawai;
	}

	public String getEmailPegawai(){
		return emailPegawai;
	}

	public void setIdPegawai(String idPegawai){
		this.idPegawai = idPegawai;
	}

	public String getIdPegawai(){
		return idPegawai;
	}

	@Override
 	public String toString(){
		return 
			"DataItem{" + 
			"no_hp_pegawai = '" + noHpPegawai + '\'' + 
			",nama_pegawai = '" + namaPegawai + '\'' + 
			",alamat_pegawai = '" + alamatPegawai + '\'' + 
			",email_pegawai = '" + emailPegawai + '\'' + 
			",id_pegawai = '" + idPegawai + '\'' + 
			"}";
		}
}