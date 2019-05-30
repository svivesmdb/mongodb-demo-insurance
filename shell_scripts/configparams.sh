# Grab the current ports of the machines for oracle and postgres from docker
ORACLE_CONTAINER_ID=$(docker ps -aqf "name=singleview-ora")
POSTGRES_CONTAINER_ID=$(docker ps -aqf "name=singleview-pg")
ORACLE_PORT=$(docker port $ORACLE_CONTAINER_ID 1521)
POSTGRES_PORT=$(docker port $POSTGRES_CONTAINER_ID 5432)
ORACLE_PORT=$(echo $ORACLE_PORT | sed "s/0.0.0.0://")
POSTGRES_PORT=$(echo $POSTGRES_PORT | sed "s/0.0.0.0://")

re='^[0-9]+$'
if ! [[ $ORACLE_PORT =~ $re ]] ; then
   echo "Error: The port obtained for the Oracle container is not numeric, make sure you've run make.sh and that docker is running" >&2; exit 1
fi

if ! [[ $POSTGRES_PORT =~ $re ]] ; then
   echo "Error: The port obtained for the Postgres container is not numeric, make sure you've run make.sh and that docker is running" >&2; exit 1
fi

echo "✅  Oracle container (singleview-ora) is $ORACLE_CONTAINER_ID and the port it exposes is $ORACLE_PORT"
echo "✅  PostgreSQL container (singleview-pg) is $POSTGRES_CONTAINER_ID and the port it exposes is $POSTGRES_PORT"

