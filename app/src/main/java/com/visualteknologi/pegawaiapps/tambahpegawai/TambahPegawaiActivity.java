package com.visualteknologi.pegawaiapps.tambahpegawai;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.visualteknologi.pegawaiapps.R;
import com.visualteknologi.pegawaiapps.tambahpegawai.model.ResponseTambahPegawai;
import com.visualteknologi.pegawaiapps.network.NetworkClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TambahPegawaiActivity extends AppCompatActivity {
    //inisialisasi
    EditText etNama, etEmail, etNoHp, etAlamat;
    Button btnInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_pegawai);

        //deklarasi widget view
        etNama = findViewById(R.id.etNamaPegawai);
        etEmail = findViewById(R.id.etEmailPegawai);
        etNoHp = findViewById(R.id.etNoHpPegawai);
        etAlamat = findViewById(R.id.etAlamatPegawai);

        btnInput = findViewById(R.id.btnInputPegawai);

        btnInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionInputPegawai();
            }
        });
    }

    private void actionInputPegawai() {
        String namaPegawai = etNama.getText().toString();
        String emailPegawai = etEmail.getText().toString();
        String noHpPegawai = etNoHp.getText().toString();
        String alamatPegawai = etAlamat.getText().toString();

        if (TextUtils.isEmpty(namaPegawai)) {
            etNama.setError("Nama Pegawai tidak boleh kosong");
            etNama.requestFocus();

        } else if (TextUtils.isEmpty(emailPegawai)) {
            etNama.setError("Email Pegawai tidak boleh kosong");
            etNama.requestFocus();

        } else if (TextUtils.isEmpty(noHpPegawai)) {
            etNama.setError("No HP Pegawai tidak boleh kosong");
            etNama.requestFocus();

        } else if (TextUtils.isEmpty(alamatPegawai)) {
            etNama.setError("Alamat Pegawai tidak boleh kosong");
            etNama.requestFocus();
        } else {
            NetworkClient.service.tambah_pegawai(namaPegawai, emailPegawai, noHpPegawai, alamatPegawai)
                    .enqueue(new Callback<ResponseTambahPegawai>() {
                        @Override
                        public void onResponse(Call<ResponseTambahPegawai> call, Response<ResponseTambahPegawai> response) {
                            if(response.isSuccessful()){
                                Boolean status = response.body().isStatus();
                                String pesan = response.body().getPesan();

                                if(status){
                                    startActivity(new Intent(TambahPegawaiActivity.this, TambahPegawaiActivity.class));
                                    Toast.makeText(TambahPegawaiActivity.this, pesan, Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(TambahPegawaiActivity.this, pesan, Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseTambahPegawai> call, Throwable t) {

                            t.printStackTrace();

                        }
                    });

        }
    }
}
