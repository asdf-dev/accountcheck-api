package accountcheck


class SteamIdHandler {
    private static final Long STEAM_OFFSET_HEX = 0x0110000100000000
    private static final String STEAM_BASIC_ID_REGEX = "STEAM_\\d:\\d:([0-9]+)"
    private static final String STEAM_BASIC_REGEX = "([\"'])(?:(?=(\\\\?))\\2.)*?\\1 STEAM_\\d:\\d:([0-9]+)"
    private static final String STEAM_NAME = "([\"'])(?:(?=(\\\\?))\\2.)*?\\1"

//todo I need unit test!
    private Long steam64ID(String basicSteamId) {

        Integer z = Integer.parseInt(basicSteamId.find("([0-9]){2,}"))
        Integer y = Integer.parseInt(basicSteamId.find(":\\d:").replace(":", ""))

        //steamID64=Z*2+OFFSET+Y
        Long result = z * 2 + STEAM_OFFSET_HEX + y
        result
    }

    Map<String, String> findBasicSteamId(String status) {
        List<String> basicList = []
        status.eachMatch(STEAM_BASIC_REGEX) {
            basicList.add(it.first())
        }
        statusToBasicIdHelper(basicList)
    }

    private Map<String, String> statusToBasicIdHelper(List<String> status) {
        Map<String, String> basicSteam = [:]

        status.each {
            basicSteam.put(steam64ID(it.find(STEAM_BASIC_ID_REGEX)), it.find(STEAM_NAME).replace("\"", ""))
        }
        basicSteam
    }


}
