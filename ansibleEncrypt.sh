#!/bin/bash

File="src/main/groovy/accountcheck/secrets/secrets.groovy"

if grep -q AES256 "$File"; then
  eval "ansible-vault decrypt $File"
else
  eval "ansible-vault encrypt $File"
fi
