
# About Project

To incorporate REST API endpoints with various functionalities required for a website (similar to Quora).

## Course:

Course 5 Group Project

## Project Name: 

Social Q&A web-app

## Git Repository

https://github.com/cs5grproj/cs1.git

## Team Members

| Name | Email ID |
|--|--|
| Prabhavathi Dodda | doddaprabha@gmail.com |
| Sindhuja Sabbani | sindhuja.sabbani@gmail.com |
| Sowmya Jhetty | jsowmya@outlook.com |
| Anil Kumar Narayanan | anilnarayanan@hotmail.com |


## REST API Endpoints

### Endpoint 1

- **User Controller**
	```javascript
	signup - "/user/signup"
	signin - "/user/signin"
	signout - "/user/signout"
	```
- **CommonController**
	```javascript
	userProfile - "/userprofile/{userId}"
	```

- **AdminController**
	```javascript
	userDelete - "/admin/user/{userId}"
	```
### Endpoint 2

- **QuestionController**
	```javascript
	createQuestion - "/question/create"
	getAllQuestions - "/question/all"
	editQuestionContent - "/question/edit/{questionId}"
	deleteQuestion - "/question/delete/{questionId}"
	getAllQuestionsByUser - "question/all/{userId}"
	```
	
### Endpoint 3

- **AnswerController**
	```javascript
	createAnswer - "/question/{questionId}/answer/create"
	editAnswerContent - "/answer/edit/{answerId}"
	deleteAnswer - "/answer/delete/{answerId}"
	getAllAnswersToQuestion - "answer/all/{questionId}"
	```
