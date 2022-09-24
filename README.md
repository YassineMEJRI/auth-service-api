# Simple authentication and authorization API with Java (no frameworks)
### Description
https://fantastic-catamaran-30b.notion.site/Description-f8a9089c74e341948e4c36b9d12a1a16

## API endpoints

### Create user

```bash
curl --location --request POST 'http://localhost:8080/api/user/' \
--header 'Content-Type: application/json' \
--data-raw '{
"username": "hkleun",
"password": "password"
}'
```

Possible responses

```
"User created successfully"
OR
"User already exists"
```

### Delete user
```bash
curl --location --request DELETE 'http://localhost:8080/api/user/' \
--header 'Content-Type: application/json' \
--data-raw '{
    "username": "hkleun"
}'
```
Possible responses
```
"User deleted successfully"
OR
"User not found"
```

### Create new Role
```bash
curl --location --request POST 'http://localhost:8080/api/role/' \
--header 'Content-Type: application/json' \
--data-raw '{
    "name": "admin"
}'
```
Possible responses
```
"Role created successfully"
OR
"Role already exists"
```

### Delete Role
```bash
curl --location --request DELETE 'http://localhost:8080/api/role/' \
--header 'Content-Type: application/json' \
--data-raw '{
    "name": "admin"
}'
```
Possible responses
```
"Role deleted successfully"
OR
"Role not found"
```

### Add role to existing user
```bash
curl --location --request POST 'http://localhost:8080/api/user/add_role' \
--header 'Content-Type: application/json' \
--data-raw '{
    "username": "hkleun",
    "rolename": "admin"
}'
```
Possible responses
```
"Role admin added to User hkleun successfully"
OR
"Role admin not found"
OR
"User hkleun not found"
OR
"User hkleun already has role admin"
```

### Remove role from user
```bash
curl --location --request DELETE 'http://localhost:8080/api/user/add_role' \
--header 'Content-Type: application/json' \
--data-raw '{
    "username": "hkleun",
    "rolename": "admin"
}'
```
Possible responses
```
"Role admin removed from User hkleun"
OR
"User hkleun has no role admin"
OR
"User hkleun not found"
OR
"Role admin not found"
```

### Login
```bash
curl --location --request POST 'http://localhost:8080/api/login' \
--header 'Content-Type: application/json' \
--data-raw '{
    "username": "hkleun",
    "password": "password"
}'
```
Possible responses
```
"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJoa2xldW4iLCJyb2xlcyI6W10sImV4cCI6MTY2MTYxMTA3M30.ARDsJlLdV-CqO7WMipaOD09ErhTjaxEddZx4ePnVTVU"
OR
"Incorrect credentials!"
```

### Get user roles by token
```bash
curl --location --request POST 'http://localhost:8080/api/user_roles' \
--header 'Content-Type: application/json' \
--data-raw '{
    "token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJoa2xldW4iLCJyb2xlcyI6WyJ1c2VyIiwiYWRtaW4iXSwiZXhwIjoxNjYxNjM1ODIxfQ.iHEKZID0KLBL_LI8b0YIkAfXqHzPUB5MufS1j9FbRK8"
}'
```
Possible responses
```
"Invalid token"
OR
[
    "user",
    "admin"
]
```

### Check role
```bash
curl --location --request POST 'http://localhost:8080/api/check_role' \
--header 'Content-Type: application/json' \
--data-raw '{
    "token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJoa2xldW4iLCJyb2xlcyI6WyJ1c2VyIiwiYWRtaW4iXSwiZXhwIjoxNjYxNjM1ODIxfQ.iHEKZID0KLBL_LI8b0YIkAfXqHzPUB5MufS1j9FbRK8",
    "role": "admin"
}'
```
Possible responses
```
"Authorized"
OR
"Unauthorized"
```

### Invalidate Token
```bashcurl --location --request POST 'http://localhost:8080/api/invalidate' \
--header 'Content-Type: application/json' \
--data-raw '{"token":"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJoa2xldW4iLCJyb2xlcyI6WyJ1c2VyIiwiYWRtaW4iXSwiZXhwIjoxNjYxNjM2MDU2fQ.HILTvdLiOkApnmpjr9sZyqvd0DkUMqC0Q1z69f2k-CM"}'
```
Possible responses
```
"Token was successfully invalidated"
OR
"Token was not valid"
```
