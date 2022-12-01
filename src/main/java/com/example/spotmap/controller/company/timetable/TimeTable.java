package com.example.spotmap.controller.company.timetable;

import lombok.Data;

import java.util.List;

@Data
public class TimeTable {

    List<TimeTableObject> monday;
    List<TimeTableObject> tuesday;
    List<TimeTableObject> wednesday;
    List<TimeTableObject> thursday;
    List<TimeTableObject> friday;
    List<TimeTableObject> saturday;
    List<TimeTableObject> sunday;

}
