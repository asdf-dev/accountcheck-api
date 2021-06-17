package accountcheck.model

class Faceit {

    String player_id
    String nickname
    String country
    String new_steam_id
    String steam_id_64
    Games games
    FaceitStats stats
    String faceit_url

}

class Games {

    String game_profile_id
    String region
    String skill_level_label
    String game_player_id
    Integer skill_level
    Integer faceit_elo
    String game_player_name

}

class FaceitStats {
    String Average_Headshots
    String Matches
    String avg_kd_ratio
}