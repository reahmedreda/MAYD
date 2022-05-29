### Intro
This project is focusing on Test Automation Framework designing, where this implementation provides many features like:. 
- Modularity.
- Scalability.
- Independency.

It consists of these modules:
| Module      | Description                        |
| ----------- | ---------------------------------- |
| Actions     | Contains generic UI and browser actions implemented with selenium and api actions implemented with rest assured |
| DTOs        | A representation of Data Object for easing the transferring |
| utils       | A customized encapsulation of utilities imported from some libraries |
| wrappers    | Files wrappers for read/write purposes |
| POM         | Classes that represents web pages using Page object model design pattern |
| Test        | Contains 3 examples : a functional testing example [login] and e2e smoke tests [purchace] and [add/remove items] , both are running by data driven |
| config files| Files to configure the project like : pom.xml for maven , test.xml for listing the tests and suites, config.properties for data configurations, dockerfile for running in docker |


### What is missing ? 
The tests cases have hit some features for smoke purposes only. But there are more should have been covered but due to time limitation couldn't do them:
1- Sorting with all its types 
2- All Burger menu features except for log out 
3- Negative scenarios of checkout 
4- Complete data validations in each step like [item price,desc,image,total price,user info in checkout page .. etc]
5- Docker execution
6- A proper logging 
7- A proper reporting
5- many other features that weren't covered due to time limitations 

### Prerequisite 
- Install Maven : brew install maven [On Mac] 
- Install Allure : brew install allure [On Mac]
- Install docker : brew install docker [On Mac]

### Execution commands Locally
- To Run login suite : mvn clean test -DfileXml=tests.xml -DselectedTests=login
- To run Shopping E2E suite : mvn clean test -DfileXml=tests.xml -DselectedTests=shoppingE2E

### Execution Command on Docker : 
- docker network create grid
- docker run -d -p 4442-4444:4442-4444 --net grid --name selenium-hub selenium/hub:4.1.4-20220427

- Repeat next step as many as thread count mentioned in test.xml from chrome tests or more 
- docker run -d --net grid -e SE_EVENT_BUS_HOST=selenium-hub --shm-size="2g" -e SE_EVENT_BUS_PUBLISH_PORT=4442 -e SE_EVENT_BUS_SUBSCRIBE_PORT=4443 selenium/node-chrome:4.1.4-20220427

- Repeat next step as many as thread count mentioned in test.xml for firefox test or more
- docker run -d --net grid -e SE_EVENT_BUS_HOST=selenium-hub --shm-size="2g" -e SE_EVENT_BUS_PUBLISH_PORT=4442 -e SE_EVENT_BUS_SUBSCRIBE_PORT=4443 selenium/node-firefox:4.1.4-20220427

- To Run login suite : mvn clean test -DfileXml=tests.xml -DselectedTests=login -DuseDocker=true
- To run Shopping E2E suite : mvn clean test -DfileXml=tests.xml -DselectedTests=shoppingE2E -DuseDocker=true
- you can review the grid, nodes and sessions status from this link : http://localhost:4444/ui#

### Generate test report
By default, you will find test reports xmls and htmls in path target/surefire-reports
But if you want a well-organised report :
- For preview report :  allure serve target/surefire-reports
- To generate this report file for later use :  allure generate target/surefire-reports and you will find it in allure report folder

