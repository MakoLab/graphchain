#!/usr/bin/env bash

USAGE_MSG="Usage: broadcast-message.sh <message-type> [<port>]"

_message_type=$1
_port=$2

if [ -z "${_message_type}" ]; then
  printf "${USAGE_MSG}\n"
  exit 1
else
  _message_type_param="?messageType=${_message_type}"
fi

if [ -z "${_port}" ]; then
  _port="8881"
fi

curl -X GET "http://localhost:${_port}/mammoth/peer/broadcast?${_message_type_param}"