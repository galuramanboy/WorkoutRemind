package com.example.workoutremind;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workoutremind.databinding.WorkoutListItemBinding;
import com.example.workoutremind.models.Workout;

public class WorkoutsAdapter extends RecyclerView.Adapter<WorkoutsAdapter.WorkoutsViewHolder> {
    ObservableArrayList<Workout> workouts;
    OnWorkoutSelectedListener listener;

    public static interface OnWorkoutSelectedListener {
        public void onSelected(Workout workout);
    }

    public WorkoutsAdapter(ObservableArrayList<Workout> workouts, OnWorkoutSelectedListener listener) {
        this.workouts = workouts;
        this.listener = listener;
        workouts.addOnListChangedCallback(new ObservableList.OnListChangedCallback<ObservableList<Workout>>() {
            @Override
            public void onChanged(ObservableList<Workout> sender) {
                notifyDataSetChanged();
            }

            @Override
            public void onItemRangeChanged(ObservableList<Workout> sender, int positionStart, int itemCount) {
                WorkoutsAdapter.this.notifyItemRangeChanged(positionStart, itemCount);
            }

            @Override
            public void onItemRangeInserted(ObservableList<Workout> sender, int positionStart, int itemCount) {
                WorkoutsAdapter.this.notifyItemRangeInserted(positionStart, itemCount);
            }

            @Override
            public void onItemRangeMoved(ObservableList<Workout> sender, int fromPosition, int toPosition, int itemCount) {
                WorkoutsAdapter.this.notifyItemMoved(fromPosition, toPosition);
            }

            @Override
            public void onItemRangeRemoved(ObservableList<Workout> sender, int positionStart, int itemCount) {
                WorkoutsAdapter.this.notifyItemRangeRemoved(positionStart, itemCount);
            }
        });
    }

    @NonNull
    @Override
    public WorkoutsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        WorkoutListItemBinding binding = WorkoutListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new WorkoutsViewHolder(binding);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull WorkoutsViewHolder holder, int position) {
        holder.getBinding().day.setText(workouts.get(position).getDay() + "");
        if (workouts.get(position).getHour() == -1 && workouts.get(position).getMinute() == -1){
            holder.getBinding().timeView.setText("No Alarm");
        }
        else {
            int hour = -1;
            String time;
            if (workouts.get(position).getHour() == 0) {
                hour = 12;
                time = String.format("%2d:%02d AM", hour, workouts.get(position).getMinute());
            }
            else if(workouts.get(position).getHour() >= 12) {
                hour = workouts.get(position).getHour() % 12;
                if (hour == 0) hour = 12;
                time = String.format("%2d:%02d PM", hour, workouts.get(position).getMinute());
            }
            else {
                hour = workouts.get(position).getHour();
                time = String.format("%2d:%02d AM", hour, workouts.get(position).getMinute());
            }

            holder.getBinding().timeView.setText(time);
        }

        if (workouts.get(position).isNotify()){
            holder.getBinding().card.setBackgroundColor(R.drawable.googleg_disabled_color_18);
        }
        holder.itemView.setOnClickListener(view -> {
            this.listener.onSelected(workouts.get(position));
        });

    }

    @Override
    public int getItemCount() {
        return workouts.size();
    }

    public static class WorkoutsViewHolder extends RecyclerView.ViewHolder {
        WorkoutListItemBinding binding;
        public WorkoutsViewHolder(WorkoutListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public WorkoutListItemBinding getBinding() {
            return binding;
        }
    }
}
