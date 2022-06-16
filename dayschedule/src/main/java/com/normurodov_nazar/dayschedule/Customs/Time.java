package com.normurodov_nazar.dayschedule.Customs;

public class Time {
    final int hour;
    final int minute;

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public Time(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
    }

    @Override
    public String toString() {
        return hour+":"+minute;
    }
}
