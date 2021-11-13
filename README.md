# Accountcheck

## About
While playing a match type stauts in the in-game console.
Paste the matching content into a post request to accountcheck.

It will return information about the players:

* Faceit profile
  * Faceit stats
* Steam profile
  * Steam stats

see [Player - model](src/main/groovy/accountcheck/model/Player.groovy) for more info


## Using account check

## Add tokens
To use replace following tokens:

in [FaceitService](grails-app/services/accountcheck/FaceitService.groovy) line 32&52 replace
```faceitToken()``` with your own token.

in [SteamService](grails-app/services/accountcheck/FaceitService.groovy) line 27&47 replace
```faceitToken()``` with your own token.

Please do not add personal tokens to github.
I suggest using ansibel vault and the [ansibleEncrypt.sh](ansibleEncrypt.sh) - It will de/encrypt the file and let git know not, to index the file. 

## Docker
make sure docker is installed. I suggest using:

```docker run -d -p 8181:8181 --restart unless-stopped accountcheck:VERISON```

## Making a request
AccountController will respond to "Post" with 'Content-Type: text/plain'

Exampel:
```
curl --location --request POST 'localhost:8181/accountcheck' \
--header 'Content-Type: text/plain' \
--data-raw '#  9 8 "steamname1" STEAM_1:1:539576123 25:40 40 0 active 786432
# 12 11 "steamname2" STEAM_1:1:27908123 25:40 87 0 active 128000
# 13 12 "steamname3" STEAM_1:0:63253123 25:40 90 0 active 196608
# 14 13 "steamname4" STEAM_1:1:59352123 25:40 109 0 active 196608'
```

## Whats being worked on
see progress here: [Trello - accountcheck](https://trello.com/b/W6L5dKq5/accountcheck)

Please submit any issues through github