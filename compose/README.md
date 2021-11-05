#### Install docker compose on your laptop 
https://phoenixnap.com/kb/install-docker-compose-ubuntu

#### Start service
In order to create the necessary packages
run the prepare.sh file with the root permission
```shell
sudo ./prepare.sh
```

Back to the project package and run command

```shell
cd ..
mvn clean packege
```

After this, create docker image

```shell
sudo docker build -t stage-payment-system .
```

Back to compose directory and start service 

```shell
cd compose
docker-compose up 
```