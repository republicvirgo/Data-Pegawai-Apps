package com.visualteknologi.pegawaiapps.network;

import com.visualteknologi.pegawaiapps.editpegawai.model.ResponseEditPegawai;
import com.visualteknologi.pegawaiapps.tambahpegawai.model.ResponseTambahPegawai;
import com.visualteknologi.pegawaiapps.tampilpegawai.model.ResponseHapusPegawai;
import com.visualteknologi.pegawaiapps.tampilpegawai.model.ResponseTampilPegawai;

import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
//import retrofit2.http.Multipart;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface RestApi {
    //@Multipart //ada file gambar
    @FormUrlEncoded
    @POST("tambah_pegawai")
    Call<ResponseTambahPegawai> tambah_pegawai(
        @Field("nama_pegawai") String namaPegawai,
        @Field("email_pegawai") String emailPegawai,
        @Field("no_hp_pegawai") String noHpPegawai,
        @Field("alamat_pegawai") String alamatPegawai
    );

    @Multipart
    @POST("upload_foto")
    Call<ResponseTambahPegawai> tambah_pegawai_foto(
        @Part MultipartBody.Part foto,
        @Part ("nama_pegawai")RequestBody namaPegawai,
        @Part ("email_pegawai")RequestBody emailPegawai,
        @Part ("no_hp_pegawai")RequestBody noHpPegawai,
        @Part ("alamat_pegawai")RequestBody alamatPegawai,
        @Part ("tanggal_ditambahkan")RequestBody tanggalDitambahkan
    );

    @GET("tampil_pegawai")
    Call<ResponseTampilPegawai> tampil_pegawai();

    @FormUrlEncoded
    @POST("hapus_pegawai")
    Call<ResponseHapusPegawai> hapus_pegawai (
        @Field("id_pegawai") String idPegawai
    );

    @FormUrlEncoded
    @POST("edit_pegawai")
    Call<ResponseEditPegawai> edit_pegawai (
        @Field("id_pegawai") String idPegawai,
        @Field("nama_pegawai") String namaPegawai,
        @Field("email_pegawai") String emailPegawai,
        @Field("no_hp_pegawai") String noHpPegawai,
        @Field("alamat_pegawai") String alamatPegawai
    );

}
