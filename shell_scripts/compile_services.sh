cd ..

mvn install:install-file \
   -Dfile=./lib/ojdbc8.jar \
   -DgroupId=com.oracle \
   -DartifactId=ojdbc8 \
   -Dversion=12.2.0.2 \
   -Dpackaging=jar \
   -DgeneratePom=true

# Main services first
cd mainframe-service
mvn clean -Dmaven.test.skip=true package

# Under mainframe_offloading
cd ..
cd mainframe_offloading
cd insurance-data-service
mvn clean -Dmaven.test.skip=true package

cd ..
cd mainframe-cdc-file
mvn clean -Dmaven.test.skip=true package

cd ..
cd mainframe-simulator
mvn clean -Dmaven.test.skip=true package
