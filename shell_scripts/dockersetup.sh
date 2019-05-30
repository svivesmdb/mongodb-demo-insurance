# Obtain the images from docker
sudo docker pull epiclabs/docker-oracle-xe-11g
sudo docker pull postgres

# Create the containers themselves (if you want to create them automatically)
#docker container create postgres 
#docker container create epiclabs/docker-oracle-xe-11g

# Run and expose the containers
sudo docker run --name singleview-pg -p 5432:5432 -e POSTGRES_DB=insurance -e POSTGRES_USER=pguser -e POSTGRES_PASSWORD=mypass postgres & 
sudo docker run --name singleview-ora -p 1521:1521 -e ORACLE_PASSWORD=mypass epiclabs/docker-oracle-xe-11g &