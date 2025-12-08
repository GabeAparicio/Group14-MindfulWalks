package com.example.mindfulwalks;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CheckpointAdapter extends RecyclerView.Adapter<CheckpointAdapter.ViewHolder> {

    private final List<Checkpoint> list;
    private final OnItemClickListener listener;
    private final OnDeleteClickListener deleteListener;
    private final OnEditClickListener editListener;

    public interface OnItemClickListener {
        void onItemClick(Checkpoint c);
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(Checkpoint c, int position);
    }

    public interface OnEditClickListener {
        void onEditClick(Checkpoint c);
    }

    public CheckpointAdapter(List<Checkpoint> list, OnItemClickListener listener,
                             OnDeleteClickListener deleteListener, OnEditClickListener editListener) {
        this.list = list;
        this.listener = listener;
        this.deleteListener = deleteListener;
        this.editListener = editListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, address;
        ImageButton btnDelete, btnEdit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.txtTitle);
            address = itemView.findViewById(R.id.txtAddress);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnEdit = itemView.findViewById(R.id.btnEdit);
        }

        public void bind(Checkpoint c, OnItemClickListener listener,
                         OnDeleteClickListener deleteListener, OnEditClickListener editListener, int position) {
            title.setText(c.title);
            address.setText(c.address);

            itemView.setOnClickListener(v -> listener.onItemClick(c));

            btnDelete.setOnClickListener(v -> deleteListener.onDeleteClick(c, position));

            btnEdit.setOnClickListener(v -> editListener.onEditClick(c));
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_checkpoint, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(list.get(position), listener, deleteListener, editListener, position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void removeItem(int position) {
        list.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, list.size());
    }
}