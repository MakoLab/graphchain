#!/bin/bash

# Author: Rafał Trójczak (Rafal.Trojczak@makolab.com)

APP_NAME="mammoth"
USAGE_MSG="Usage: validate__get.sh"

_host="`cat host`"

curl -X GET "$_host/$APP_NAME/validate"


