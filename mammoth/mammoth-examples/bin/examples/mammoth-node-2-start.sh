#!/bin/bash

cd ~/programming/makolab/bc/mammoth
#mvn clean install > /dev/null
cd mammoth-web
mvn jetty:run -Djetty.http.port=8883 \
    -Dp2p.server.port=7003 \
    -Drepository.repositoryUrl=http://localhost:8080/rdf4j-server/repositories/bc-node-2
