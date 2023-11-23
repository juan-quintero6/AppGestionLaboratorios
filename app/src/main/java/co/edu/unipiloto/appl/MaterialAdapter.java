package co.edu.unipiloto.appl;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class MaterialAdapter extends RecyclerView.Adapter<MaterialAdapter.MaterialViewHolder> {

    private List<String> listaMateriales;

    public MaterialAdapter(List<String> listaMateriales) {
        this.listaMateriales = listaMateriales;
    }

    @Override
    public MaterialViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_material, parent, false);
        return new MaterialViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MaterialViewHolder holder, int position) {
        String material = listaMateriales.get(position);
        holder.txtMaterial.setText(material);
    }

    @Override
    public int getItemCount() {
        return listaMateriales.size();
    }

    public static class MaterialViewHolder extends RecyclerView.ViewHolder {
        TextView txtMaterial;

        public MaterialViewHolder(View itemView) {
            super(itemView);
            txtMaterial = itemView.findViewById(R.id.txtMaterial);
        }
    }
}
