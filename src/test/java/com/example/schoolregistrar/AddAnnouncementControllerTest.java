package com.example.schoolregistrar;

import org.junit.jupiter.api.Test;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.example.schoolregistrar.AddAnnouncementController.*;
import static com.example.schoolregistrar.SchoolRegistrarApplication.fstore;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import static org.junit.jupiter.api.Assertions.*;
class AddAnnouncementControllerTest {

    static final FirestoreContext contxtFirebase = new FirestoreContext();
    static boolean found=false;
    private String CRN;
    List<String> Courses;
    @BeforeAll
    static void setUp() {
        fstore = contxtFirebase.firebase();
    }

    @Test
    void addAnnouncement() throws ExecutionException, InterruptedException {

        CollectionReference base1 = fstore.collection("courses");
        ApiFuture<QuerySnapshot> future1 = base1.get();
        List<QueryDocumentSnapshot> documents = future1.get().getDocuments();

        if (documents.size() == 0) {
            fail("No data exists");
        }

        for (QueryDocumentSnapshot document : documents) {

            CollectionReference base2 = fstore.collection("courses").document(document.getId()).collection("sections");
            ApiFuture<QuerySnapshot> future2 = base2.get();
            List<QueryDocumentSnapshot> sections = future2.get().getDocuments();

            if (!sections.isEmpty()){
                for (QueryDocumentSnapshot section : sections) {

                    CRN = section.get("CRN").toString();

                    if (!CRN.equals("Empty")) {
                        CollectionReference base3 = fstore.collection("courses").document(document.getId()).collection("sections").document(CRN).collection("announcements");
                        ApiFuture<QuerySnapshot> future3 = base3.get();
                        List<QueryDocumentSnapshot> announcements = future3.get().getDocuments();

                        for (QueryDocumentSnapshot announcement: announcements) {
                            if (announcement.get("Name").equals(courseString + " " + name)) {
                                assertEquals(announcement.get("Name"), courseString + " " + name);
                                assertEquals(announcement.get("Description"), description);
                                found=true;
                                return;
                            }
                        }

                    }

                }

            }

        }

        if (found) {
            fail("Error inserting data");
        }
    }
}

