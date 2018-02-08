#!/usr/bin/env bash

curl -X POST -H 'Content-Type: application/json' --data '{"peerAddress": "http://localhost:7003"}' http://localhost:8881/mammoth/peer/add