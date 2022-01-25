# parking-a-lot


Garage

In this problem, you have a garage that can be parked up to 10 slots (you can consider each slot is 1 unit range) at any
given point in time. You should create an automated ticketing system that allows your customers to use your garage
without human intervention. When a car enters your garage, you give a unique ticket issued to the driver. The ticket
issuing process includes us documenting the plate and the colour of the car and allocating an available slots to the car
before actually handing over a ticket to the driver. When a vehicle holds number of slots with its own width, you have to
leave 1 unit slot to next one. The customer should be allocated slot(s) which is nearest to the entry. At the exit the
customer returns the ticket which then marks slot(s) they were using as being available.
Create a spring boot project and then, publish a rest controller. Your controller methods include park, leave and status.

CREATE A PARK RESOURCE 

POST /api/parking-a-lot/park
Accept: application/json
Content-Type: application/json

{
"regNo": "34-BO-1987",
"color": "Red",
"deviceType": "TRUCK"
}

RESPONSE: Allocated slot number: 4
Location header: http://localhost:8080/api/parking-a-lot/park

CURL EXAMPLE : 

curl --location --request POST 'http://localhost:8080/api/parking-a-lot/park' \
--header 'Content-Type: application/json' \
--data-raw '{
"regNo": "34-BO-1987",
"color": "Red",
"deviceType": "TRUCK"
}'


GET PARK STATUS

GET /api/parking-a-lot/status
Accept: application/json
Content-Type: application/json

CURL EXAMPLE

curl --location --request GET 'http://localhost:8080/api/parking-a-lot/status'


To remove vehicles from parking slots

PUT /api/parking-a-lot/leave/3 HTTP/1.1
Host: localhost:8080

CURL EXAMPLE :
curl --location --request PUT 'http://localhost:8080/api/parking-a-lot/leave/3'

PS : Added postman collection.