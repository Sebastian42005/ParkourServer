package com.example.spotmap.controller.company.timetable;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Date;

@Data
@NoArgsConstructor
public class TimeTableObject {
    String from;
    String to;
    String name;
}
