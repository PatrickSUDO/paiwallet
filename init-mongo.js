db = new Mongo().getDB("paiwalletdb");
db.createUser(
    {
        user: "psu",
        pwd: "psu",
        roles: [
            {
                role: "readWrite",
                db: "paiwalletdb"
            }
        ]
    }
);

db.createCollection('users', { capped: false });

// Add an initial user (you can add any initial data you want here)
db.users.insertOne([
    {
        username: "admin",
        password: "password", // remember to hash your password
        email: "admin@paiwallet.com"
    }
]);

// Show all users and databases
var allUsers = db.getUsers();
printjson(allUsers);

var allDatabases = db.adminCommand({ listDatabases: 1 });
printjson(allDatabases);
