package com.example.db;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.Activity;
import com.example.Footstep;
import com.example.Plan;
import com.example.Sport;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.type.Date;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.support.constraint.Constraints.TAG;

public class DBController {

    public FirebaseFirestore db = null;
    public static boolean activityComplete = false;
    public static boolean SportComplete = false;
    public static boolean PlanComplete = false;
    private List<Sport> _sports = new ArrayList<>();
    public DBController(){
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(false)
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        firestore.setFirestoreSettings(settings);
        db = firestore;
    }
    public void test(){
        db.collection("Users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }
    public void add(){
        Map<String, Object> user = new HashMap<>();
        user.put("first", "Ada");
        user.put("last", "Lovelace");
        user.put("born", 1815);

// Add a new document with a generated ID
        db.collection("Users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }
    public void addNewUser(String email, String password){
        Map<String, Object> newUser = new HashMap<>();
        newUser.put("email", email);
        newUser.put("password", password);


        db.collection("Users").document(email)
                .set(newUser)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }
    public void addSports(String email, Sport _sport){
        db.collection("Users").document(email).collection("Sports").document(_sport.name).set(_sport);
    }

    public List<Sport> getAllSports(String email){
        //volatile boolean flag = false;
        SportComplete = false;
        db.collection("Users").document(email).collection("Sports")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Sport> sports  = task.getResult().toObjects(Sport.class);
                            if(sports != null  && sports.size() != 0  ) _sports = sports;
                            else _sports.clear();
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                        SportComplete = true;
                    }
                });
        try {
            // Simulate network access.


            Thread.sleep(1000);
        } catch (InterruptedException e) {
            return null;
        }
        while(!SportComplete){}
        return _sports;

    }
    public void addActicity(String email, Activity ac){
        // Add a new document with a generated id.
        Map<String, Object> data = new HashMap<>();
        data.put("name", ac.name);
        data.put("start", ac.start);
        data.put("end",ac.end);
        db.collection("Users").document(email).collection("Activities")
                .add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }
    public void addFootStepActivity(String email, Footstep ft){
        // Add a new document with a generated id.
        Map<String, Object> data = new HashMap<>();
        data.put("name", ft.name);
        data.put("date", ft.date);
        data.put("value",ft.value);
        db.collection("Users").document(email).collection("Activities")
                .add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }
    public List<Object> getAllActivity(String email){
        activityComplete = false;
        final List<Object> allActivities = new ArrayList<>();
        db.collection("Users").document(email).collection("Activities")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                allActivities.add(document.getData());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                        activityComplete = true;
                    }
                });
//        try {
//            // Simulate network access.
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            return null;
//        }
        while(!activityComplete){}
        return allActivities;
    }
    public void addPlan(String email, Plan plan){
        Map<String,Object> plans = new HashMap<>();
        plans.put("date", plan.date);
        plans.put("name",plan.name);
        db.collection("Users").document(email).collection("Plans").document(plan.name).set(plan);
        db.collection("Users").document(email).collection("Plans").document(plan.name).update(plans);
        for (int i = 0; i < plan.activity.size(); i++){
            Activity ac = (Activity)plan.activity.get(i);
            if(!ac.name.equals("footsteps")) {
                Map<String, Object> data = new HashMap<>();
                data.put("start", ac.start);
                data.put("end", ac.end);
                data.put("name", ac.name);

                db.collection("Users").document(email).collection("Plans").document(plan.name).collection("Activities").add(data);
            }
            else{
                Footstep ft = (Footstep) plan.activity.get(i);
                Map<String, Object> data = new HashMap<>();
                data.put("date", ft.date);
                data.put("value", ft.value);
                data.put("name", ft.name);

                db.collection("Users").document(email).collection("Plans").document(plan.name).collection("Activities").add(data);

            }
        }


    }
    public Plan getPlan(String email, String planName){
        PlanComplete = false;
        final Plan a = new Plan();
        DocumentReference docRef = db.collection("Users").document(email).collection("Plans").document(planName);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        a.name = (String)document.getData().get("name");
                        a.date = (Date)document.getData().get("date");
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
                PlanComplete = true;
            }
        });
       a.activity = getAllActivity(email);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            return null;
        }
        while(!PlanComplete){}
        return a;


    }
    public void setPersonalInfo(String email, int height, int weight, int age){
        DocumentReference userRef = db.collection("Users").document(email);


        userRef
                .update("age", age)

                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });
        userRef
                .update("height", height)

                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });
        userRef
                .update("weight", weight)

                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });

    }

}
