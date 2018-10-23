# USCFit - CSCI310 Group Project

- **Developers**: 
 1. Haozhe Chen (haozhech@usc.edu)
 2. Muyao Xu (muyaoxu@usc.edu)
 3. Shunkai Zhang (shunkaiz@usc.edu)
 4. Siyuan Xu (siyuanx@usc.edu)
 5. Zhixu Li (zhixul@usc.edu)

- **Setup Instructions**
1. Follow the regular setup steps for Android Projects.  
2. The emilator version for testing purposes would be: **Nexus 5X API 28 (Android 9, API28)**. 

- **Detailed Steps**:
1. Login Page 
When the user types in the email and password, there would be two cases: 

    a. If the user has registered his/her email before, and the password is correct, then the user will be redirected to the user homepage. If the password is incorrect, the app will stay on the original page, and the user will be notified of the wrong password. 

    b. If theh user hasn't registered his/her email before, then the app will send his/her email and password to backend and allows for user login. 

2. Home Page 
This is the place where user gain access to all core functionalities of this application, which include **Footstep**, **Add Activity**, **Add Sport**, **Add Plan**, **Progress**, and **User Activity and Plan Profile**. 

The following points explain the different core functionalities location in **Home Page**: 

3. Footstep Button 
On the top right corner, user can click on the Footstep button to access the daily footstep taken. 

4. Add Activity Page 
This is where the user can add **finished** activities for calorie calculation. The user can add activities that is already finished, with starting and ending time so that our system can calculate the corresponding calorie. 

5. Add Plan Page 
This is where the user can add **future** daily plan and activities. A plan contains multiple activities for **one single day**, and user may add activities with starting and ending time of the plan date. 

6. Progress Page 
This is where the user can view the workout plan progress. If the user user finsihes one daily plan, 1 badge will be awarded. If the user has finished 7 consecutive daily plans, he/she will receive a large badge. 

7. Profile Page 
This is where the user can view user's sports and their corresponding calories per hour. It will also include user's activities and all their corresponding calories consumed. 