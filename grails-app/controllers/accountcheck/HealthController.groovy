package accountcheck


import java.util.concurrent.TimeUnit

import static accountcheck.BootStrap.BOOT_TIME

class HealthController {
    static responseFormats = ['json']

    def health() {
        Long now = new Date().time
        String aliveTime = timeToHours(now - BOOT_TIME.time)

        render(view: "health", model: [upTime: aliveTime])
    }


    protected static String timeToHours(Long millis) {
        long hours = TimeUnit.MILLISECONDS.toHours(millis)
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1)
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1)

        return String.format("%02d:%02d:%02d", Math.abs(hours), Math.abs(minutes), Math.abs(seconds))

    }

}
