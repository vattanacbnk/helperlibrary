/*
 * Copyright (c) 2021. Vattanac Bank.
 * @Created by piseysen(IT Application) on 05/02/2021 5:36 PM
 */

package com.vb.helperlibrary.interfaces

interface CustomAlertDialogListener<T> {
    fun onAccepted(dialog: T)
    fun onCancel(dialog: T)
}