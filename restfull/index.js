const express = require('express')
const bodyParser = require('body-parser')
const app = express()
const db = require('./queries')
const port = 3000

app.use(bodyParser.json())
app.use(
  bodyParser.urlencoded({
    extended: true,
  })
)

app.get('/', (request, response) => {
    response.json({ info: 'Node.js, Express, and Postgres API' })
  })

app.get('/users', db.getUsers)
app.post('/users', db.createUser)

app.post('/itemlist', db.createItem)

app.post('/shoppinglist', db.addItem)
app.delete('/shoppinglist', db.removeItem)
app.post('/createshoppinglist', db.createShoppinglist)
app.delete('/deleteshoppinglist', db.deleteShoppinglist)

app.post('/group', db.createGroup);
app.delete('/group', db.leaveGroup);
app.get('/group', db.getGroupUsers);
app.get('/grouplist', db.getGroups);

app.get('/changeset', db.getGroupChanges);

app.post('/invite', db.sendInvite);
app.get('/invite', db.getInvite);
app.delete('/invite', db.handleInvite);

app.listen(port, () => {
    console.log(`App running on port ${port}.`)
})