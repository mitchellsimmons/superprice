# SuperPrice

A fully responsive (mock) website developed for SuperPrice, an online price comparison and delivery service for purchasing groceries from local supermarkets.
Developed as part of my Software Engineering Processes and Tools course at RMIT University by a six member team.
A Scrum framework, consisting of two major sprints (outlined below) was used to guide development.

The application was deployed to AWS Elastic Beanstalk via dockerisation but is not currently live.
Local build instructions can be found below.

## Members

-   Mitchell Simmons
-   Marty Lawrence
-   Ruschia Mackay
-   Benjamin Grayland
-   Geordie Elliot-Kerr
-   Maximus Dionyssopouloss

## Sprints

### Sprint 2 - 08/10/23

User Stories completed during Sprint 2:

-   User can place order
-   User able to view cart
-   User able to set postcode
-   Migrate to deployment DB (MySQL/PostgreSQL)
-   Code to be containerised
-   User can select a delivery time slot
-   User able to enter delivery instructions
-   User can add products to cart
-   App to send notifications
-   Products have a stock level
-   App to run in browser
-   User can get a search result in <2 seconds
-   App will load a page in <2 seconds
-   A response to an action will occur in <2 seconds
-   App to perform actions without suffering performance issues
-   App to be available 99% of the time
-   Website demonstrates accessible colour scheme
-   App's UI to be easy to use
-   App to have robust error handling for inputs and actions

### Sprint 1 - 17/09/23

User Stories completed during Sprint 1:

-   User can view main/basic site
-   Product data is stored in database
-   Website can connect to product database
-   User can view details of a product
-   User can view complete list of products
-   Products to be available in different sizes/volumes/quantities
-   User can browse product categories hierarchically
-   User can browse products by category
-   User able to compare prices between different supermarkets
-   User can search for product

# Using SuperPrice

If you would like to build and run the application locally, please follow the instructions below:

## Setup your environment

You will need to have in your system

-   Java 17.0 or higher
-   Node and npm
-   Apache Maven
-   IDE or Editor

## Building and running the application locally

-   cd into backend/superprice
-   Start the backend server with "./mvnw spring-boot:run"
    -   If encountering FlyWay migration errors, you may need to locally delete the H2 DB and re-run
-   cd into frontend/
-   Build the app with "npm run build"
-   Start the app with "npm run start"
-   go to [localhost:3000](localhost:3000) for the landing page.

## Building and running the application to/from Docker images

-   Setup a repository on the AWS Elastic Container Registry.
-   Update the ECR_DOMAIN variable on the compose.sh file to your repository.
-   Ensure your AWS SSH and CLI credentials are up to date.
-   Run `aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin $ECR_DOMAIN`

### Building the images

-   Run `./compose.sh` in the project root directory, this will run the build process and push the images to the repository.

### Composing and running the application using images

-   Create a `compose-local-vars.env` file in the root directory with the below configuration.
    -   DB_PASSWORD=ANY_PASSWORD_YOU_CHOOSE
    -   CLIENT_API_URL=http://localhost:8080
    -   API_URL=http://localhost:8080
    -   spring.h2.console.enabled=false
-   Run `docker compose -f compose.yml --env-file compose-local-vars.env up`
-   Visit http://localhost:3000
