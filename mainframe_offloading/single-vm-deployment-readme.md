# Deployment on AWS EC2 & S3

## VM preparation

Launch a new instance with the Amazon Linux AMI and install docker and git

```
ec2$ sudo yum update -y
ec2$ sudo yum install docker -y
ec2$ sudo service docker start
ec2$ sudo usermod -a -G docker ec2-user
ec2$ sudo yum install git -y
```

Log out from the VM and then log in again so that your user ec2-user can run `docker` without `sudo`.

Clone the repository

```
ec2$ git clone https://github.com/ckurze/mongodb-demo-insurance
ec2$ ls
mongodb-demo-insurance
ec2$ pwd
/home/ec2-user
```
## Run the mainframe simulator

Change to the directory of the `mainframe-simulator`

```
ec2$ cd /home/ec2-user/mongodb-demo-insurance/mainframe_offloading/mainframe-simulator/
```

Create the `mainframe-simulator` jar

```
ec2$ docker run --name maven-container --rm -v $(pwd):/home/app -w /home/app maven:3.5-jdk-8 /bin/bash -c "mvn package"
ec2$ ls target | grep jar
mainframe-simulator-0.0.1.jar
mainframe-simulator-0.0.1.jar.original
```

Package the jar in a docker container

```
ec2$ docker build -t mainframe-simulator .
```

Run the `mainframe-simulator`

```
ec2$ docker run -d --rm \
--name mainframe-simulator \
-p 8080:8080  \
-v $(pwd)/sample-data:/home/insurance-data \
-e mainframe.repository.base=/home/insurance-data/mainframe \
mainframe-simulator
```
The `mainframe-simulator` is then available on `http://<public ip>:8080`

## Run the insurance data service

Change to the directory of the `insurance-data-service`

```
ec2$ cd /home/ec2-user/mongodb-demo-insurance/mainframe_offloading/insurance-data-service/
```

Create the `insurance-data-service` jar

```
ec2$ docker run --name maven-container --rm -v $(pwd):/home/app -w /home/app maven:3.5-jdk-8 /bin/bash -c "mvn package"
```

Package the jar in a docker container

```
docker build -t insurance-data-service .
```

Run the `insurance-data-service`

*NOTE: REPLACE THE VALUES FOR THE `SPRING_DATA_MONGODB_URI` and `SPRING_DATA_MONGODB_DATABASE`*

```
docker run -d --rm \
--name insurance-data-service \
-p 8081:8080 \
-e SPRING_DATA_MONGODB_URI=mongodb+srv://<username>:<password>@<host> \
-e SPRING_DATA_MONGODB_DATABASE=insurance \
insurance-data-service
```

The `insurance-data-service` is then available on `http://<public ip>:8081`

## Deploy the legacy-insurer-portal

Before building the package to be uploaded to S3, change the endpoint of the API server. For this deployment option it will be the public IP of the VM prepared in the previous steps.

* Update the endpoint (to `http://<public ip>:8080`) in https://github.com/ckurze/mongodb-demo-insurance/blob/master/mainframe_offloading/legacy-insurer-portal/src/APIUtil.js

* Create the production build as described here https://github.com/ckurze/mongodb-demo-insurance/tree/master/mainframe_offloading/legacy-insurer-portal#production-build

* Upload the content of the build folder to the S3 bucket and make the elements public. Have a look here https://docs.aws.amazon.com/AmazonS3/latest/dev/HowDoIWebsiteConfiguration.html in case you need further instructions

## Deploy the modern-insurer-portal

Before building the package to be uploaded to S3, change the endpoint of the API server. For this deployment option it will be the public IP of the VM prepared in the previous steps.

* Update the endpoint (to `http://<public ip>:8081`) in https://github.com/ckurze/mongodb-demo-insurance/blob/master/mainframe_offloading/modern-insurer-portal/src/APIUtil.js

* Create the production build as described here https://github.com/ckurze/mongodb-demo-insurance/tree/master/mainframe_offloading/modern-insurer-portal#production-build

* Upload the content of the build folder to the S3 bucket and make the elements public. Have a look here https://docs.aws.amazon.com/AmazonS3/latest/dev/HowDoIWebsiteConfiguration.html in case you need further instructions