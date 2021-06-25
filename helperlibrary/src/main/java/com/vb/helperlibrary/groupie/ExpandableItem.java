/*
 * Copyright (c) 2021. Pisey Sen.
 * @Created by piseysen(IT Application) on 05/02/2021 5:36 PM
 */


package com.vb.helperlibrary.groupie;

import androidx.annotation.NonNull;

/**
 * The "collapsed"/header item of an expanded group.  Some part (or all) of it is a "toggle" to
 * expand the group.
 *
 * Collapsed:
 * - This
 *
 * Expanded:
 * - This
 *   - Child
 *   - Child
 *   - etc
 *
 */
public interface ExpandableItem {
    void setExpandableGroup(@NonNull ExpandableGroup onToggleListener);
}
