cd ..

docker rm --force insurance-data-service
docker rm --force legacy-insurer-portal
docker rm --force mainframe-cdc-file
docker rm --force mainframe-simulator
docker rm --force modern-portal
docker rm --force insurance-service

# Main services first
cd mainframe-service
docker run mainframe-service:latest &
cd ..
cd insurance-service
docker-compose up &

# Under mainframe_offloading
cd ..
cd mainframe_offloading
cd insurance-data-service
docker run --name insurance-data-service insurance-data-service:latest   &

cd ..
cd legacy-insurer-portal
docker run --name legacy-insurer-portal legacy-insurer-portal:latest &

cd ..
cd mainframe-cdc-file
docker run --name mainframe-cdc-file mainframe-cdc-file:latest &

cd ..
cd mainframe-simulator
docker run --name mainframe-simulator mainframe-simulator:latest &

cd ..
cd modern-insurer-portal
docker run --name modern-portal modern-portal:latest &


