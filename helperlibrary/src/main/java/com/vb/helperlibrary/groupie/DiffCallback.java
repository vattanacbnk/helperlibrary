/*
 * Copyright (c) 2021. Pisey Sen.
 * @Created by piseysen(IT Application) on 05/02/2021 5:36 PM
 */

package com.vb.helperlibrary.groupie;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import java.util.Collection;


class DiffCallback extends DiffUtil.Callback {
    private final int oldBodyItemCount;
    private final int newBodyItemCount;
    private final Collection<? extends Group> oldGroups;
    private final Collection<? extends Group> newGroups;

    DiffCallback(Collection<? extends Group> oldGroups, Collection<? extends Group> newGroups) {
        this.oldBodyItemCount = GroupUtils.getItemCount(oldGroups);
        this.newBodyItemCount = GroupUtils.getItemCount(newGroups);
        this.oldGroups = oldGroups;
        this.newGroups = newGroups;
    }

    @Override
    public int getOldListSize() {
        return oldBodyItemCount;
    }

    @Override
    public int getNewListSize() {
        return newBodyItemCount;
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        Item oldItem = GroupUtils.getItem(oldGroups, oldItemPosition);
        Item newItem = GroupUtils.getItem(newGroups, newItemPosition);
        return newItem.isSameAs(oldItem);
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        Item oldItem = GroupUtils.getItem(oldGroups, oldItemPosition);
        Item newItem = GroupUtils.getItem(newGroups, newItemPosition);
        return newItem.hasSameContentAs(oldItem);
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        Item oldItem = GroupUtils.getItem(oldGroups, oldItemPosition);
        Item newItem = GroupUtils.getItem(newGroups, newItemPosition);
        return oldItem.getChangePayload(newItem);
    }
}