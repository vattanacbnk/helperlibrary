/*
 * Copyright (c) 2021. Pisey Sen.
 * @Created by piseysen(IT Application) on 05/02/2021 5:36 PM
 */


package com.vb.helperlibrary.groupie;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public abstract class TouchCallback extends ItemTouchHelper.SimpleCallback {

    public TouchCallback() {
        super(0, 0);
    }

    @Override
    public int getSwipeDirs(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        return ((GroupieViewHolder) viewHolder).getSwipeDirs();
    }

    @Override
    public int getDragDirs(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        return ((GroupieViewHolder) viewHolder).getDragDirs();
    }
}
