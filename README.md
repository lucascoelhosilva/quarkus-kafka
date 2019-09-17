# E-commerce

> Project created for studies

### Requirements:
 - Docker 
 - Docker Compose
 - Maven 3.6

#
#### Getting Started
   Microservices running on docker
   
* If you want to use the Gmail to sending emails, first create a dedicated in https://myaccount.google.com/apppasswords

When done, you can configure the api-notification in file `docker-compose.yml` with your `password-generate` 
   
#### Overview



##### Building Projects 
 - api-notification
 - api-users
 - api-sales

> mvn clean package

####Running
 - api-notification
 - api-sales
 - api-users

> docker-compose build

> docker-compose up


#### How to use


* http://localhost:8090/users
       
        {
        	"name": "Lucas Coelho",
        	"email": "lucascoelhosilvacs@gmail.com"
        }

* http://localhost:8090/products

        {
        	"name": "product1",
        	"price": "10.00"
        }
        
* http://localhost:8090/sales

        {
            "userId": "userId created",
            "products": [
                {
                    "name": "product1",
                    "price": "10.00"
                },
                {
                    "name": "product1",
                    "price": "10.00"
                }
            ]
        }