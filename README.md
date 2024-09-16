# Spring Boot ToDo Application

## Overview

This project is a Spring Boot application designed for evaluation purposes. It uses drill, a powerful API test program, to test and validate the application’s functionality. The application showcases a RESTful API that can be used to manage tasks, making it an useful choice for people looking for one ToDo tool to better manager their tasks.

## Features

    RESTful API for managing tasks
    Simple user API for easy interaction
    CRUD operations for tasks

## Technologies Used

    Spring Boot: For building the application.
    Drill: For API testing.
    Maven: For dependency management.
    Nix: For developer environment and build.

## Getting Started

### Prerequisites

Before you begin, ensure you have the following installed:

    Java Development Kit (JDK) 17 or later
    Apache Maven
    Drill (for API testing)

For development, it is also recommended the following tools for a better developer experience:

    nix-shell (downloads and imports all the necessary tooling)
    pre-commit (ensures that code commited passes tests, build and styling)
    astyle (style the code)
    direnv (automatic use nix-shell and exports envvars)

### Running the Application

Clone the repository:

    git clone https://github.com/jyeno/todo-engsoft2.git

Build and run the project using Maven:

    mvn clean spring-boot:run

The application will start on http://localhost:9000.

### Testing with Drill

Drill can be used to test and validate the project API:

    drill --benchmark todo_requests.yml

## API Endpoints

    Method	Endpoint	               Description
    GET      /tasks                     Retrieve all tasks
    GET      /tasks/{id}                Retrieve a specific task
    GET      /tasks/status/{status}     Retrieve all tasks with given status (PENDING,DOING,DONE)
    POST     /tasks                     Create a new task
    PUT      /tasks/{id}                Update an existing task
    DELETE   /tasks/{id}                Delete an task
