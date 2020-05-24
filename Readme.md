## Car Platform REST API
   	
### Tech Stack used
-  Java 8	 	
-  Spring Boot 2 	
-  JPA Specs
-  Lombok
-  H2 Database
-  Maven
-  Git
-  IntelliJ IDE

### Approach to solve this problem

#### Problems you discovered
- My choosen approach towards TDD is bit challenging in the given time and so consumed more time for test cases. 

#### Executed tests and results
- Executed 12 tests succesfully
- Passed 12 tests
#### Ideas you would like to implement if you had time - explain how you would implement them
- API security : these end points can protected with api tokens and so authentication module can be implemented using spring security and JWT etc
- Swagger Documentation : For easy access to endpoints this can be made using swagger plugin integration
- Logging Implementation : Effective logging implementation can be done using log4j
#### Decisions you had to take and why
- Choose Spring Boot Framework : as this is right option for quick start and introduce different layers like routing, controllers and services
- Picked Lombok : Used setter/getter implementations with annotations for POJOs
- Choose H2 : For most non real time projects we use H2 rather than mysql or postgres
- Apache commons IO : For csv file processing, I had used this   
#### Tested architectures and reasons why you chose them instead of other options
- I have started TDD concept(like tests written initially & failing them, later passing them after writing actual functionality)    
- Web Layer : Controller tests are done by hitting each end point developed with sample data and responses are verified.
- Service Layer : Backend layer which means repository and service both are covered here.
 

### EndPoints Exposed 
- GET http://localhost:8080/search    
- GET http://localhost:8080/search?query=mercedes
- POST http://localhost:8080/vehicle_listings/
```
{
    "dealer": 3,
    "provider": "TESLA Provider",
    "listings": [
            {
                    "code": "m",
                    "make": "tesla",
                    "model": "T5",
                    "kW": 490,
                    "year": 2019,
                    "color": "yellow",
                    "price": 40990
            }
    ]
}
```
- POST http://localhost:8080/upload_csv/1?provider=BMV Provider  (csv file to uploaded here and samples are available in tests/resources folder)
 