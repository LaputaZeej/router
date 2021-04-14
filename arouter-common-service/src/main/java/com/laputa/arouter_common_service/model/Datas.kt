package com.laputa.arouter_common_service.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

/**
 * Author by xpl, Date on 2021/4/13.
 */
data class SerializableBean(var name: String, var id: Int) : Serializable

@Parcelize
data class ParcelableBean(var name: String, var age: Long) : Parcelable

class ObjectBean(var name:String,var age:Float)