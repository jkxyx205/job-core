package com.yodean.job;

import net.fortuna.ical4j.model.*;
import net.fortuna.ical4j.model.component.VAlarm;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.parameter.Value;
import net.fortuna.ical4j.model.property.*;
import org.junit.Test;

import java.io.IOException;
import java.text.ParseException;

/**
 * Created by rick on 6/25/18.
 */
public class Ical4jTest {

    @Test
    public void testIcal4j() throws ParseException, IOException {
        DtStart dtstart = new DtStart("20180528T063302Z");
        DtStart endDate = new DtStart("20200925T063302Z");

        RRule rule = new RRule("FREQ=MONTHLY;INTERVAL=1;BYDAY=2SA,2SU;COUNT=10");

        VEvent sessionEvent = new VEvent(dtstart.getDate(), "hahahhehe");

        sessionEvent.getProperties().add(rule);
//        sessionEvent.getProperties().add(dtstart);
        sessionEvent.getProperties().add(new Uid("2322332323323323"));
        sessionEvent.getProperties().add(new Location("南京堵路"));

        // 提醒,提前10分钟
        VAlarm valarm = new VAlarm(new Dur(0, 0, -10, 0));
        valarm.getProperties().add(new Summary("Event Alarm"));
        valarm.getProperties().add(Action.DISPLAY);
        valarm.getProperties().add(new Description("Progress Meeting at 9:30am"));
        sessionEvent.getAlarms().add(valarm);



        net.fortuna.ical4j.model.Calendar calendar = new net.fortuna.ical4j.model.Calendar();
        calendar.getProperties().add(new ProdId("-//Ben Fortuna//iCal4j 1.0//EN"));
        calendar.getProperties().add(Version.VERSION_2_0);
        calendar.getProperties().add(CalScale.GREGORIAN);
        calendar.getComponents().add(sessionEvent);
        calendar.validate();

//        System.out.println(sessionEvent.getDescription());



//        FileOutputStream fout = new FileOutputStream("/Users/rick/jkxyx205/log/2.ics");
//        CalendarOutputter outputter = new CalendarOutputter();
//        outputter.output(calendar, fout);

//        PeriodList periodList = sessionEvent.getConsumedTime(dtstart.getDate(), endDate.getDate());
//
//        for(Period period : periodList) {
//            System.out.println(period.get);
//        }



        DateList list = rule.getRecur().getDates(dtstart.getDate(), dtstart.getDate(), endDate.getDate(), Value.DATE_TIME, 10);

        for(Date date : list) {
            System.out.println(date.toLocaleString());
        }

    }
}
