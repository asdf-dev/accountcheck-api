package accountcheck.model

class Faceit {

    String player_id
    String nickname
    String country
    String status
    boolean verified
    String avatar
    List<Games> games
    FaceitStats faceitStats
    String faceit_url

}

class Games {

    String name
    Integer skill_level

}

class FaceitStats {
    String Average_Headshots
    String Matches
    String avg_kd_ratio
}