A user guide for the app is provided in the repository.
Clone repository

##  Setup database
Install Postgres database you will need pgadmin to edit the databse
copy the database scheme from the repository 
Find Postgres files on your computer
\Program Files\PostgreSQL\11\bin\

Start the database with the command:
```
./pg_ctl.exe -D "C:\Program Files\PostgreSQL\11\data" start
```

## install required packages for the server
Write this command into the commandline
```
npm install express
npm install pg
```

## Insert ip-adress into project

Open the file smartshoppinglist/gradle.properties and assign your ip-adress to HostIp as follows 
```
HostIp=ip-adress:3000
```
On emulator use 10.0.2.2 as ip-adress.
On mobile-phone use your computer local ip-adress
Local ip-adress can be read by writing ipconfig in the console.

## Start json-server:

Open console go into .\project-proposal-walliserwagner\restfull and write
```
node index.js
```