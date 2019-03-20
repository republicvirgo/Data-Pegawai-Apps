package com.visualteknologi.pegawaiapps.tampilpegawai.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.visualteknologi.pegawaiapps.R;
import com.visualteknologi.pegawaiapps.detailpegawai.DetailPegawaiActivity;
import com.visualteknologi.pegawaiapps.editpegawai.EditPegawaiActivity;
import com.visualteknologi.pegawaiapps.network.NetworkClient;
import com.visualteknologi.pegawaiapps.tampilpegawai.TampilPegawaiActivity;
import com.visualteknologi.pegawaiapps.tampilpegawai.model.DataItem;
import com.visualteknologi.pegawaiapps.tampilpegawai.model.ResponseHapusPegawai;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterTampilPegawai extends RecyclerView.Adapter<AdapterTampilPegawai.MyHolder> {

    public AdapterTampilPegawai(Context context, List<DataItem> dataItemList) {
        this.context = context;
        this.dataItemList = dataItemList;
    }

    Context context;
    List<DataItem> dataItemList;

    @NonNull
    @Override
    public AdapterTampilPegawai.MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list_pegawai, viewGroup, false);
        return new MyHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterTampilPegawai.MyHolder myHolder, final int position) {
        final DataItem dataItem = dataItemList.get(position);

        //myHolder.idPegawai.setText(dataItem.getIdPegawai());
        myHolder.namaPegawai.setText(dataItem.getNamaPegawai());
        myHolder.emailPegawai.setText(dataItem.getEmailPegawai());
        myHolder.noHpPegawai.setText(dataItem.getNoHpPegawai());
        myHolder.alamatPegawai.setText(dataItem.getAlamatPegawai());

        //TODO : ketika hapus di klik
        myHolder.btnHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idPegawai = dataItem.getIdPegawai();
                NetworkClient.service.hapus_pegawai(idPegawai).enqueue(new Callback<ResponseHapusPegawai>() {
                    @Override
                    public void onResponse(Call<ResponseHapusPegawai> call, Response<ResponseHapusPegawai> response) {
                        if(response.isSuccessful()){
                            Boolean status = response.body().isStatus();
                            String pesan = response.body().getPesan();

                            if(status){
                                Intent intent = new Intent(myHolder.itemView.getContext(), TampilPegawaiActivity.class);
                                myHolder.itemView.getContext().startActivity(intent);
                                ((Activity)context).finish();

                                Toast.makeText(context, pesan, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, pesan, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseHapusPegawai> call, Throwable t) {

                    }
                });
            }
        });

        //TODO : ketika edit di klik
        myHolder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditPegawaiActivity.class);
                intent.putExtra("data_pegawai", dataItem);

                myHolder.itemView.getContext().startActivity(intent);
            }
        });

        //TODO : ketika item di klik
        myHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(myHolder.itemView.getContext(), DetailPegawaiActivity.class);

                // cara 1
                /*intent.putExtra("id_pegawai", dataItemList.get(position).getIdPegawai());
                intent.putExtra("nama_pegawai", dataItemList.get(position).getNamaPegawai());
                intent.putExtra("email_pegawai", dataItemList.get(position).getEmailPegawai());
                intent.putExtra("no_hp_pegawai", dataItemList.get(position).getNoHpPegawai());
                intent.putExtra("alamat_pegawai", dataItemList.get(position).getAlamatPegawai());*/

                // cara 2
                intent.putExtra("data_pegawai", dataItem);

                myHolder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataItemList.size();
    }

    //nyambung dari item dari layout item_list_data
    public class MyHolder extends RecyclerView.ViewHolder {
        public TextView namaPegawai, emailPegawai, noHpPegawai, alamatPegawai;

        ImageView btnHapus, btnEdit;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            namaPegawai = itemView.findViewById(R.id.tvNamaPegawai);
            emailPegawai = itemView.findViewById(R.id.tvEmailPegawai);
            noHpPegawai = itemView.findViewById(R.id.tvNoHpPegawai);
            alamatPegawai = itemView.findViewById(R.id.tvAlamatPegawai);

            btnHapus = itemView.findViewById(R.id.btnHapus);
            btnEdit = itemView.findViewById(R.id.btnEdit);
        }
    }
}
