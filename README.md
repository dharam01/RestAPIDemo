# RestAPIDemo
This is a gradle project built on spring boot.

## Setup
- Clone the repository
- Import the project into IDE
- It is using Java 12

## How to run unit tests
- Open the command prompt go to root directory of the project and run below gradle task.
```
Tasks : gradlew clean test
```
- The above task will generate the test report.
- To verify the unit tests report open below html file.
```
build/reports/tests/test/index.html
```

## How to run the application
- Go to root directory of the project and run below gradle task
```
Task: gradlew clean bootRun
```
- It will start the application at port number 8080
- If you want to run the application in different port then run below command
```
Task: gradlew clean bootRun --args='--server.port=<port_number>'
For example running at port 8081
Task: gradlew clean bootRun --args='--server.port=8081'
```

## Rest API details
There is one GET Rest API is hosted in this application URL is given below
```
http://localhost:8080/search/api/rest/v2/catalog/products/reductions?labelType=ShowWasNow
```
There query parameter labelType is optional to change the format of priceLabel.
```
labelType=ShowWasNow => in this case priceLabel will be “Was £x.xx, now £y.yyy”
labelType=ShowWasThenNow => in this case priceLabel will be “Was £x.xx, then £y.yy, now £z.zzz”
labelType=ShowPercDscount => in this case priceLabel will be “x% off - now £y.yy”.
```