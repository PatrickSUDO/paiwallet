db.createUser(
    {
        user: "paiwalletuser",
        pwd: "example",
        roles: [
            {
                role: "readWrite",
                db: "paiwalletdb"
            }
        ]
    }
);

db = new Mongo().getDB("paiwalletdb");

db.createCollection('users', { capped: false });

// Add an initial user (you can add any initial data you want here)
db.users.insert([
    {
        username: "admin",
        password: "hashed_password_here", // remember to hash your password
        email: "admin@paiwallet.com"
    }
]);
