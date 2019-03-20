package com.visualteknologi.pegawaiapps.tampilpegawai;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.visualteknologi.pegawaiapps.R;
import com.visualteknologi.pegawaiapps.network.NetworkClient;
import com.visualteknologi.pegawaiapps.tambahpegawai.TambahPegawaiActivity;
import com.visualteknologi.pegawaiapps.tampilpegawai.adapter.AdapterTampilPegawai;
import com.visualteknologi.pegawaiapps.tampilpegawai.model.DataItem;
import com.visualteknologi.pegawaiapps.tampilpegawai.model.ResponseTampilPegawai;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TampilPegawaiActivity extends AppCompatActivity {

    //inisialisasi
    private RecyclerView rvDataPegawai;
    private List<DataItem> dataItemList;

    Button btnTambahPegawai, btnShowDialog;

    private long backPressedTime;
    private Toast backToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tampil_pegawai);

        //deklarasi
        rvDataPegawai = findViewById(R.id.rvListPegawai);
        dataItemList = new ArrayList<>();

        btnTambahPegawai = findViewById(R.id.btnTambahPegawai);
        btnShowDialog = findViewById(R.id.btnDialogCustom);

        //beri action untuk pindah ke form tambah
        btnTambahPegawai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            startActivity(new Intent(TampilPegawaiActivity.this, TambahPegawaiActivity.class));
            finish();
            }
        });

        //TODO: beri action klik untuk dialog
        btnShowDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: action custom dialog
                final Dialog dialog = new Dialog(TampilPegawaiActivity.this);
                dialog.setContentView(R.layout.custom_dialog);
                dialog.setTitle("Title");
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                //TODO: set komponen dialog
                TextView text = dialog.findViewById(R.id.tvText);
                text.setText("Dasar Tuman!!!");
                ImageView image = dialog.findViewById(R.id.ivDialog);
                image.setImageResource(R.drawable.tuman);

                Button dialogClose = dialog.findViewById(R.id.btnClose);

                //TODO: ketika button close di klik
                dialogClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        //menggunakan method gitListDataPegawai
        getListDataPegawai();
    }

    private void getListDataPegawai() {
        try {
            NetworkClient.service.tampil_pegawai().enqueue(new Callback<ResponseTampilPegawai>() {
                @Override
                public void onResponse(Call<ResponseTampilPegawai> call, Response<ResponseTampilPegawai> response) {
                    if(response.isSuccessful()){
                        dataItemList = response.body().getData();
                        AdapterTampilPegawai adapterTampilPegawai = new AdapterTampilPegawai(TampilPegawaiActivity.this, dataItemList);
                        rvDataPegawai.setLayoutManager(new LinearLayoutManager(TampilPegawaiActivity.this, LinearLayoutManager.VERTICAL, true));
                        //rvDataPegawai.setLayoutManager(new GridLayoutManager(TampilPegawaiActivity.this, 2));
                        //rvDataPegawai.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
                        rvDataPegawai.setAdapter(adapterTampilPegawai);
                    }
                }

                @Override
                public void onFailure(Call<ResponseTampilPegawai> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    /*public void onBackPressed() {
        if(backPressedTime + 2000 > System.currentTimeMillis()){
            backToast.cancel();
            super.onBackPressed();
            return;
        } else {
            backToast = Toast.makeText(this, "Takan 2 kali untuk keluar", Toast.LENGTH_SHORT);
            backToast.show();
        }

        backPressedTime = System.currentTimeMillis();
    }*/

    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("Yakin Mau Ketemu Pak Ndul?");
        builder.setPositiveButton("Mau Dong!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //TODO: jika opsi yes dipilih maka keluar aplikasi
                finish();
            }
        });

        builder.setNegativeButton("Tidak...", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //TODO: jika opsi No dipilih maka dialog akan keluar dan aplikasi akan berlanjut
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        //super.onBackPressed();
    }


}
