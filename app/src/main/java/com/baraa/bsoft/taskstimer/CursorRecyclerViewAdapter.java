package com.baraa.bsoft.taskstimer;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created by baraa on 16/07/2017.
 */

class CursorRecyclerViewAdapter extends RecyclerView.Adapter<CursorRecyclerViewAdapter.TasksViewHolder> {
    private static final String TAG = "CursorRecyclerViewAdapt";
    private Cursor cursor;
    private ItemListClickListener itemListClickListener;

    interface ItemListClickListener{
        void onDeleteListClicked(Task task);
        void onEditListClicked(Task task);

    }

    public CursorRecyclerViewAdapter(Cursor nCursor,ItemListClickListener callback) {
        this.itemListClickListener = callback;
        this.cursor = nCursor;
    }

    @Override
    public TasksViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tasks_list_item,parent,false);
        return new CursorRecyclerViewAdapter.TasksViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TasksViewHolder holder, int position) {
        if(null == cursor || cursor.getCount() ==0 ){
            // welecome message if there is no data ...
            holder.getTvName().setText("No Tasks ...");
            holder.getTvDescription().setText("Press add button to Enter new task\n");
            holder.getBtnEdit().setVisibility(View.GONE);
            holder.getBtnDelete().setVisibility(View.GONE);
        }else {
            if(!cursor.moveToPosition(position)){
                throw new IllegalStateException("Error! Cursor out of Range "+position);
            }
            final Task task = new Task(cursor.getInt(cursor.getColumnIndex(TasksContract.Columns._ID)),
                    cursor.getString(cursor.getColumnIndex(TasksContract.Columns.TASKS_NAME)),
                    cursor.getString(cursor.getColumnIndex(TasksContract.Columns.TASKS_DESCRIPTION)),
                    cursor.getInt(cursor.getColumnIndex(TasksContract.Columns.TASKS_SORTORDER)));

            holder.getTvName().setText(task.getName());
            holder.getTvDescription().setText(task.getDescription());
            holder.getBtnEdit().setVisibility(View.VISIBLE);
            holder.getBtnDelete().setVisibility(View.VISIBLE);

            View.OnClickListener taskOnClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()){
                        case R.id.btnEditLi:
                            itemListClickListener.onEditListClicked(task);
                            break;
                        case R.id.btnDeleteLi:
                            itemListClickListener.onDeleteListClicked(task);
                            break;
                        default:
                            throw new IllegalArgumentException("Unknown Button ID :"+v.getId());
                    }
                }
            };
            holder.btnEdit.setOnClickListener(taskOnClickListener);
            holder.btnDelete.setOnClickListener(taskOnClickListener);

        }

    }

    @Override
    public int getItemCount() {
        if(null == cursor||cursor.getCount()==0){
            return 1;
        }else {
            return cursor.getCount();
        }
    }

    Cursor swapCursor(Cursor newCursor){
        if(cursor == newCursor){
            return null;
        }
        final Cursor oldCursor = cursor;
        cursor = newCursor;
        if(newCursor != null){
            notifyDataSetChanged();
        }else {
            notifyItemRangeRemoved(0,getItemCount());
        }
        return oldCursor;

    }

    public static class TasksViewHolder extends RecyclerView.ViewHolder{
        private TextView tvName;
        private TextView tvDescription;
        private ImageButton btnEdit;
        private ImageButton btnDelete;
        public TasksViewHolder(View itemView) {
            super(itemView);
            this.tvName = (TextView) itemView.findViewById(R.id.tvNameLi);
            this.tvDescription = (TextView) itemView.findViewById(R.id.tvDescriptionLi);
            this.btnDelete = (ImageButton) itemView.findViewById(R.id.btnDeleteLi);
            this.btnEdit = (ImageButton) itemView.findViewById(R.id.btnEditLi);
        }

        public TextView getTvName() {
            return tvName;
        }

        public TextView getTvDescription() {
            return tvDescription;
        }

        public ImageButton getBtnEdit() {
            return btnEdit;
        }

        public ImageButton getBtnDelete() {
            return btnDelete;
        }
    }
}
