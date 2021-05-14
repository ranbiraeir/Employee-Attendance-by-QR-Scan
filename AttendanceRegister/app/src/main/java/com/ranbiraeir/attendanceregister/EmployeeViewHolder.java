package com.ranbiraeir.attendanceregister;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class EmployeeViewHolder extends RecyclerView.ViewHolder {

    final TextView employeeName;
    final ImageView employeePresent;

    public EmployeeViewHolder(@NonNull View itemView) {
        super(itemView);

        employeeName = itemView.findViewById(R.id.employee_name);
        employeePresent = itemView.findViewById(R.id.employee_present);
    }
}
