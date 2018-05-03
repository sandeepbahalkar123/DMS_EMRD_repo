package com.rescribe.doctor.model.dashboard;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CalendarTypeList implements Parcelable
{

@SerializedName("id")
@Expose
private Integer id;
@SerializedName("type")
@Expose
private String type;
public final static Parcelable.Creator<CalendarTypeList> CREATOR = new Creator<CalendarTypeList>() {


@SuppressWarnings({
"unchecked"
})
public CalendarTypeList createFromParcel(Parcel in) {
return new CalendarTypeList(in);
}

public CalendarTypeList[] newArray(int size) {
return (new CalendarTypeList[size]);
}

}
;

protected CalendarTypeList(Parcel in) {
this.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
this.type = ((String) in.readValue((String.class.getClassLoader())));
}

public CalendarTypeList() {
}

public Integer getId() {
return id;
}

public void setId(Integer id) {
this.id = id;
}

public String getType() {
return type;
}

public void setType(String type) {
this.type = type;
}

public void writeToParcel(Parcel dest, int flags) {
dest.writeValue(id);
dest.writeValue(type);
}

public int describeContents() {
return 0;
}

}