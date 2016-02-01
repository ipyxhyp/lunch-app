# Lunch reservation REST service

Allows users/clients to make a lunch reservations from a daily menu dishes at specific restaurant and on particular date.

Available exposed REST operations are :

* create role -POST-
* create user -POST-
* create restaurant (only for role admin) -POST-
* create menu item  (only for role admin) -POST-
* create menu item list (only for role admin) -POST-
* create daily menu -POST-
* create daily menu list -POST-
* create daily Order -POST-
* update daily Order -POST-
* get daily menu -GET-


Before run and execute REST API commands verify to have installed DB and defined db driver, url , username , password property values in the db.properties file located at src/main/resources
During the mvn clean install all required dependencies will be fetched.
DB schema is created while app is deployed to app server.
