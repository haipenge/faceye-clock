#!/bin/bash
curl -X POST http://cluster-server-001:8881/shutdown
curl -X POST http://cluster-server-002:8882/shutdown