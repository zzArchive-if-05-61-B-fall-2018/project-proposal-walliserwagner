const Pool = require('pg').Pool
const pool = new Pool({
  user: 'postgres',
  host: 'localhost',
  database: 'postgres',
  password: 'password',
  port: 5432,
})


const getUsers = (request, response) => {
    const email = request.query.email;
    const password = request.query.password;
    if(password===undefined){
        console.debug('SELECT * FROM users where email=\''+email+'\';');
        pool.query('SELECT * FROM users where email=\''+email+'\';', (error, results) => {
            if (error) {
              throw error
            }
            response.status(200).json(results.rows)
          })
    }
    else{
        console.debug('SELECT * FROM users where email=\''+email+'\' and password=\''+password+'\';');
        pool.query('SELECT * FROM users where email=\''+email+'\' and password=\''+password+'\';', (error, results) => {
            if (error) {
              throw error
            }
            response.status(200).json(results.rows)
            
          })
    }
  }

const createUser = (request, response) => {
  const { name, email, password} = request.body
  let groupid, userid;
  console.debug('INSERT INTO users (name, email, password) VALUES ($1, $2, $3)', [name, email, password]);
  pool.query('INSERT INTO users (name, email, password) VALUES ($1, $2, $3)', [name, email, password], (error, results) => {
      if (error) {
      throw error
      }
      pool.query('select max(userid) from users;', (error1, useridres) => {
        if (error1) {
          throw error1
        }
        const userid = useridres.rows[0]['max'];
        })
      response.status(201).send(`User added with ID: ${results.rows}`)
  })  
}

const createItem = (request, response) => {
  const { userid, name, defunit, category} = request.body;
  console.debug('insert into item (userid, name, defunit) values('+userid+', $1, $2, $3);',[name, defunit, category]);
  pool.query('INSERT INTO item (userid, name, defunit, category) VALUES('+userid+', $1, $2, $3);',[name, defunit, category], (error, result) =>{
    if(error){
      throw error
    }  
    console.debug(result.rows);
    response.status(201).send(result.rows);
  })
}

const addItem = (request, response) => {

}

const createShoppinglist = (request, response) => {
  const {groupid, name} = request.body;
}

const createGroup = (request, response) =>{
  const {userid, name} = request.body;
  let groupid;
  pool.query('INSERT INTO "group" (name) VALUES ($1);',[name], (error2, results) => {
    if (error2) {
      throw error2
    }
    pool.query('select max(groupid) from "group";', (error3, groupidres) => {
      if (error3) {
        throw error3
      }
      groupid = groupidres.rows[0]['max'];
      console.debug(groupidres.rows[0]['max']);
      pool.query('INSERT INTO "member" (groupid, userid) VALUES ('+groupid+', '+userid+');', (error4, results2) => {
        if (error4) {
          throw error4
        }
        console.debug(groupid);
        response.status(201).send('{"groupid":"'+groupid+'"}') 
      })
    })
  })
}

  module.exports = {
      getUsers,
      createUser,
      createItem,
      addItem,
      createGroup,
  }