# Steps to reproduce the issue

### Frontend (Angular)

1. clone the repo
2. move to the client folder
3. create `environments` directory and sub files as the `sample.environments`
4. move to `src/app/app.component.ts` file and replace `this.cookieService.createOrganizationID('test_21625_id');` with
   actual id from database.
5. run `npm install --force`
6. run `npm run start`

### Backend (Spring Boot)

1. move back to main folder
2. create `.env` file as the `sample.env`
3. run `mvn clean install -DskipTests`

### Database (MongoDB)

1. create a new database in compass or mongo atlas cluster
2. if you use compass, move to the `resources/application.properties` file and uncomment
   the `spring.data.mongodb.host=localhost` and comment the `spring.data.mongodb.uri=${DATABASE_URI:World}`.
3. if you use mongo atlas cluster, move to the `resources/application.properties` file and uncomment
   the `spring.data.mongodb.uri=${DATABASE_URI:World}` and comment the `spring.data.mongodb.host=localhost`.
