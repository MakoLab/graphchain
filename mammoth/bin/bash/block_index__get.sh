#!/usr/bin/env bash

# Author: Rafał Trójczak (Rafal.Trojczak@makolab.com)

APP_NAME="mammoth"
USAGE_MSG="Usage: block_index__get.sh <index>"

_index=$1

if [[ -z ${_index} ]]; then
  printf "${USAGE_MSG}\n"
  exit 1
fi

_host="`cat host`"

curl -X GET "${_host}/${APP_NAME}/block/${_index}"

