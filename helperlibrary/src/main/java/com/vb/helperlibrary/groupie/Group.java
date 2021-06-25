/*
 * Copyright (c) 2021. Pisey Sen.
 * @Created by piseysen(IT Application) on 05/02/2021 5:36 PM
 */


package com.vb.helperlibrary.groupie;

import androidx.annotation.NonNull;

/**
 * A group of items, to be used in an adapter.
 */
public interface Group {

    int getItemCount();

    @NonNull
    Item getItem(int position);

    /**
     * Gets the position of an item inside this Group
     * @param item item to return position of
     * @return The position of the item or -1 if not present
     */
    int getPosition(@NonNull Item item);

    void registerGroupDataObserver(@NonNull GroupDataObserver groupDataObserver);

    void unregisterGroupDataObserver(@NonNull GroupDataObserver groupDataObserver);

}