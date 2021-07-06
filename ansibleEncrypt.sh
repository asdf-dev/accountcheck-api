#!/bin/bash

File="src/main/groovy/accountcheck/secrets/secrets.groovy"

if grep -q AES256 "$File"; then
  eval "ansible-vault decrypt $File"
  eval "git update-index --assume-unchanged src/main/groovy/accountcheck/secrets/secrets.groovy"
else
  eval "ansible-vault encrypt $File"
fi
