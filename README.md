# USCFit - CSCI310 Group Project

- **Developers**: 
 1. Haozhe Chen (haozhech@usc.edu)
 2. Muyao Xu (muyaoxu@usc.edu)
 3. Shunkai Zhang (shunkaiz@usc.edu)
 4. Siyuan Xu (siyuanx@usc.edu)
 5. Zhixu Li (zhixul@usc.edu)

- **Improvements Made Since Project 2.3**

1. The first improvement task of our team for this sprint is to add a **Footstep** text field functionality when adding a plan. Here is what we've got: 

<img width="399" alt="1" src="https://user-images.githubusercontent.com/22974252/47958367-87fec580-df87-11e8-8aa3-a190590c31ba.png">

The effect of this improvement would be reflected in our progress section, where the bar shown in the screenshot would show a progress in terms of the footsteps completed. 

<img width="291" alt="1" src="https://user-images.githubusercontent.com/22974252/47958395-3dca1400-df88-11e8-88c1-06c413d9258b.png">

2. For the second improment task, we added username, email, age, height, weight info to **Profile** page.
User is also able to update personal info using the "wrench" icon at top-right corner of the Profile page.

<img width="31" alt="1" src="https://user-images.githubusercontent.com/22974252/47958510-cd23f700-df89-11e8-9cac-e672a954ff3d.png">

User must input **all of the fields** in order to trigger an update, otherwise there will be alert box asking for complete input. 

<img width="313" alt="1" src="https://user-images.githubusercontent.com/22974252/47958520-edec4c80-df89-11e8-9ce8-15f15a398e01.png">

<img width="316" alt="1" src="https://user-images.githubusercontent.com/22974252/47958522-f9d80e80-df89-11e8-827f-18a376cf47f7.png">


- **Setup Instructions**
1. Follow the regular setup steps for Android Projects. 


2. The emulator version for testing purposes would be: **Nexus 5X API 28 (Android 9, API28)**. 

3. If you need a user that's already filled with information, here it is: 

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Username: haozhech@usc.edu

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Password: 123456

- **Detailed Steps**

1. When the user first logs in (assuming it's a new user), he/she should add a sport. 

    a. If the user has registered his/her email before, and the password is correct, then the user will be redirected to the user homepage. If the password is incorrect, the app will stay on the original page, and the user will be notified of the wrong password. 

    b. If the user hasn't registered his/her email before, then the app will send his/her email and password to backend and allows for user login. 

2. After user logs in, make sure you add a sport into your account! 

3. When the user adds an activity (in both **Add Activity** and **Add Plan**), make sure to select **OK** after picking the start/end time. Otherwise the app will not allow for submission. 

4. After user adds a plan, user can click on the progress page (expect 10 seconds for this page to load, so just be patient!) and will see the progress with badges.

5. After user adds an activity/plan, user can click on the progress button on bottom right, and you will see a list of all sports, activities and their corresponding calorie consumptions. 

6. When the user clicks on the **Footstep** button on the top right corner, you will see the number of footsteps taken today. If you click on it again, it will resume to button mode. 

- **Detailed Page Information**:
1. Login Page 

<img width="399" alt="login" src="https://user-images.githubusercontent.com/22974252/47329417-7a614b80-d629-11e8-955c-77317ac84b91.png">

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;When the user types in the email and password, there would be two cases: 

    a. If the user has registered his/her email before, and the password is correct, then the user will be redirected to the user homepage. If the password is incorrect, the app will stay on the original page, and the user will be notified of the wrong password. 

    b. If the user hasn't registered his/her email before, then the app will send his/her email and password to backend and allows for user login. 


2. Home Page 

<img width="399" alt="home_page" src="https://user-images.githubusercontent.com/22974252/47329474-b72d4280-d629-11e8-8a80-66e9a37e9c61.png">

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;This is the place where user gain access to all core functionalities of this application, which include **Footstep**, **Add Activity**, **Add Sport**, **Add Plan**, **Progress**, and **User Activity and Plan Profile**. 

The following points explain the different core functionalities location in **Home Page**: 


3. Footstep Button 

<img width="104" alt="foot_step" src="https://user-images.githubusercontent.com/22974252/47329523-e3e15a00-d629-11e8-9d54-37dec2214cdd.png">

<img width="96" alt="foot_step2" src="https://user-images.githubusercontent.com/22974252/47329554-070c0980-d62a-11e8-989d-b2c092ce699a.png">

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;On the top right corner, user can click on the Footstep button to access the daily footstep taken. 


4. Add Activity Page

<img width="81" alt="add_activity_btn" src="https://user-images.githubusercontent.com/22974252/47329581-2014ba80-d62a-11e8-9df9-69e7d5013b38.png">

<img width="399" alt="add_activity" src="https://user-images.githubusercontent.com/22974252/47329606-33278a80-d62a-11e8-976b-627854030c6c.png"> 

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;This is where the user can add **finished** activities for calorie calculation. The user can add activities that is already finished, with starting and ending time so that our system can calculate the corresponding calorie. 

5. Add Sport Page 

<img width="79" alt="add_sport" src="https://user-images.githubusercontent.com/22974252/47329660-7124ae80-d62a-11e8-9277-515adc17825c.png">

<img width="399" alt="add_sport" src="https://user-images.githubusercontent.com/22974252/47329678-83065180-d62a-11e8-8491-7c26cfe46dd1.png">

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;This is where user can add new sport and its corresponding calorie per hour into his/her account. 

6. Add Plan Page 

<img width="80" alt="add_plan_btn" src="https://user-images.githubusercontent.com/22974252/47329721-adf0a580-d62a-11e8-922c-b44dd08d32be.png">

<img width="399" alt="add_plan" src="https://user-images.githubusercontent.com/22974252/47329750-bf39b200-d62a-11e8-99ac-d8577e07a143.png">

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;This is where the user can add **future** daily plan and activities. A plan contains multiple activities for **one single day**, and user may add activities with starting and ending time of the plan date. 


7. Progress Page 

<img width="78" alt="progress_btn" src="https://user-images.githubusercontent.com/22974252/47329821-e98b6f80-d62a-11e8-9608-d8544df917e5.png">

<img width="399" alt="progress" src="https://user-images.githubusercontent.com/22974252/47329874-19d30e00-d62b-11e8-890d-f0228ee3e22b.png">

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;This is where the user can view the workout plan progress. If the user user finsihes one daily plan, 1 badge will be awarded. If the user has finished 7 consecutive daily plans, he/she will receive a large badge. 


8. Profile Page 

<img width="53" alt="profile_btn" src="https://user-images.githubusercontent.com/22974252/47329899-2d7e7480-d62b-11e8-8776-6d20243400b1.png">

<img width="399" alt="profile" src="https://user-images.githubusercontent.com/22974252/47334405-a685c800-d63b-11e8-9dd3-498a7a3b660c.png">

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;This is where the user can view user's sports and their corresponding calories per hour. It will also include user's activities and all their corresponding calories consumed. 