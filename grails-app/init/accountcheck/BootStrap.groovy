package accountcheck

class BootStrap {

    public static final Date BOOT_TIME = new Date()

    def init = { servletContext ->
        BOOT_TIME.time
    }
    def destroy = {
    }
}
