package com.tcsorcs.trailsapp.managers;

import android.provider.BaseColumns;

/**
 * Created by Innovation on 2/24/2015.
 */
public class TrailAppDbContract {

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public TrailAppDbContract(){}

    public static abstract class QRInfo implements BaseColumns {
        public static final String TABLE_NAME = "qrinfo";
        public static final String COLUMN_NAME_QRID = "qrid";
        public static final String COLUMN_NAME_QRCODE = "qrcode";
        public static final String COLUMN_NAME_QRTYPE = "qrtype";
        public static final String COLUMN_NAME_QRTYPEDID = "qrtypedid";
    }

    public static abstract class Achievements implements BaseColumns {
        public static final String TABLE_NAME = "achievements";
        public static final String COLUMN_NAME_ACHIEVEMENTID = "achievementid";
        public static final String COLUMN_NAME_ACHIEVMENTNAME = "achievementname";
        public static final String COLUMN_NAME_ACHIEVEMENTGET = "achievementget";
        public static final String COLUMN_NAME_ACIEVEMENTDESCRIPTION = "achievementdescription";
        public static final String COLUMN_NAME_LOGOID = "logoid";
        public static final String COLUMN_NAME_DAYACHIEVED = "dayachieved";
        public static final String COLUMN_NAME_VISIBLEBEFOREGET = "visiblebeforeget";
        public static final String COLUMN_NAME_ACHIEVEMENTWEIGHT = "achievementweight";
        public static final String COLUMN_NAME_SECRETBEFOREGET = "secretbeforeget";
        public static final String COLUMN_NAME_ACHIEVEMENTSOUND = "achievementsound";
    }

    public static abstract class Requirements implements BaseColumns {
        public static final String TABLE_NAME = "requirements";
        public static final String COLUMN_NAME_REQUIREMENTID = "requirementid";
        public static final String COLUMN_NAME_ACHIEVEMENTID = "achievementid";
        public static final String COLUMN_NAME_REQUIREMENTDONE = "requirementdone";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
    }

    public static abstract class Locations implements BaseColumns {
        public static final String TABLE_NAME = "locations";
        public static final String COLUMN_NAME_LOCATIONID = "locationid";
        public static final String COLUMN_NAME_LOCATIONNAME = "locationname";
        public static final String COLUMN_NAME_LOCATIONDESCRIPTION = "locationdescription";
        public static final String COLUMN_NAME_LOCATIONX = "locationx";
        public static final String COLUMN_NAME_LOCATIONY = "locationy";
    }

    public static abstract class StatsInformation implements BaseColumns {
        public static final String TABLE_NAME = "stats";
        public static final String COLUMN_NAME_CURRENTDISTANCE = "currentdistance";
        public static final String COLUMN_NAME_CURRENTPACE = "currentpace";
        public static final String COLUMN_NAME_CURRENTDURATION = "currentduration";
        public static final String COLUMN_NAME_LIFETIMEDISTANCE = "lifetimedistance";
        public static final String COLUMN_NAME_LIFETIMEPACE = "lifetimepace";
        public static final String COLUMN_NAME_LIFETIMEDURATION = "lifetimeduration";
        public static final String COLUMN_NAME_CUMMULATIVEDISTANCE = "cummulativedistance";
        public static final String COLUMN_NAME_CUMMULATIVEPACE = "cummulativepace";
        public static final String COLUMN_NAME_CUMMULATIVEDURATION = "cummulativeduration";
    }

    public static abstract class SegmentInformation implements BaseColumns {
        public static final String TABLE_NAME = "segments";
        public static final String COLUMN_NAME_SEGMENTID = "segmentid";
        public static final String COLUMN_NAME_SEGMENTNAME = "segmentname";
        public static final String COLUMN_NAME_POINTA = "pointa";
        public static final String COLUMN_NAME_POINTB = "pointb";
        public static final String COLUMN_NAME_SEGMENTLENGTH = "segmentlength";
        public static final String COLUMN_NAME_SEGMENTTYPE = "segmenttype";
    }

    public static abstract class SegmentGraphic implements BaseColumns{
        public static final String TABLE_NAME = "segmentgraphics";
        public static final String COLUMN_NAME_SEGMENTGRAPHICID = "segementgraphicid";
        public static final String COLUMN_NAME_SEGMENTGRAPHICNAME = "segmentgraphicname";
        public static final String COLUMN_NAME_SEGMENTCORNERX = "segmentcornerx";
        public static final String COLUMN_NAME_SEGMENTCORNERY = "segmentcornery";
        public static final String COLUMN_NAME_SEGMENTWIDTH = "segmentwidth";
        public static final String COLUMN_NAME_SEGMENTHEIGHT = "segmentheight";
        public static final String COLUMN_NAME_GRAPHICLOCATION = "graphiclocation";
    }
}
