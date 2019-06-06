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
  const {userid, groupid, listname, itemname, amount, unit, category} = request.body
  console.debug('Insert into itemcontainer (userid, unit, count, shoppinglistid, category, name) values('+userid+', $1, '+amount+', (select shoppinglistid from shoppinglist where groupid='+groupid+' and name=$2), $3, $4);',[unit,listname, category, itemname]);
  pool.query('Insert into itemcontainer (userid, unit, count, shoppinglistid, category, name) values('+userid+', $1, '+amount+', (select shoppinglistid from shoppinglist where groupid='+groupid+' and name=$2), $3, $4);',[unit,listname, category, itemname], (error, result)=>{
    if(error){
      throw error
    }
    response.status(201).send(result.rows);
  })  
}

const removeItem = (request,response) => {
  const userid=request.query.userid, groupid=request.query.groupid
  const listname=request.query.listname
  const itemname=request.query.itemname
  const unit=request.query.unit
  const category=request.query.category
  console.debug('Delete from itemcontainer where userid='+userid+' and name=$1 and shoppinglistid=(select shoppinglistid from shoppinglist where name=$2 and groupid='+groupid+') and unit=$3 and category=$4;',[itemname,listname,unit, category])
  pool.query('Delete from itemcontainer where userid='+userid+' and name=$1 and shoppinglistid=(select shoppinglistid from shoppinglist where name=$2 and groupid='+groupid+') and unit=$3 and category=$4;',[itemname,listname,unit, category], (error, result) => {
    if(error){
      throw error
    }
    response.status(202).send(result.rows)
  })
}

const deleteShoppinglist = (request,response)=>{
  const groupid=request.query.groupid, listname=request.query.listname
  pool.query('Delete from itemcontainer where shoppinglistid=(select shoppinglistid from shoppinglist where groupid='+groupid+' and name=$1);',[listname], (error, result) =>{
    if(error){
      throw error
    }
    pool.query('Delete from shoppinglist where groupid='+groupid+' and name=$1',[listname], (error1, result1) => {
      if(error1){
        throw error1
      }
      response.status(202).send(result1.rows);
    })
  })
}

const leaveGroup = (request, response) => {
  const userid=request.query.userid, groupid=request.query.groupid;
  pool.query('Delete from "member" where groupid='+groupid+' and userid='+userid+';', (error, result) =>{
    if(error){
      throw error
    }
    response.status(202).send(result.rows);
  })
}

const createShoppinglist = (request, response) => {
  const {groupid, name} = request.body;
  pool.query('Insert into shoppinglist (groupid, name) VALUES ('+groupid+', $1);',[name], (error, result) =>{
    if(error){
      throw error
    }
    pool.query('Select max(shoppinglistid) from shoppinglist;', (error, shoppinglistres) =>{
      if(error){
        throw error
      }
      console.debug('{"shoppinglistid":"'+shoppinglistres.rows[0]['max']+'"}')
      response.status(201).send('{"shoppinglistid":"'+shoppinglistres.rows[0]['max']+'"}');
    })
    
  })
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

const enterGroup = (request, response) => {
  const {userid, groupid} = request.body;
  pool.query('insert into "member" (groupid, userid) values('+groupid+', '+userid+');', (error, result) =>{
    if(error){
      throw error;
    }
    response.send(201).send(result.rows);
  })
}

const sendInvite = (request, response) => {
  const {senderid, receiveremail, groupid} = request.body;
  pool.query('insert into invite (receiverid, senderid, groupid) values((select userid from users where email=$1),'+senderid+', '+groupid+');',[receiveremail], (error, result) => {
    if(error){
      throw error;
    }
    response.send(result.rows);
  })
}

const getInvite = (request, response) => {
  const userid = request.query.userid;
  pool.query('select invite.groupid, users.email, "group".name from invite, users, "group" where "group".groupid = invite.groupid and invite.senderid = users.userid and invite.receiverid = '+userid+';', (error, result) => {
    if(error){
      throw error
    }
    console.debug(result.rows);
    response.send(result.rows);
  })
}

const handleInvite = (request, response) => {
  const userid = request.query.userid, groupid = request.query.groupid, accepted = request.query.accepted;
  if(accepted === 'true'){
      console.debug('insert into "member" values('+groupid+', '+userid+')')
      pool.query('insert into "member" values('+groupid+', '+userid+');' , (error, result)=>{
        if(error){
          throw error;
        }
        console.debug('delete from invite where groupid='+groupid+';');
        pool.query('delete from invite where groupid='+groupid+';', (err, res) =>{
          if(err){
            throw err
          }
          response.send(202)
        })
      })
  }
  else {
    console.debug('delete from invite where groupid='+groupid+';');
    pool.query('delete from invite where groupid='+groupid+';', (err, res) =>{
      if(err){
        throw err
      }
      response.send(202)
    })
  }
}


  module.exports = {
      getUsers,
      createUser,
      createItem,
      addItem,
      createGroup,
      createShoppinglist,
      removeItem,
      deleteShoppinglist,
      leaveGroup,
      handleInvite,
      sendInvite,
      getInvite
  }