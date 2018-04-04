# Deploy the demo in a single VM

This description assumes that you just want to run the demo and therefore reuse the published Docker images. Of course, you can build the images on your own, detailed build instructions are given in the README for every component.

1. Create a VM on a public cloud provider. For simplicity choose an image which has Docker pre-installed (e.g. the [container-optimized OS](https://cloud.google.com/container-optimized-os/docs/) on Google Cloud Platform or Container Linux by CoreOS on AWS). Make sure that the VM has a public IP, and allows HTTP/S traffic. Setup SSH keys so you can connect from your host.

2. For going further you should check that you can connect to the VM and that docker is installed (*my-private-key* should be the proper path to YOUR private key you configured the instance with, obviously *mentlsve* and *1.2.3.4* need to be adapted)

    ```
    host$ ssh -i my-private-key mentlsve@1.2.3.4
    Enter passphrase for key 'my-private-key':
    username@mainframe-offloading-demo-vm ~ $  docker -v
    Docker version 17.03.2-ce, build f5ec1e2
    ```

3.  Create the directory containing the insurance-data (part of mainframe simulator)

    ```
    vm$ sudo mkdir -p /home/insurance-data
    vm$ sudo chmod -R 777 /home/insurance-data
    ```

    It's not ideal to grant full access to everyone (using groups would be better) but for this demo we can live with it.

4. Copy the files from your host to the VM.
   ```
   host$ scp -i ~/.ssh/gcloud-acic -r ./mainframe-simulator/sample-data/. sven.mentl@35.198.132.129:/home/insurance-data
   vm$ ls /home/insurance-data/
   customer  home  motor
   ```

5. Run the mainframe simulator
    ```
    docker run -d --name mainframe-simulator -p 8081:8080 -v /home/insurance-data/:/home/app/sample-data/ mentlsve/mainframe-simulator
    ```

6. When making a request against the following url
   http://<public-ip>:8081/policies you should get a large JSON object back.

7. Deploy the web interface

[WIP]