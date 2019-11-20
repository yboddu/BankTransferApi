# banktransferapi
Simple RESTful api to transfer funds between two different bank accounts. The application gets initialised with the following bank accounts:

```
Account Id		Balance		Currency

10000011		1000		GBP
10000022		2000		GBP
10000033		3000		GBP
10000044		4000		GBP
10000055		5000		GBP

```

Authentication, currency exchange etc are outside the scope of this Api implementation.


## Run
To start the Api:
```
./gradlew jar
java -jar build/libs/banktransferapi-1.0-SNAPSHOT.jar 4567
```

## Test
```
./gradlew test
```


## Api Model:
Transfer Money:
```
curl --request POST \
  --url http://localhost:4567/api/account/transfer \
  --header 'content-type: application/json' \
  --data '{
	"fromAccountNumber": 10000011,
  	"toAccountNumber": 10000022,
  	"amount":20.00
}'
```

Get Account details
```
curl --request GET \
  --url http://localhost:4567/api/account/transfer/10000011
```
