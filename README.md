# Bestseller challenge

## Assumptions and Decisions
* _DiscountedAmount_: This is the amount of the order after we have applied Discounts. In other words: DiscountedAmount = OriginalAmount - Discounts
* _CartCache Map in OrderService_: This acts like a Cache for the Carts that are active at the moment. In a real-world environment this could be a Redis or something like that.
* _UserCache Map in UserService_: This acts like a Cache for the Users that are active (have a Cart and they can add items to it) at the moment. In a real-world environment this could be a Redis or something like that.
* _Why a User cache and not a Drink/Topping cache_: When I retrieve a user from DB I can be sure that, until the Order is placed, I will need it. I can't say the same about Drink and Toppings. Not only I have no idea if they're going to be used again, but I also need to be aware of changes in the DB, like them being deleted/modified.
* _Drinks/Toppings modified/deleted but in a cart_: I assume that when you add a Drink/Topping to a cart with a price, it exists and costs that. If before placing the order the Drink/Topping is deleted or modified, it doesn't matter for my current carts. It can be thought as if I delete a Drink is because I ran out of stock or something like that, but when you added it to the Cart we had it and reserved one for you.
* _Logging_: I decided to Log as much as possible with as much information as possible in order to be able to follow the process by reading them. However, by changing _logging.level.com.bestseller.starbux.*_ property in the _application.properties_ file we can avoid logging so much if instead of logging INFO logs we log DEBUG or more.

## About tests

### Tests that write/delete from DB

Some tests actually writes or delete elements in the DB. 
In this case, because we're using a volatile DB, it doesn't matter.
However, in a different environment with a more persistent DB, we would need to take care of this.
Some options would be:
* Having different DBs for Development and Production, and of course, 
    using this against Development DB.
* Having some sort of mechanism to rollback our changes as soon as we tested them right.

## Dependencies added
* _H2_: Provides us with a small embebbed DB.

* _MapStruct_: This allow us to map objects in a quick, clean and easy way.

### Running docker image
1. Clone the repo: https://github.com/aguerredev/bs-challenge
2. Make sure you have Docker running
3. Go to the repository folder and execute this command: _mvn spring-boot:build-image_
    2. This will create the docker image **AND** run the tests.
4. Run this command: _docker run -p 8080:8080 docker.io/library/starbux:1.0_
    With this you will be able to perform API calls using Postman or your preferred software.

## Candidate Info

Sebastian Aguerre - Uruguay
