package com.example.travel.repository

import android.graphics.Bitmap


interface ProfileImageRepository {
    fun getImageUrlFromFirestore() : Bitmap?
}