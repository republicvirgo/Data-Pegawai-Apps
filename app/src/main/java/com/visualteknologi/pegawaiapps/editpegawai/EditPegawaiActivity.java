package com.visualteknologi.pegawaiapps.editpegawai;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.visualteknologi.pegawaiapps.R;
import com.visualteknologi.pegawaiapps.editpegawai.model.ResponseEditPegawai;
import com.visualteknologi.pegawaiapps.network.NetworkClient;
import com.visualteknologi.pegawaiapps.tampilpegawai.TampilPegawaiActivity;
import com.visualteknologi.pegawaiapps.tampilpegawai.model.DataItem;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditPegawaiActivity extends AppCompatActivity {

    //TODO: Inisaialisasi
    EditText etEditNama, etEditEmail, etEditNoHp, etEditAlamat;
    Button btnConfirmEdit;

    DataItem dataItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pegawai);

        //TODO: Deklarasi
        etEditNama = findViewById(R.id.etEditNamaPegawai);
        etEditEmail = findViewById(R.id.etEditEmailPegawai);
        etEditNoHp = findViewById(R.id.etEditNoHpPegawai);
        etEditAlamat = findViewById(R.id.etEditAlamatPegawai);

        btnConfirmEdit = findViewById(R.id.btnEditData);

        dataItem = (DataItem) getIntent().getSerializableExtra("data_pegawai");

        etEditNama.setText(dataItem.getNamaPegawai());
        etEditEmail.setText(dataItem.getEmailPegawai());
        etEditNoHp.setText(dataItem.getNoHpPegawai());
        etEditAlamat.setText(dataItem.getAlamatPegawai());

        btnConfirmEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionEdit();
            }
        });
    }

    private void actionEdit(){
        String idPegawai = dataItem.getIdPegawai();
        String namaPegawai = etEditNama.getText().toString();
        String emailPegawai = etEditEmail.getText().toString();
        String noHpPegawai = etEditNoHp.getText().toString();
        String alamatPegawai = etEditAlamat.getText().toString();

        if(TextUtils.isEmpty(namaPegawai)){
            etEditNama.setError("Nama tidak boleh kosong");
            etEditNama.requestFocus();
        } else if(TextUtils.isEmpty(emailPegawai)){
            etEditEmail.setError("Email tidak boleh kosong");
            etEditEmail.requestFocus();
        } else if(TextUtils.isEmpty(noHpPegawai)){
            etEditNoHp.setError("No HP tidak boleh kosong");
            etEditNoHp.requestFocus();
        } else if(TextUtils.isEmpty(alamatPegawai)){
            etEditAlamat.setError("Alamat tidak boleh kosong");
            etEditAlamat.requestFocus();
        } else {
            NetworkClient.service.edit_pegawai(idPegawai, namaPegawai, emailPegawai, noHpPegawai, alamatPegawai)
                .enqueue(new Callback<ResponseEditPegawai>() {
                    @Override
                    public void onResponse(Call<ResponseEditPegawai> call, Response<ResponseEditPegawai> response) {
                        if(response.isSuccessful()){
                            Boolean status = response.body().isStatus();
                            String pesan = response.body().getPesan();

                            if(status){
                                startActivity(new Intent(EditPegawaiActivity.this, TampilPegawaiActivity.class));
                                Toast.makeText(EditPegawaiActivity.this, pesan, Toast.LENGTH_SHORT).show();

                                finish();
                            } else {
                                Toast.makeText(EditPegawaiActivity.this, pesan, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseEditPegawai> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
        }
    }
}
