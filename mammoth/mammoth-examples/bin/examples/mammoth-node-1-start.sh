#!/bin/bash

cd ~/programming/makolab/bc/mammoth
mvn clean install > /dev/null
cd mammoth-web
mvn jetty:run -Djetty.http.port=8881 \
    -Dp2p.server.port=7001 \
    -Drepository.repositoryUrl=http://localhost:8080/rdf4j-server/repositories/bc-node-1
