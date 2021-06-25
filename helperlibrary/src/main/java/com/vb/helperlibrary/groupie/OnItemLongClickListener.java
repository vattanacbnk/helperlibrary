/*
 * Copyright (c) 2021. Pisey Sen.
 * @Created by piseysen(IT Application) on 05/02/2021 5:36 PM
 */


package com.vb.helperlibrary.groupie;

import android.view.View;

import androidx.annotation.NonNull;

public interface OnItemLongClickListener {

    boolean onItemLongClick(@NonNull Item item, @NonNull View view);

}
