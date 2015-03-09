package com.tcsorcs.trailsapp.managers;

import android.provider.BaseColumns;

import com.tcsorcs.trailsapp.helpers.Achievement;

/**
 * Created by Innovation on 2/24/2015.
 */
public class TrailAppDbContract {

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public TrailAppDbContract(){}

    public static abstract class QRInfo implements BaseColumns {
        public static final String TABLE_NAME = "qrinfo";
        public static final String COLUMN_NAME_QR_ID = "qrid";
        public static final String COLUMN_NAME_QR_CODE = "qrcode";
        public static final String COLUMN_NAME_QR_TYPE = "qrtype";
        public static final String COLUMN_NAME_QR_TYPED_ID = "qrtypedid";
    }

    public static abstract class Achievements implements BaseColumns {
        public static final String TABLE_NAME = "achievements";
        public static final String COLUMN_NAME_ACHIEVEMENT_ID = "achievementid";
        public static final String COLUMN_NAME_ACHIEVEMENT_NAME = "achievementname";
        public static final String COLUMN_NAME_ACHIEVEMENT_GET = "achievementget";
        public static final String COLUMN_NAME_ACHIEVEMENT_DESCRIPTION = "achievementdescription";
        public static final String COLUMN_NAME_LOGO_ID = "logoid";
        public static final String COLUMN_NAME_DAY_ACHIEVED = "dayachieved";
        public static final String COLUMN_NAME_VISIBLE_BEFORE_GET = "visiblebeforeget";
        public static final String COLUMN_NAME_ACHIEVEMENT_WEIGHT = "achievementweight";
        public static final String COLUMN_NAME_SECRET_BEFORE_GET = "secretbeforeget";
        public static final String COLUMN_NAME_ACHIEVEMENT_SOUND = "achievementsound";
    }

    public static abstract class Requirements implements BaseColumns {
        public static final String TABLE_NAME = "requirements";
        public static final String COLUMN_NAME_REQUIREMENT_ID = "requirementid";
        public static final String COLUMN_NAME_ACHIEVEMENT_ID = "achievementid";
        public static final String COLUMN_NAME_REQUIREMENT_DONE = "requirementdone";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
    }

    public static abstract class Locations implements BaseColumns {
        public static final String TABLE_NAME = "locations";
        public static final String COLUMN_NAME_LOCATION_ID = "locationid";
        public static final String COLUMN_NAME_LOCATION_NAME = "locationname";
        public static final String COLUMN_NAME_LOCATION_DESCRIPTION = "locationdescription";
        public static final String COLUMN_NAME_LOCATION_X = "locationx";
        public static final String COLUMN_NAME_LOCATION_Y = "locationy";
    }

    public static abstract class StatsInformation implements BaseColumns {
        public static final String TABLE_NAME = "stats";
        public static final String COLUMN_NAME_CURRENT_DISTANCE = "currentdistance";
        public static final String COLUMN_NAME_CURRENT_PACE = "currentpace";
        public static final String COLUMN_NAME_CURRENT_DURATION = "currentduration";
        public static final String COLUMN_NAME_LIFETIME_DISTANCE = "lifetimedistance";
        public static final String COLUMN_NAME_LIFETIME_PACE = "lifetimepace";
        public static final String COLUMN_NAME_LIFETIME_DURATION = "lifetimeduration";
        public static final String COLUMN_NAME_CUMMULATIVE_DISTANCE = "cummulativedistance";
        public static final String COLUMN_NAME_CUMMULATIVE_PACE = "cummulativepace";
        public static final String COLUMN_NAME_CUMMULATIVE_DURATION = "cummulativeduration";
    }

    public static abstract class SegmentInformation implements BaseColumns {
        public static final String TABLE_NAME = "segments";
        public static final String COLUMN_NAME_SEGMENT_ID = "segmentid";
        public static final String COLUMN_NAME_SEGMENT_NAME = "segmentname";
        public static final String COLUMN_NAME_POINT_A = "pointa";
        public static final String COLUMN_NAME_POINT_B = "pointb";
        public static final String COLUMN_NAME_SEGMENT_LENGTH = "segmentlength";
        public static final String COLUMN_NAME_SEGMENT_TYPE = "segmenttype";
    }

    public static abstract class SegmentGraphic implements BaseColumns{
        public static final String TABLE_NAME = "segmentgraphics";
        public static final String COLUMN_NAME_SEGMENT_GRAPHIC_ID = "segementgraphicid";
        public static final String COLUMN_NAME_SEGMENT_GRAPHIC_NAME = "segmentgraphicname";
        public static final String COLUMN_NAME_SEGMENT_CORNER_X = "segmentcornerx";
        public static final String COLUMN_NAME_SEGMENT_CORNER_Y = "segmentcornery";
        public static final String COLUMN_NAME_SEGMENT_WIDTH = "segmentwidth";
        public static final String COLUMN_NAME_SEGMENT_HEIGHT = "segmentheight";
        public static final String COLUMN_NAME_GRAPHIC_LOCATION = "graphiclocation";
    }

    private static final String TEXT_TYPE = " TEXT";
    private static final String PRIMARY_KEY_TYPE = " INTEGER PRIMARY KEY";
    private static final String COMMA_SEP = ",";


    public static final String SQL_CREATE_QR_INFO =
            "CREATE TABLE " + QRInfo.TABLE_NAME + " (" +
                    QRInfo._ID + PRIMARY_KEY_TYPE + COMMA_SEP +
                    QRInfo.COLUMN_NAME_QR_ID + TEXT_TYPE + COMMA_SEP +
                    QRInfo.COLUMN_NAME_QR_CODE + TEXT_TYPE + COMMA_SEP +
                    QRInfo.COLUMN_NAME_QR_TYPE + TEXT_TYPE + COMMA_SEP +
                    QRInfo.COLUMN_NAME_QR_TYPED_ID + TEXT_TYPE +
            " )";

    public static final String SQL_CREATE_ACHIEVEMENTS =
            "CREATE TABLE " + Achievements.TABLE_NAME + " (" +
                    Achievements._ID + PRIMARY_KEY_TYPE + COMMA_SEP +
                    Achievements.COLUMN_NAME_ACHIEVEMENT_ID + TEXT_TYPE + COMMA_SEP +
                    Achievements.COLUMN_NAME_ACHIEVEMENT_NAME + TEXT_TYPE + COMMA_SEP +
                    Achievements.COLUMN_NAME_ACHIEVEMENT_GET + TEXT_TYPE + COMMA_SEP +
                    Achievements.COLUMN_NAME_ACHIEVEMENT_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
                    Achievements.COLUMN_NAME_LOGO_ID + TEXT_TYPE + COMMA_SEP +
                    Achievements.COLUMN_NAME_DAY_ACHIEVED + TEXT_TYPE + COMMA_SEP +
                    Achievements.COLUMN_NAME_VISIBLE_BEFORE_GET + TEXT_TYPE + COMMA_SEP +
                    Achievements.COLUMN_NAME_ACHIEVEMENT_WEIGHT + TEXT_TYPE + COMMA_SEP +
                    Achievements.COLUMN_NAME_SECRET_BEFORE_GET + TEXT_TYPE + COMMA_SEP +
                    Achievements.COLUMN_NAME_ACHIEVEMENT_SOUND + TEXT_TYPE +
            " )";
    public static final String SQL_CREATE_REQUIREMENTS =
            "CREATE TABLE " + Requirements.TABLE_NAME + " (" +
                    Requirements._ID + PRIMARY_KEY_TYPE + COMMA_SEP +
                    Requirements.COLUMN_NAME_REQUIREMENT_ID + TEXT_TYPE + COMMA_SEP +
                    Requirements.COLUMN_NAME_ACHIEVEMENT_ID + TEXT_TYPE + COMMA_SEP +
                    Requirements.COLUMN_NAME_REQUIREMENT_DONE + TEXT_TYPE + COMMA_SEP +
                    Requirements.COLUMN_NAME_DESCRIPTION + TEXT_TYPE +
            " )";

    public static final String SQL_CREATE_LOCATIONS =
            "CREATE TABLE " + Locations.TABLE_NAME + " (" +
                    Locations._ID + PRIMARY_KEY_TYPE + COMMA_SEP +
                    Locations.COLUMN_NAME_LOCATION_ID + TEXT_TYPE + COMMA_SEP +
                    Locations.COLUMN_NAME_LOCATION_NAME + TEXT_TYPE + COMMA_SEP +
                    Locations.COLUMN_NAME_LOCATION_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
                    Locations.COLUMN_NAME_LOCATION_X + TEXT_TYPE + COMMA_SEP +
                    Locations.COLUMN_NAME_LOCATION_Y + TEXT_TYPE +
            " )";

    public static final String SQL_CREATE_STATS_INFORMATION =
            "CREATE TABLE " + StatsInformation.TABLE_NAME + " (" +
                    StatsInformation._ID + PRIMARY_KEY_TYPE + COMMA_SEP +
                    StatsInformation.COLUMN_NAME_CURRENT_DISTANCE + TEXT_TYPE + COMMA_SEP +
                    StatsInformation.COLUMN_NAME_CURRENT_PACE + TEXT_TYPE + COMMA_SEP +
                    StatsInformation.COLUMN_NAME_CURRENT_DURATION + TEXT_TYPE + COMMA_SEP +
                    StatsInformation.COLUMN_NAME_CUMMULATIVE_DISTANCE + TEXT_TYPE + COMMA_SEP +
                    StatsInformation.COLUMN_NAME_CUMMULATIVE_PACE + TEXT_TYPE + COMMA_SEP +
                    StatsInformation.COLUMN_NAME_CUMMULATIVE_DURATION + TEXT_TYPE + COMMA_SEP +
                    StatsInformation.COLUMN_NAME_LIFETIME_DISTANCE + TEXT_TYPE + COMMA_SEP +
                    StatsInformation.COLUMN_NAME_LIFETIME_PACE + TEXT_TYPE + COMMA_SEP +
                    StatsInformation.COLUMN_NAME_LIFETIME_DURATION + TEXT_TYPE +
            " )";

    public static final String SQL_CREATE_SEGMENT_INFORMATION =
            "CREATE TABLE " + SegmentInformation.TABLE_NAME + " (" +
                    SegmentInformation._ID + PRIMARY_KEY_TYPE + COMMA_SEP +
                    SegmentInformation.COLUMN_NAME_SEGMENT_ID + TEXT_TYPE + COMMA_SEP +
                    SegmentInformation.COLUMN_NAME_SEGMENT_NAME + TEXT_TYPE + COMMA_SEP +
                    SegmentInformation.COLUMN_NAME_POINT_A + TEXT_TYPE + COMMA_SEP +
                    SegmentInformation.COLUMN_NAME_POINT_B + TEXT_TYPE + COMMA_SEP +
                    SegmentInformation.COLUMN_NAME_SEGMENT_LENGTH + TEXT_TYPE + COMMA_SEP +
                    SegmentInformation.COLUMN_NAME_SEGMENT_TYPE + TEXT_TYPE +
            " )";

    public static final String SQL_CREATE_SEGMENT_GRAPHIC =
            "CREATE TABLE " + SegmentGraphic.TABLE_NAME + " (" +
                    SegmentGraphic._ID + PRIMARY_KEY_TYPE + COMMA_SEP +
                    SegmentGraphic.COLUMN_NAME_SEGMENT_GRAPHIC_ID + TEXT_TYPE + COMMA_SEP +
                    SegmentGraphic.COLUMN_NAME_SEGMENT_GRAPHIC_NAME + TEXT_TYPE + COMMA_SEP +
                    SegmentGraphic.COLUMN_NAME_SEGMENT_CORNER_X + TEXT_TYPE + COMMA_SEP +
                    SegmentGraphic.COLUMN_NAME_SEGMENT_CORNER_Y + TEXT_TYPE + COMMA_SEP +
                    SegmentGraphic.COLUMN_NAME_SEGMENT_WIDTH + TEXT_TYPE + COMMA_SEP +
                    SegmentGraphic.COLUMN_NAME_SEGMENT_HEIGHT + TEXT_TYPE + COMMA_SEP +
                    SegmentGraphic.COLUMN_NAME_GRAPHIC_LOCATION + TEXT_TYPE + COMMA_SEP +
            " )";
}
