package com.example.dualingo;

import android.util.Log;
import com.google.firebase.firestore.FirebaseFirestore;
import com.example.dualingo.Models.*;
import java.util.List;

public class FirestoreHelper {

    private FirebaseFirestore db;

    public FirestoreHelper() {
        db = FirebaseFirestore.getInstance();
    }

    public void addVocabulary(Vocabulary vocabulary) {
        db.collection("Vocabulary").document(vocabulary.getIdVocabulary())
                .set(vocabulary)
                .addOnSuccessListener(aVoid -> Log.d("FirestoreHelper", "Added Vocabulary"))
                .addOnFailureListener(e -> Log.w("FirestoreHelper", "Error adding Vocabulary", e));
    }

    public void addGrammar(Grammar grammar) {
        db.collection("Grammar").document(grammar.getIdGrammar())
                .set(grammar)
                .addOnSuccessListener(aVoid -> Log.d("FirestoreHelper", "Added Grammar"))
                .addOnFailureListener(e -> Log.w("FirestoreHelper", "Error adding Grammar", e));
    }

    public void addVocabularyLesson(VocabularyLesson vocabularyLesson) {
        db.collection("VocabularyLessons").document(vocabularyLesson.getIdVocabularyLesson())
                .set(vocabularyLesson)
                .addOnSuccessListener(aVoid -> Log.d("FirestoreHelper", "Added Vocabulary Lesson"))
                .addOnFailureListener(e -> Log.w("FirestoreHelper", "Error adding Vocabulary Lesson", e));
    }

    public void addLecture(Lecture lecture) {
        db.collection("Lectures").document(lecture.getIdLecture())
                .set(lecture)
                .addOnSuccessListener(aVoid -> Log.d("FirestoreHelper", "Added Lecture"))
                .addOnFailureListener(e -> Log.w("FirestoreHelper", "Error adding Lecture", e));
    }

    public void addSession(Session session) {
        db.collection("Sessions").document(session.getIdSession())
                .set(session)
                .addOnSuccessListener(aVoid -> Log.d("FirestoreHelper", "Added Session"))
                .addOnFailureListener(e -> Log.w("FirestoreHelper", "Error adding Session", e));
    }

    public void addIntroduction(Introduction introduction) {
        db.collection("Introductions").document(introduction.getIdIntroduction())
                .set(introduction)
                .addOnSuccessListener(aVoid -> Log.d("FirestoreHelper", "Added Introduction"))
                .addOnFailureListener(e -> Log.w("FirestoreHelper", "Error adding Introduction", e));
    }
}
