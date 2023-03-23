
# Tintolmarket


Tintolmarket is a CLI Client-Server Distributed Application that represents a Wine Store, similar to _Vivino_, but with
some new features (Clients are able to buy and sell wine).

- - -

# How to run

- Open 1 CLI for running the Server and 1 or more CLI's for running 1 or multiple clients at the same
time.


- `cd "path where the .jar [ Tintolmarket.jar (Client) / TintolmarketServer.jar (Server) ] is located"`


- In the Server CLI execute the following command: `java -jar TintolmarketServer.jar {serverPort (optional)}`


- In the Client CLI execute the following command: `java -jar Tintolmarket.jar {serverIP:{serverPort (optional)}}} {username} {password (optional)}`


- **PS:** If you give the server a `serverPort` you need to give the same `serverPort` to the clients.
- - -

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

- - -

# Limitations

Spaces and Colons are banished from every user input, except:
  - saces in messages;
  - colons in server initialization `(serverIP:serverPort)`;
  - spaces in between arguments when initializing, either the server or the clients.

- - -

# Authors

- Tomás Barreto (fc56282)
- Diogo Pereira (fc56302)
- Bruno Costa (fc56354)