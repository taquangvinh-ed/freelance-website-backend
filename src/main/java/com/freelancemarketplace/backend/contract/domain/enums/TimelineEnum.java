/**
 * Timeline Constants & Mappings
 * Chuẩn hóa timeline: text -> days
 */

package com.freelancemarketplace.backend.contract.domain.enums;

import java.util.HashMap;
import java.util.Map;

public enum TimelineEnum {
    LESS_THAN_1_MONTH("Less than 1 month", 21),
    ONE_TO_3_MONTHS("1 to 3 months", 60),
    THREE_TO_6_MONTHS("3 to 6 months", 120),
    MORE_THAN_6_MONTHS("More than 6 months", 210);

    private final String display;
    private final int days;

    TimelineEnum(String display, int days) {
        this.display = display;
        this.days = days;
    }

    public String getDisplay() {
        return display;
    }

    public int getDays() {
        return days;
    }

    /**
     * Map text to enum
     */
    private static final Map<String, TimelineEnum> LOOKUP = new HashMap<>();

    static {
        for (TimelineEnum e : TimelineEnum.values()) {
            LOOKUP.put(e.display, e);
        }
    }

    public static TimelineEnum fromDisplay(String display) {
        return LOOKUP.get(display);
    }

    public static int getDaysFromDisplay(String display) {
        TimelineEnum timeline = fromDisplay(display);
        return timeline != null ? timeline.days : 30;
    }
}

