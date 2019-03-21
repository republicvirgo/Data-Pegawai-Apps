package com.visualteknologi.pegawaiapps.tambahpegawai;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.visualteknologi.pegawaiapps.MainActivity;
import com.visualteknologi.pegawaiapps.R;
import com.visualteknologi.pegawaiapps.tambahpegawai.model.ResponseTambahPegawai;
import com.visualteknologi.pegawaiapps.network.NetworkClient;
import com.visualteknologi.pegawaiapps.tampilpegawai.TampilPegawaiActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TambahPegawaiActivity extends AppCompatActivity {

    //inisialisasi widget view
    EditText etNama, etEmail, etNoHp, etAlamat;
    Button btnInput;
    ImageView ivCamera;
    TextView tvNamaGambar;

    DatePickerDialog datePickerDialog;
    SimpleDateFormat simpleDateFormat;
    EditText etCalendar;


    String mediaPath;
    String fileName;
    Dialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_pegawai);

//        dialog = new Dialog(this);

        //deklarasi widget view
        etNama = findViewById(R.id.etNamaPegawai);
        etEmail = findViewById(R.id.etEmailPegawai);
        etNoHp = findViewById(R.id.etNoHpPegawai);
        etAlamat = findViewById(R.id.etAlamatPegawai);
        btnInput = findViewById(R.id.btnInputPegawai);
        ivCamera = findViewById(R.id.ivBtnGambar);
        tvNamaGambar = findViewById(R.id.tvNamaGambar);

        etCalendar = findViewById(R.id.etCalendar);

        btnInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionInputPegawai();
            }
        });

        /*Kita menggunakan format tanggal dd-MM-yyyy
        yang nantinya ketika tanggal kita ditampilkan akan terlihat seperti berikut
        20-03-2019
*/
        simpleDateFormat = new SimpleDateFormat("dd-MMMM-yyyy", new Locale("id", "ID"));
        etCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog();
            }
        });

        ivCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                actionDialogCapture(0);

            }
        });
    }

    private void actionDialogCapture(final int i) {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_dialog);

        Button btnTakePicture = dialog.findViewById(R.id.btnClose);
        Button btnGallery = dialog.findViewById(R.id.btnGallery);
        btnTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, i + 2);
            }
        });
        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, i + 1);
            }
        });
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        dialog.dismiss();
        if (requestCode == 0 && resultCode == RESULT_OK) {

            Uri selectImage = data.getData();
            String[] filePatchColumn = new String[]{MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectImage, filePatchColumn, null, null, null);
            int columnIndext = cursor.getColumnIndex(filePatchColumn[0]);
            mediaPath = cursor.getString(columnIndext);
            tvNamaGambar.setText(selectImage.getPath());
            cursor.close();
        } else if (requestCode == 2) {
            try {
                Date d = new Date();
                CharSequence formatTanggal = DateFormat.format("dd-MM-yyyy", d.getTime());
                String tanggalSekarang = String.valueOf(formatTanggal);

                Bitmap datas = (Bitmap) data.getExtras().get("data");
                Image(datas, "camera-" + tanggalSekarang);

            } catch (NullPointerException e) {

            }
        }
    }

    //TODO : Methode Convert gambar
    private void Image(Bitmap bitmap, String name) {
        File fileDir = getFilesDir();
        File imageFile = new File(fileDir, name + ".jpg");
        mediaPath = imageFile.getPath();
        fileName = imageFile.getName();
        //tvNamaGambar.setText(mediaPath);
        tvNamaGambar.setText(fileName);
        try {
            FileOutputStream outputStream = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }


    private void showDateDialog() {
        //TODO : mendapatkan tanggal sekarang
        final Calendar calendar = Calendar.getInstance();

        //TODO : datepicker dialog
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar calendarDate = Calendar.getInstance();
                calendarDate.set(year, month, dayOfMonth);

                etCalendar.setText(simpleDateFormat.format(calendarDate.getTime()));
            }
        }, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();

    }

    private void actionInputPegawai() {
        File gambar = new File(mediaPath);
        RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), gambar);
        MultipartBody.Part filePhoto = MultipartBody.Part.createFormData("userfile", gambar.getName(), requestBody);

        RequestBody namaPegawai = MultipartBody.create(MediaType.parse("text/plain"), etNama.getText().toString());
        RequestBody emailPegawai = MultipartBody.create(MediaType.parse("text/plain"), etEmail.getText().toString());
        RequestBody noHpPegawai = MultipartBody.create(MediaType.parse("text/plain"), etNoHp.getText().toString());
        RequestBody alamatPegawai = MultipartBody.create(MediaType.parse("text/plain"), etAlamat.getText().toString());
        RequestBody tanggalTambah = MultipartBody.create(MediaType.parse("text/plain"), etCalendar.getText().toString());

        if (namaPegawai == null) {
            etNama.setError("Nama Pegawai tidak boleh kosong!");
            etNama.requestFocus();
        } else if (emailPegawai == null) {
            etEmail.setError("Email Tidak Boleh Kosong");
            etEmail.requestFocus();
        } else if (noHpPegawai == null) {
            etNoHp.setError("No Hp Tidak Boleh Kosong");
            etNoHp.requestFocus();
        } else if (noHpPegawai == null) {
            etAlamat.setError("Alamat Tidak boleh kosong");
            etAlamat.requestFocus();
        } else {
            NetworkClient.service.tambah_pegawai_foto(filePhoto, namaPegawai, emailPegawai, noHpPegawai, alamatPegawai, tanggalTambah)
                .enqueue(new Callback<ResponseTambahPegawai>() {
                    @Override
                    public void onResponse(Call<ResponseTambahPegawai> call, Response<ResponseTambahPegawai> response) {
                        if (response.isSuccessful()) {
                            Boolean status = response.body().isStatus();
                            String pesan = response.body().getPesan();

                            if (status) {
                                startActivity(new Intent(TambahPegawaiActivity.this, TampilPegawaiActivity.class));
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

/*public class TambahPegawaiActivity extends AppCompatActivity {
    //inisialisasi
    EditText etNama, etEmail, etNoHp, etAlamat;
    Button btnInput;
    ImageView ivCamera;
    TextView tvNamaGambar;

    DatePickerDialog datePickerDialog;
    SimpleDateFormat simpleDateFormat;
    EditText etCalendar;

    String mediaPath;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_pegawai);

        dialog = new Dialog(this);

        //deklarasi widget view
        etNama = findViewById(R.id.etNamaPegawai);
        etEmail = findViewById(R.id.etEmailPegawai);
        etNoHp = findViewById(R.id.etNoHpPegawai);
        etAlamat = findViewById(R.id.etAlamatPegawai);

        btnInput = findViewById(R.id.btnInputPegawai);

        ivCamera = findViewById(R.id.ivBtnGambar);

        tvNamaGambar = findViewById(R.id.tvNamaGambar);

        etCalendar = findViewById(R.id.etCalendar);

        btnInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionInputPegawai();
            }
        });

        // kita menggunakan formar tgl dd-MM-yyyy yang nantinya ketika tgl kita ditampilkan akan terlihat sbb 20-03-2019
        //simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", new Locale("id", "ID"));

        etCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog();
            }
        });

        ivCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionDialogCapture();
            }
        });
    }

    private void actionDialogCapture(){
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_dialog);

        Button btnTaKePicture = dialog.findViewById(R.id.btnClose);
        btnTaKePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,2);
            }
        });
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        dialog.dismiss();
        if (requestCode == 2) {
            Bitmap datas = (Bitmap)data.getExtras().get("data");
            Image(datas, "camera");
        }
    }

    //TODO: Method Convert gambar
    private void Image(Bitmap bitmap, String name){
        File fileDir = getFilesDir();
        File imageFile = new File(fileDir, name + ".jpg");
        mediaPath = imageFile.getPath();
        tvNamaGambar.setText(mediaPath);

        try{
            FileOutputStream outputStream = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Throwable t){
            t.printStackTrace();
        }
    }

    private void showDateDialog() {
        //TODO: mendapatkan tgl sekarang
        Calendar calendar = Calendar.getInstance();

        //TODO: datepicker dialog
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar calendarDate = Calendar.getInstance();
                calendarDate.set(year,month,dayOfMonth);

                etCalendar.setText(simpleDateFormat.format(calendarDate.getTime()));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
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
                                    startActivity(new Intent(TambahPegawaiActivity.this, TampilPegawaiActivity.class));
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
}*/
