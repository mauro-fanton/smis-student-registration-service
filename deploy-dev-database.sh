#!/bin/sh

databaseContainerName="smis-cassandra-database-service"
cqlScriptPaths="ddl-scripts"
cqlScriptName="create_tables.cql"
docker-compose up -d

until [ "`docker inspect -f {{.State.Health.Status}} $databaseContainerName`"=="healthy" ]; do
    sleep 0.1;
done;

echo "$databaseContainerName up and running"

echo "run script $cqlScriptPaths/$cqlScriptName"
docker exec -i $databaseContainerName cqlsh -t < "$cqlScriptPaths/$cqlScriptName"