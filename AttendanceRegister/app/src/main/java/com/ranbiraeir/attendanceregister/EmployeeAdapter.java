package com.ranbiraeir.attendanceregister;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.widget.ImageViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeViewHolder> {

    private final Context context;
    private List<Employee> employeeList;

    public EmployeeAdapter(Context context, List<Employee> employeeList) {
        this.context = context;
        this.employeeList = employeeList;
    }

    @NonNull
    @Override
    public EmployeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_employee, parent, false);
        return new EmployeeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeViewHolder holder, int position) {
        Employee employee = employeeList.get(position);
        Resources resources = context.getResources();
        holder.employeeName.setText(employee.getName());
        holder.employeePresent.setImageDrawable(employee.isPresent() ?resources.getDrawable(R.drawable.ic_check)
                : resources.getDrawable(R.drawable.ic_cancel));
        ImageViewCompat.setImageTintList(holder.employeePresent, ColorStateList.valueOf(employee.isPresent() ? resources.getColor(R.color.present)
                : resources.getColor(R.color.absent)));
    }

    public void setEmployeeList(List<Employee> employeeList) {
        this.employeeList = employeeList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return employeeList == null ? 0 : employeeList.size();
    }
}
