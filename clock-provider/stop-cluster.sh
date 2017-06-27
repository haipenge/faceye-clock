#!/bin/bash
curl -X POST http://cluster-server-001:8885/shutdown
curl -X POST http://cluster-server-002:8886/shutdown