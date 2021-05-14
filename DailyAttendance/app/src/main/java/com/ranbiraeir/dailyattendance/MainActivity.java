package com.ranbiraeir.dailyattendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

import com.google.firebase.auth.FirebaseAuth;
import com.google.zxing.WriterException;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private ImageView qrCodeIV;
    private String dataEdt;
    private TextView username;
    private FirebaseDatabase db;
    Bitmap bitmap;
    QRGEncoder qrgEncoder;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.show();

        FirebaseAuth auth =  FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        // Log.d(TAG, "Current user: "+ auth.getCurrentUser().getUid());
        String uid = Objects.requireNonNull(auth.getCurrentUser()).getUid();
        username = findViewById(R.id.username);
        db.getReference("employees").child(uid).child("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                progressDialog.dismiss();
                username.setText("Hi, " + snapshot.getValue().toString() +"!");
                ((TextView) findViewById(R.id.textView)).setText("Show this QR code when asked.");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
            }
        });

        qrCodeIV = findViewById(R.id.idIVQrcode);


        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);

        Display display = manager.getDefaultDisplay();

        Point point = new Point();
        display.getSize(point);

        int width = point.x;
        int height = point.y;

        int dimen = Math.min(width, height);
        dimen = dimen * 3 / 4;

        dataEdt = Objects.requireNonNull(auth.getCurrentUser()).getUid();
        qrgEncoder = new QRGEncoder(dataEdt, null, QRGContents.Type.TEXT, dimen);

        try {
            // getting QR-code in the form of bitmap.
            bitmap = qrgEncoder.encodeAsBitmap();
            // view bitmap
            qrCodeIV.setImageBitmap(bitmap);
        } catch (WriterException e) {
            // this method is called for
            // exception handling.
            Log.e("Tag", e.toString());
        }

    }
}