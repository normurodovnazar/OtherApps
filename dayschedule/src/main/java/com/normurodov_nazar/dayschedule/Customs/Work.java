package com.normurodov_nazar.dayschedule.Customs;

public class Work {
    final String title;
    final Time time;
    boolean active;

    public Work(String title, Time time, boolean active) {
        this.title = title;
        this.time = time;
        this.active = active;
    }

    public String getTitle() {
        return title;
    }

    public Time getTime() {
        return time;
    }

    public boolean isActive() {
        return active;
    }

    public String getId(){
        return time.toString();
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "Work{" +
                "title='" + title + '\'' +
                ", hour=" + time.getHour() +
                ", minute"+ time.getMinute()+
                ", active=" + active +
                '}';
    }
}
