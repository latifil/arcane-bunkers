package me.latifil.bunkers.util;

public class TimeUtil {

    public static String formatSeconds(int totalSeconds) {
        if (totalSeconds < 3600) {
            int minutes = (totalSeconds - (totalSeconds % 60)) / 60;
            int seconds = totalSeconds % 60;
            return ((minutes < 10) ? "0" + minutes : String.valueOf(minutes)) + ":" + ((seconds < 10) ? "0" + seconds : String.valueOf(seconds));
        } else if (totalSeconds > 3600 && totalSeconds < 3600 * 24) {
            int minutes = (totalSeconds / 60) % 60;
            int hours = (totalSeconds / 60) / 60;
            int seconds = totalSeconds % 60;
            return hours + ":" + ((minutes < 10) ? "0" + minutes : String.valueOf(minutes)) + ":" + ((seconds < 10) ? "0" + seconds : String.valueOf(seconds));
        }
        return "00:00";
    }
}
