package com.ayronasystems.core.util;

import java.util.*;

/**
 * Created by gorkemgok on 12/06/16.
 */
public class Interval {

    public static final Interval ZERO = new Interval (new Date (0), new Date (0));

    private Date beginDate;

    private Date endDate;

    public Interval (Date beginDate, Date endDate) {
        this.beginDate = beginDate;
        this.endDate = endDate;
    }

    public Date getBeginningDate () {
        return beginDate;
    }

    public Date getEndingDate () {
        return endDate;
    }

    public boolean contains(Date date){
        if ((beginDate.before (date) || beginDate.equals (date)) && endDate.after (date)){
            return true;
        }
        return false;
    }

    public boolean contains(Date beginDate, Date endDate){
        if ((this.beginDate.before (beginDate) || this.beginDate.equals (beginDate)) && this.endDate.after (beginDate) &&
                (this.beginDate.before (endDate) && (this.endDate.after (endDate) || this.endDate.equals (endDate)))){
            return true;
        }
        return false;
    }

    public boolean contains(Interval interval){
        return contains (interval.getBeginningDate (), interval.getEndingDate ());
    }

    public boolean overlapsAtLeft(Date beginDate, Date endDate){
        if (this.beginDate.before (beginDate) &&
                this.endDate.after (beginDate) &&
                (this.endDate.before (endDate) || this.endDate.equals (endDate))){
            return true;
        }
        return false;
    }

    public boolean overlapsAtRight(Date beginDate, Date endDate){
        if ((this.beginDate.after (beginDate) || this.beginDate.equals (beginDate)) &&
                this.beginDate.before (endDate) &&
                this.endDate.after (endDate)){
            return true;
        }
        return false;
    }

    public boolean overlaps(Date beginDate, Date endDate){
        return overlapsAtLeft (beginDate, endDate) || overlapsAtRight (beginDate, endDate);
    }

    public boolean overlapsAtLeft(Interval interval){
        return overlapsAtLeft (interval.getBeginningDate (), interval.getEndingDate ());
    }

    public boolean overlapsAtRight(Interval interval){
        return overlapsAtRight (interval.getBeginningDate (), interval.getEndingDate ());
    }

    public boolean overlaps(Interval interval){
        return overlaps (interval.getBeginningDate (), interval.getEndingDate ());
    }

    @Override
    public boolean equals (Object o) {
        if ( this == o ) {
            return true;
        }
        if ( o == null || getClass () != o.getClass () ) {
            return false;
        }

        Interval interval = (Interval) o;

        if ( !beginDate.equals (interval.beginDate) ) {
            return false;
        }
        return endDate.equals (interval.endDate);

    }

    @Override
    public int hashCode () {
        int result = beginDate.hashCode ();
        result = 31 * result + endDate.hashCode ();
        return result;
    }

    public List<Interval> extractNot(Interval interval){
        if (interval.equals (this) || contains (interval)){
            return Collections.EMPTY_LIST;
        }
        boolean contains = interval.contains (this);
        List<Interval> intervalList = new ArrayList<Interval> ();
        if ((interval.overlapsAtLeft (this) || contains) &&
                !interval.getBeginningDate ().equals ( getBeginningDate ()) ){
            Interval absentInterval = new Interval (interval.getBeginningDate (), getBeginningDate ());
            intervalList.add (absentInterval);
        }
        if ((interval.overlapsAtRight (this) || contains) &&
                !getEndingDate ().equals (interval.getEndingDate ())){
            Interval absentInterval = new Interval (getEndingDate (), interval.getEndingDate ());
            intervalList.add (absentInterval);
        }
        if (intervalList.isEmpty ()){
            return Arrays.asList (interval);
        }
        return intervalList;
    }
}
