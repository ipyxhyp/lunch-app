# lunch-app
lunch reservation app

Before run and execute REST API commands it's neccessary to have installed DB and defined db driver, url , username , password property values in the db.properties file located at src/main/resources
During the mvn clean install all required dependencies will be fetched.
DB schema is created while app is deployed to app server.


Commands to execute :
1. To Create Restaurant Daily Menu: containing Menu Items (dishName , price), RestaurantName, MenuDate
curl POST http://localhost:8080/lunch/{%username%}/createDailyMenu
{%userName%} - path parameter accepts userName of role Admin
{
"restaurantName" : "Restaurant2",
"menuDate": "2015-11-21",
"menuItems" : [
{ "name" : "pasta" , "price" : "97.0" },
{ "name" : "pizza" , "price" : "75.0" }
]
}
---------------------------------------------------
{
"restaurantName" : "Restaurant1",
"menuDate": "2015-11-21",
"menuItems" : [
{ "name" : "salad" , "price" : "80.0" },
{ "name" : "schnitzel" , "price" : "100.0" }
]
}
2. To Create Client/Guest lunch order containing : RestaurantName , userName, menuItemName, orderDate
MenuItems should exist in Daily menu of Restaurant on requested date.
curl POST http://localhost:8080/lunch/createClientDailyOrder
{
"restaurantName" : "Restaurant1",
"userName": "Vasya",
"menuItemName" : "pasta",
"orderDate" : "2015-11-21"
}
3. To update/modify Client/Guest lunch order on specific date - send JSON containing same parameters as for creation daily order
RestaurantName, userName, selected menuItem, requested menuDate.
curl POST http://localhost:8080/lunch/updateClientDailyOrder
{
"restaurantName" : "Restaurant1",
"userName": "Vasya",
"menuItemName" : "schnitzel",
"orderDate" : "2015-11-21"
}

