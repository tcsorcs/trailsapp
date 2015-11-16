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
		public static final String COLUMN_NAME_QR_ID = "_id";
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
		public static final String COLUMN_NAME_LOCATION_ID = "_id";
		public static final String COLUMN_NAME_LOCATION_NAME = "locationname";
		public static final String COLUMN_NAME_LOCATION_DESCRIPTION = "locationdescription";
		public static final String COLUMN_NAME_LOCATION_X = "locationx";
		public static final String COLUMN_NAME_LOCATION_Y = "locationy";
		public static final String COLUMN_NAME_SIDE_OF_ROAD = "sideofroad";
	}

	public static abstract class StatsInformation implements BaseColumns {
		public static final String TABLE_NAME = "stats";
		public static final String COLUMN_NAME_CURRENT_DISTANCE = "currentdistance";
		public static final String COLUMN_NAME_CURRENT_PACE = "currentpace";
		public static final String COLUMN_NAME_CURRENT_DURATION = "currentduration";
		public static final String COLUMN_NAME_LIFETIME_DISTANCE = "lifetimedistance";
		public static final String COLUMN_NAME_LIFETIME_PACE = "lifetimepace";
		public static final String COLUMN_NAME_LIFETIME_DURATION = "lifetimeduration";
		public static final String COLUMN_NAME_CUMULATIVE_DISTANCE = "cumulativedistance";
		public static final String COLUMN_NAME_CUMULATIVE_PACE = "cumulativepace";
		public static final String COLUMN_NAME_CUMULATIVE_DURATION = "cumulativeduration";
	}

	public static abstract class SegmentInformation implements BaseColumns {
		public static final String TABLE_NAME = "segments";
		public static final String COLUMN_NAME_SEGMENT_ID = "_id";
		public static final String COLUMN_NAME_SEGMENT_NAME = "segmentname";
		public static final String COLUMN_NAME_POINT_A = "pointa";
		public static final String COLUMN_NAME_POINT_B = "pointb";
		public static final String COLUMN_NAME_SEGMENT_LENGTH = "segmentlength";
		public static final String COLUMN_NAME_SEGMENT_TYPE = "segmenttype";
		public static final String COLUMN_NAME_IS_ENTRANCE = "isentrance";
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
}