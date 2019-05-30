cd ..

# Main services first
cd mainframe-service
docker build --rm -f "Dockerfile" -t mainframe-service:latest .

# Under mainframe_offloading
cd ..
cd mainframe_offloading
cd insurance-data-service
docker build --rm -f "Dockerfile" -t insurance-data-service:latest .

cd ..
cd legacy-insurer-portal
docker build --rm -f "Dockerfile" -t legacy-insurer-portal:latest .

cd ..
cd mainframe-cdc-file
docker build --rm -f "Dockerfile" -t mainframe-cdc-file:latest .

cd ..
cd mainframe-simulator
docker build --rm -f "Dockerfile" -t mainframe-simulator:latest .

cd ..
cd modern-insurer-portal
docker build --rm -f "Dockerfile" -t modern-portal:latest .


# Main insurance service
cd ../..
cd insurance-service
docker-compose build
