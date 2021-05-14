package com.ranbiraeir.attendanceregister;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends AppCompatActivity implements CaptureCallbackListener {

    private FirebaseDatabase db;

    private static final String TAG = "HomeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ButterKnife.bind(this);

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please Wait..");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        List<Employee> employeeList = new ArrayList<>();
        RecyclerView recyclerView = findViewById(R.id.rv_employees);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        EmployeeAdapter adapter = new EmployeeAdapter(this, null);
        recyclerView.setAdapter(adapter);

        db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference("employees");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshots) {

                employeeList.clear();
                for (DataSnapshot snapshot : snapshots.getChildren())
                    employeeList.add(new Employee(snapshot.getKey(), snapshot.child("name").getValue().toString(), false));

                adapter.setEmployeeList(employeeList);

                Date date = new Date();
                db.getReference(date.getDate() + "-" + (date.getMonth() + 1) + "-" + (date.getYear() + 1900))
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshots) {
                                Log.d(TAG, "bitch: " + snapshots);
                                progressDialog.dismiss();

                                if (snapshots.getValue() == null) return;

                                Set<String> ids = new HashSet<>();

                                for (DataSnapshot snapshot : snapshots.getChildren())
                                    ids.add(snapshot.getKey());

                                for (int i = 0; i < employeeList.size(); ++i) {
                                    Employee employee = employeeList.get(i);
                                    employeeList.get(i).setPresent(ids.contains(employee.getId()));
                                }
                                Log.d(TAG, "onDataChange: " + employeeList);
                                adapter.setEmployeeList(employeeList);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                progressDialog.dismiss();
                                Log.d(TAG, "onCancelled: " + error.getMessage());
                            }
                        });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
                Log.d(TAG, "onCancelled: " + error.getMessage());
            }
        });
    }

    @OnClick(R.id.fab_scan)
    public void onClickFab() {
        CaptureQRCodeFragment fragment = new CaptureQRCodeFragment(this);
        fragment.show(getSupportFragmentManager(), "capture_qr_code");
    }

    @Override
    public void onCaptureTextReceived(String text) {
        DatabaseReference ref = db.getReference("employees");
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshots) {
                boolean found = false;
                DataSnapshot target = null;
                for (DataSnapshot snapshot : snapshots.getChildren()) {
                    if (text.equals(snapshot.getKey())) {
                        found = true;
                        target = snapshot;
                        break;
                    }
                }

                if (found) {
                    Date date = new Date();
                    DatabaseReference ref = db.getReference(date.getDate() + "-" + (date.getMonth() + 1) + "-" + (date.getYear() + 1900)).child(text);
                    ref.setValue(new Employee(text, target.child("name").getValue().toString(), true));

                } else
                    Toast.makeText(HomeActivity.this, "hum se bakchodi?!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "onCancelled: " + error.getMessage());
                Toast.makeText(HomeActivity.this, "onCancelled: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };

        ref.addValueEventListener(listener);
    }
}