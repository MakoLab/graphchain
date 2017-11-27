#!/usr/bin/env bash

# Author: Rafał Trójczak (Rafal.Trojczak@makolab.com)

APP_NAME="mammoth"
USAGE_MSG="Usage: populate__chain.sh <directory>"

_host="`cat host`"

for file_path in $* ; do
  graph_iri="$(grep '^@base' "${file_path}" | perl -p -e 's/^.*\<graph([^\>]+)\>.*$/http\1/g')"

  ./block_create__post.sh ${graph_iri} ${file_path}
done