

# About Project

To incorporate REST API endpoints with various functionalities required for a website (similar to Quora).

---

## Course:

Course 5 Group Project

---

## Project Name: 

Social Q&A web-app

---

## Git Repository

https://github.com/cs5grproj/cs1.git

---

## Team Members

- Prabhavathi Dodda | doddaprabha@gmail.com
- Sindhuja Sabbani | sindhuja.sabbani@gmail.com
- Sowmya Jhetty | jsowmya@outlook.com
- Anil Kumar Narayanan | anilnarayanan@hotmail.com

---

## REST API Endpoints

### Endpoint 1

- **User Controller**                         
    ![EP1](Trello_quora/images/ep1.png)
                                  
    ![EP1](Trello_quora/images/ep2.png)

    ![EP1](Trello_quora/images/ep3.png)

### Endpoint 2

- **QuestionController**
    ![EP2](Trello_quora/images/ep5.png)

	
### Endpoint 3

- **AnswerController**
    ![EP3](Trello_quora/images/ep6.png)

---

### Project import instructions (Intellij IDEA)

1. Import Project from Version Control
    ![Project Import](Trello_quora/images/1.png)

2. Provide GIT repository path to clone the project to the local target directory
    ![Project Import](Trello_quora/images/2.png)

3. Import Maven dependencies
    ![Project Import](Trello_quora/images/3.png)

    ![Project Import](Trello_quora/images/4.png)

---

### Project build instructions

1. Open terminal available in the Intellij IDE

2. Change to the project directory i.e Trello_quora

3. Execute the below Maven commands on the terminal:

    `mvn clean install -PskipTests`
    
    `mvn clean install -Psetup`

---

### Swagger UI

```http://localhost:8080/api/swagger-ui.html```

---

### Manual Testing Results on Swagger UI

- #### Endpoint 1
    ![TC1](Trello_quora/images/tc1.png)
    
    ![TC2](Trello_quora/images/tc2.png)

    ![TC3](Trello_quora/images/tc3.png)
    
- #### Endpoint 2

     - createQuestion - "/question/create"
    
         ![TC4](Trello_quora/images/cq.png)
        
         ![TC5](Trello_quora/images/cq_notsignin.png)
        
         ![TC6](Trello_quora/images/cq_athr002.png)
         
     - getAllQuestions - "/question/all"
     
         ![TC7](Trello_quora/images/qa_athr001.png)
        
         ![TC8](Trello_quora/images/question_all.png)
        
         ![TC9](Trello_quora/images/questionall_athr002.png)
         
     - editQuestionContent - "/question/edit/{questionId}"
      
         ![TC10](Trello_quora/images/qeq_athr001.png)
         
         ![TC11](Trello_quora/images/qeq_athr002.png)
         
         ![TC12](Trello_quora/images/qeq_questionedited.png)
         
         ![TC13](Trello_quora/images/qeq_ques001.png)
         
         ![TC14](Trello_quora/images/qeq_athr003.png)
         
     - deleteQuestion - "/question/delete/{questionId}"
       
         ![TC15](Trello_quora/images/qdq_athr001.png)
         
         ![TC16](Trello_quora/images/qdq_athr002.png)
          
         ![TC17](Trello_quora/images/qdq_questiondeleted.png)
          
         ![TC18](Trello_quora/images/qdq_ques001.png)
         
         ![TC19](Trello_quora/images/qdq_athr003.png)
              
     - getAllQuestionsByUser - "question/all/{userId}"

         ![TC20](Trello_quora/images/qau_athr001.png)
          
         ![TC21](Trello_quora/images/qau_all_userid.png)
         
         ![TC22](Trello_quora/images/qau_usr001.png)
         
         ![TC23](Trello_quora/images/qau_athr002.png)

         
- #### Endpoint 3

    - createAnswer - "/question/{questionId}/answer/create"
    
        ![TC24](Trello_quora/images/qqac_answercreated.png)
    
        ![TC25](Trello_quora/images/qqac_athr002.png)
        
        ![TC26](Trello_quora/images/qqac_ques001.png)
    
    - editAnswerContent - "/answer/edit/{answerId}"
    
        ![TC27](Trello_quora/images/aea_athr001.png)
        
        ![TC28](Trello_quora/images/aea_athr002.png)
    
    - deleteAnswer - "/answer/delete/{answerId}"
    
        ![TC29](Trello_quora/images/ada_athr001.png)
        
        ![TC30](Trello_quora/images/ada_athr002.png)
       
        ![TC35](Trello_quora/images/ada_athr003.png)
    
    - getAllAnswersToQuestion - "answer/all/{questionId}"
    
        ![TC31](Trello_quora/images/aaq_200.png)
    
        ![TC32](Trello_quora/images/aaq_athr001.png)
        
        ![TC33](Trello_quora/images/aaq_athr002.png)
        
        ![TC34](Trello_quora/images/aaq_ques001.png)
   
   