package accountcheck.model


class Steam {


    /**
     * mulighed for ctor der kan lave obj
     * custom binder tager imod map og smider dem på obj
     * lige nu er alt lowercase ad
     */

    String steamid

    String playerName
    String loccountrycode
    String profileurl
    String avatar
    String avatarmedium
    String avatarfull
    BigInteger lastlogoff
    Integer accountAge
    Integer communityvisibilitystate
    Integer game_count


}
