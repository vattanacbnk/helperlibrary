/*
 * Copyright (c) 2021. Pisey Sen.
 * @Created by piseysen(IT Application) on 05/02/2021 5:36 PM
 */

package com.vb.helperlibrary.groupie;

import androidx.annotation.NonNull;

import java.util.Collection;

class GroupUtils {
    @NonNull
    static Item getItem(Collection<? extends Group> groups, int position) {
        int previousPosition = 0;

        for (Group group : groups) {
            int size = group.getItemCount();
            if (size + previousPosition > position) {
                return group.getItem(position - previousPosition);
            }
            previousPosition += size;
        }

        throw new IndexOutOfBoundsException("Wanted item at " + position + " but there are only "
                + previousPosition + " items");
    }

    static int getItemCount(@NonNull Collection<? extends Group> groups) {
        int size = 0;
        for (Group group : groups) {
            size += group.getItemCount();
        }
        return size;
    }
}
