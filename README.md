# Doctor availability checker

## How to use
### Logging into Luxmed
To login into Luxmed, you can send a request:  
**POST /login?username=XXXXXX&password=XXXXXX**

and you will get back an LXToken that could be used for further requests.

## Next steps
1. setup database with help of JPA + Hibernate (use sessionFactory, create entities)
2. cache Luxmed credentials into database and read them for security 
(create initial database with hashed password and ship it with the project, and we'll also need to be able to unhash it to)
3. Whatever.  
4. implement search of available doctor sessions.