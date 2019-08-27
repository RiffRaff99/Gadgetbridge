package nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr;

public class GarminFitSubtypes {
    public enum FitSubType {
        ACTIVITY(FileDataType.ACTIVITY),
        WORKOUT(FileDataType.WORKOUT),
        COURSE(FileDataType.COURSE),
        WEIGHT(FileDataType.WEIGHT),
        MONITORING_DAILY(FileDataType.MONITORING_DAILY),
        MONITORING_B(FileDataType.MONITORING_B),
        SEGMENT(FileDataType.SEGMENT),
        SEGMENT_LIST(FileDataType.SEGMENT_LIST),
        SLEEP_DATA(FileDataType.SLEEP_DATA),
        USER_BEHAVIOR_LOG(FileDataType.USER_BEHAVIOR_LOG),
        PACE_BAND(FileDataType.PACE_BAND),
        INVALID(FileDataType.INVALID);

        public final int code;

        FitSubType(FileDataType type) {
            this.code = type.code;
        }
    }

    public enum FileDataType {
        DEVICE(1),
        SETTINGS(2),
        SPORT(3),
        ACTIVITY(4),
        WORKOUT(5),
        COURSE(6),
        SCHEDULES(7),
        LOCATIONS(8),
        WEIGHT(9),
        TOTALS(10),
        GOALS(11),
        MAP(12),
        DEBUG(13),
        BLOOD_PRESSURE(14),
        MONITORING_A(15),
        BROADCAST_DEFINITIONS(16),
        UI_QUESTION_LIBRARY(17),
        UI_ACTIVE_QUESTION(18),
        UI_RESPONSE(19),
        ACTIVITY_SUMMARY(20),
        GLUCOSE(21),
        TRACKING_RECORDS(22),
        TRACKING_EVENTS(23),
        VECTOR(25),
        MONITORING_DAILY(28),
        PERSONAL_RECORDS(29),
        DAILY_ALERT(30),
        RAW_DATA(31),
        MONITORING_B(32),
        MULTISPORT(33),
        SEGMENT(34),
        SEGMENT_LIST(35),
        GOLF_SWING(36),
        GOLF_CLUB(37),
        GOLF_SCORECARD(38),
        ADJUSTMENT(39),
        EXD_CONFIGURATION(40),
        SETTINGS_CHANGE_LOG(41),
        IOP(42),
        DOG(43),
        BIOMETRIC_DATA(44),
        BAT_SWING(45),
        ROSTER(46),
        DIVE_PLAN(47),
        HSA_DATA(48),
        SLEEP_DATA(49),
        SOFTWARE_INVENTORY(50),
        CHALLENGE_RESULT(51),
        USER_BEHAVIOR_LOG(52),
        SHOT_ROUND_INFO(53),
        SHOT_DATA(54),
        SHOT_SCORECARD(55),
        PACE_BAND(56),
        MFG_RANGE_MIN(247),
        MFG_RANGE_MAX(254),
        INVALID(255);

        public final int code;

        FileDataType(int code) {
            this.code = code;
        }
    }
}
