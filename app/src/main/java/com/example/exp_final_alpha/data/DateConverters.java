package com.example.exp_final_alpha.data;

import androidx.annotation.Nullable;
import androidx.room.TypeConverter;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class DateConverters {

    @TypeConverter
    public long dateToTimestamp(@Nullable Date date){
        assert date != null;
        return date.getTime();
    }

    @TypeConverter
    public Date longToDate(long time){
        return new Date(time);
    }

}
