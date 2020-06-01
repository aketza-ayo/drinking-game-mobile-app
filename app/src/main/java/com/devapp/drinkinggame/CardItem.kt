package com.devapp.drinkinggame

import android.os.Parcel
import android.os.Parcelable

class CardItem(val name: String?, val imageResource: Int, val rule: String?, val value: Int) : Parcelable{


    constructor(parcel: Parcel) : this(parcel.readString(), parcel.readInt(), parcel.readString(), parcel.readInt()) {

    }


    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest!!.writeString(this.name)
        dest!!.writeInt(this.imageResource)
        dest!!.writeString(this.rule)
        dest!!.writeInt(this.value)
    }

    override fun describeContents(): Int {
       return 0
    }

    companion object CREATOR : Parcelable.Creator<CardItem> {
        override fun createFromParcel(parcel: Parcel): CardItem {
            return CardItem(parcel)
        }

        override fun newArray(size: Int): Array<CardItem?> {
            return arrayOfNulls(size)
        }
    }

}