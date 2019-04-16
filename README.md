A user guide for the app is provided in the repository.


Clone repository

## install json-server:

npm json-server

## Start json-server:

### Emulator:

json-server --watch db.json

If the app is run in the emulator use 10.0.2.2 to access your devices localhost.


### Mobile-Phone:

json-server --host <ip-adress> --watch db.json

If the app is run on a mobile-phone make sure the mobile-phone is in the same network as the computer the server is hosted on
and use your local ip-adress.

Local ip-adress can be read by writing ipconfig in the console.


Open the file smartshoppinglist/gradle.properties and assign your ip-adress to HostIp as follows HostIp=<ip-adress>:3000

