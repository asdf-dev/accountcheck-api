package accountcheck


import accountcheck.model.RequestId

class SteamIdHandler {
    private static final Long STEAM_OFFSET_HEX = 0x0110000100000000
    private static final String STEAM_BASIC_ID_REGEX = "STEAM_\\d:\\d:([0-9]+)"


    List<RequestId> findBasicSteamId(String searchString) {
        List<RequestId> requestList = []
        List<String> baseids = searchString.findAll(STEAM_BASIC_ID_REGEX)

        baseids.each {
            def request = new RequestId(steam64: steam64ID(it))
            requestList.add(request)
        }
        requestList
    }


    private String steam64ID(String basicSteamId) {

        Integer z = Integer.parseInt(basicSteamId.find("([0-9]){2,}"))
        Integer y = Integer.parseInt(basicSteamId.find(":\\d:").replace(":", ""))

        //steamID64=Z*2+OFFSET+Y
        Long result = z * 2 + STEAM_OFFSET_HEX + y
        result.toString()
    }


}
