#!/bin/bash

# Author: Rafał Trójczak (Rafal.Trojczak@makolab.com)

APP_NAME="mammoth"
USAGE_MSG="Usage: block_create__post.sh <graph-iri> <turtle-path>"

_host="`cat host`"
_graph_iri=$1
_file_path=$2

if [ -z "${_graph_iri}" ] || [ -z "${_file_path}" ]; then
  printf "${USAGE_MSG}\n"
  exit 1
fi

printf "[CURL] ${_host}/${APP_NAME}/block/create?graphIri=${_graph_iri} -d @${_file_path} -H \"Content-Type: text/turtle\"\n"

curl "$_host/$APP_NAME/block/create?graphIri=${_graph_iri}" \
       -d "@${_file_path}" \
       -H "Content-Type: text/turtle"