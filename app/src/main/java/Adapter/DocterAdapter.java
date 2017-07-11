package Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.ai.dtest.R;
import java.util.List;

import commen.Docter;

/**
 * Created by ai on 2017/7/9.
 */

public class DocterAdapter extends RecyclerView.Adapter<DocterAdapter.ViewHolder>{

    private List<Docter> mList;

    public DocterAdapter(List<Docter> mList){
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
        Docter docter= mList.get(position);
        holder.docter_name.setText(docter.getName());
        holder.docter_age.setText(Integer.toString(docter.getAge()));
        holder.docter_keshi.setText(docter.getKeshi());
        holder.docter_hosipital.setText(docter.getHospital());
        holder.docter_specialty.setText(docter.getSpecialty());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
