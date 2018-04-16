## Screens

### Create Car Insurance Policy

With the "Create Car Insurance Policy' screen a new policy can be created. The form cannot be submitted until all validation errors are fixed.

![create-car-insurance-policy-screen](doc/create-car-insurance-policy-screen.png)

### Create Car Insurance Claim

With the "Create Car Insurance Claim' screen a new claim can be created. The form cannot be submitted until all validation errors are fixed.

![create-car-insurance-claim-screen](doc/create-car-insurance-claim-screen.png)

## Development mode

Run the application.
Notes:
* Assumption is that the first command is executed from the directory `<repo-root>/mainframe_offloading/legacy-insurer-portal`
* `npm install` is only required for the first time or when dependencies change

```
host$ docker run -it --rm -v $(pwd):/home/app -p 3000:3000  node:9.5 /bin/bash
container$ cd /home/app
container$ npm install
container$ npm start
```

Open a browser pointing at http://localhost:3000/

## Production build

In order to create a production build run the following command from the directory https://github.com/ckurze/mongodb-demo-insurance/tree/master/mainframe_offloading/legacy-insurer-portal. This will create a folder named build

```
host$ docker run -it --rm -v $(pwd):/home/app -w /home/app -p 3000:3000  node:9.5 -c "npm install; npm run build"
host$ tree -L 2 build/
build/
├── asset-manifest.json
├── favicon.ico
├── index.html
├── manifest.json
├── service-worker.js
└── static
    ├── css
    ├── js
    └── media
```

docker run -it --rm -v $(pwd):/home/app -w /home/app -p 3000:3000  node:9.5 -c "npm install; npm run build"
### Package Docker container

**Note: This is not ideal, as it starts the development server. The better way is to use a HTTP server like nginx with the production build**

The repository includes a Dockerfile to package the legacy-insurer-portal as container.

Replace `mentlsve/legacy-insurer-portal` with whatever name you like:

```
host$ cd packaging
host$ docker build -t mentlsve/legacy-insurer-portal .
```
Then run the application with
```
host$ docker run -d -p 3000:3000 --name=legacy-insurer-portal mentlsve/legacy-insurer-portal
```

Open a browser pointing at http://localhost:3000/

Kill the container with
```
host$ docker rm -f legacy-insurer-portal
```

## Run on minikube

The repository includes Kubernetes manifests to create a deployment and expose a service.

Deploying to minikube
```
host$ minikube start
host$ kubectl apply -f packaging/deployment.yaml
host$ kubectl apply -f packaging/service.yaml
host$ minikube ip
192.168.99.100
```

You should then be able to open a browser and reach the application on http://192.168.99.100:30100



