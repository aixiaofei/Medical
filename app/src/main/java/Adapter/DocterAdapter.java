package Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.ai.dtest.R;
import java.util.List;

import commen.TDoctorCustom;
import commen.TDoctorinfo;

/**
 * Created by ai on 2017/7/9.
 */

public class DocterAdapter extends RecyclerView.Adapter<DocterAdapter.ViewHolder>{

    private List<TDoctorCustom> mList;

    public DocterAdapter(List<TDoctorCustom> mList){
        this.mList=mList;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        View docter_view;
        TextView docter_name;
        TextView docter_age;
        TextView docter_keshi;
        TextView docter_hosipital;
        TextView docter_specialty;

        public ViewHolder(View view){
            super(view);
            docter_view=view;
            docter_name= (TextView) view.findViewById(R.id.docter_name);
            docter_age= (TextView) view.findViewById(R.id.docter_age);
            docter_keshi= (TextView) view.findViewById(R.id.docter_keshi);
            docter_hosipital= (TextView) view.findViewById(R.id.docter_hosipital);
            docter_specialty= (TextView) view.findViewById(R.id.docter_specialty);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.docter_item,parent,false);
        final ViewHolder viewHolder= new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TDoctorCustom docter= mList.get(position);
        holder.docter_name.setText(docter.getDocname());
        holder.docter_age.setText(docter.getDocage().toString());
        holder.docter_keshi.setText(docter.getDocdepartment());
        holder.docter_hosipital.setText(docter.getHospitaladdress());
        holder.docter_specialty.setText(docter.getDocexpert());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
