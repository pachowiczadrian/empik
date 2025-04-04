Sample requests:

###
POST http://localhost:8080/api/complaints
Content-Type: application/json

{
    "productId": 1,
    "customerId": 11,
    "content": "CONTENT"
}

###
GET http://localhost:8080/api/complaints/1
Content-Type: application/json

###
PATCH http://localhost:8080/api/complaints/1
Content-Type: application/json

{
    "content": "CONTENT_TO_UPDATE"
}