
# Rocket Control API

API create and control rockets and planets

## Technologies

 - Spring
 - Java 11
 - MySQL 
 - Docker

## Getting Started

### Requirements

 - Docker
 - Java 11
 - MySQL

### Running

 - On your terminal, go to /infrastructure/docker and run "docker-compose up".
 - Go to src/main/java/com/elo7/junior/dev/challenge/ and run "Main.java"

## APIs
### Rocket
- **POST** /v1/rocket
  - Creates a new rocket
- **GET** /v1/rocket
  - Returns a list with all rocket. An empty list is returned if no rockets are found
  - Can receive query parameters such as page size and page number
- **GET** /v1/rocket/{id}
  - Returns a json with the rocket information
  - id variable expects an integer
- **POST** /v1/rocket/{id}/move
  - Moves the rocket with said id according to the movement list
  - id variable expects an integer
  - Expects a request body such as {"movementList":"MLR"}
- **POST** /v1/rocket/{id}/sendToPlanet/{planetId}
  - Send rocket with said id to planet with said planetId
  - id variable expects an integer
  - planetId variable expects an integer
- **POST** /v1/rocket/{id}/recall
  - Recalls rocket with said id back from it's allocated planet
  - id variable expects an integer
- **DELETE** /v1/rocket/{id}
  - Deletes rocket with said id
  - id variable expects an integer
### Planet
- **POST** /v1/planet
  - Creates a new planet
  - Expects a request body such as {"name":"Planet", "size":"5x5"}
- **GET** /v1/planet
  - Returns a list with all planets. An empty list is returned if no rockets are found
  - Can receive query parameters such as page size and page number
- **GET** /v1/planet/{id}
  - Returns a json with the planet information
  - id variable expects an integer
- **GET** /v1/planet/{id}/rockets
  - Returns a list with all rockets allocated in that planet
  - id variable expects an integer
  - Can receive query parameters such as page size and page number
- **DELETE** /v1/planet/{id}
  - Deletes planet with said id
  - id variable expects an integer