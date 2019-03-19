package com.visualteknologi.pegawaiapps.detailpegawai;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.visualteknologi.pegawaiapps.R;
import com.visualteknologi.pegawaiapps.tampilpegawai.model.DataItem;

public class DetailPegawaiActivity extends AppCompatActivity {
    //inisialisasi
    TextView tvDetailNama, tvDetailEmail, tvDetailNoHp, tvDetailAlamat;
    DataItem dataItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pegawai);

        //deklarasi
        tvDetailNama = findViewById(R.id.tvDetailNama);
        tvDetailEmail = findViewById(R.id.tvDetailEmail);
        tvDetailNoHp = findViewById(R.id.tvDetailNoHp);
        tvDetailAlamat = findViewById(R.id.tvDetailAlamat);

        //tampung data di string

        //cara 1
        /*String namaPegawai = getIntent().getStringExtra("nama_pegawai");
        String emailPegawai = getIntent().getStringExtra("email_pegawai");
        String noHpPegawai = getIntent().getStringExtra("no_hp_pegawai");
        String alamatPegawai = getIntent().getStringExtra("alamat_pegawai");

        tvDetailNama.setText(namaPegawai);
        tvDetailEmail.setText(emailPegawai);
        tvDetailNoHp.setText(noHpPegawai);
        tvDetailAlamat.setText(alamatPegawai);*/

        //cara 2
        //(DataItem) diambil dari nama class DataItem di Model
        dataItem = (DataItem) getIntent().getSerializableExtra("data_pegawai");

        tvDetailNama.setText(dataItem.getNamaPegawai());
        tvDetailEmail.setText(dataItem.getEmailPegawai());
        tvDetailNoHp.setText(dataItem.getNoHpPegawai());
        tvDetailAlamat.setText(dataItem.getAlamatPegawai());
    }
}
