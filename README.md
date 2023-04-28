
# Tintolmarket


Tintolmarket is a CLI Client-Server Distributed Application that represents a Wine Store, similar to _Vivino_, but with
some new features (Clients are able to buy and sell wine).

# How to Compile

- Open a CLI (README directory), run the following command: `javac -d obj src/domain/*.java src/interfaces/*.java`.

# How to run

- Open 1 CLI (README directory or where the TintolmarketServer.jar is located) for running the Server and 1 or more CLI's (README directory or where the Tintolmarket.jar is located) for running 1 or multiple clients at the same time.


- In the Server CLI execute the following command: `java -jar TintolmarketServer.jar {serverPort (optional)} 123456 keystore.server 123456`


- In the Client CLI execute the following command: `java -jar Tintolmarket.jar {serverIP:{serverPort (optional)}}} truststore.clients keystore.{userID} 123456 {userID}`


- **PS:** If you give the server a `serverPort` you need to give the same `serverPort` to the clients.


# How to use TintolmarketClient (Commands)

- `add <wine> <image>` - adds a wine identified by `wine`, associated to the image `image`.


- `sell <wine> <value> <quantity>` - sells a certain quantity `quantity` of wine identified by `wine`, costing `value`
per unit.


- `view <wine>` - shows the wine identified by `wine` informations: image associated, classification, and sellers
  (with available stock) if there are any.


- `buy <wine> <seller> <quantity>` - buys `quantity` units of `wine` from `seller`.


- `wallet` - shows the user's current balance.


- `classify <wine> <stars>` - assigns a rating of 1 to 5 to the `wine`, indicated by `stars`.


- `talk <user> <message>` - allows the user (you) to send a private `message` to `user`.


- `read` - allows the user (you) to read all the received messages and cleans the user's mailbox right after.


- `stop`(extra) - logout, disconnects and stops the client

- `list` - list all transactions in the blockchain

# Limitations

Spaces and Colons are banished from every user input, except:
  - spaces in messages;
  - colons in server initialization `(serverIP:serverPort)`;
  - spaces in between arguments when initializing, either the server or the clients.

# Authors

- Tomás Barreto (fc56282)
- Diogo Pereira (fc56302)
- Bruno Costa (fc56354)