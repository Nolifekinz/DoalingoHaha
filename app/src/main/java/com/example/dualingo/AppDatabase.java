package com.example.dualingo;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import android.content.Context;

import com.example.dualingo.Converter.FormulaConverter;
import com.example.dualingo.Converter.ListStringConverter;
import com.example.dualingo.DAO.ArrangingDAO;
import com.example.dualingo.DAO.CompletedLessonDAO;
import com.example.dualingo.DAO.FillBlankDAO;
import com.example.dualingo.DAO.GrammarDAO;
import com.example.dualingo.DAO.IntroductionDAO;
import com.example.dualingo.DAO.LectureDAO;
import com.example.dualingo.DAO.ListeningDAO;
import com.example.dualingo.DAO.SessionDAO;
import com.example.dualingo.DAO.SpeakingDAO;
import com.example.dualingo.DAO.UserDAO;
import com.example.dualingo.DAO.UserExerciseProgressDAO;
import com.example.dualingo.DAO.VocabularyDAO;
import com.example.dualingo.DAO.VocabularyLessonDAO;

import com.example.dualingo.DAO.WrongQuestionDAO;
import com.example.dualingo.Models.Arranging;
import com.example.dualingo.Models.CompletedLesson;
import com.example.dualingo.Models.FillBlank;
import com.example.dualingo.Models.Grammar;
import com.example.dualingo.Models.Introduction;
import com.example.dualingo.Models.Lecture;
import com.example.dualingo.Models.Listening;
import com.example.dualingo.Models.Session;
import com.example.dualingo.Models.Speaking;
import com.example.dualingo.Models.User;
import com.example.dualingo.Models.UserExerciseProgress;
import com.example.dualingo.Models.Vocabulary;
import com.example.dualingo.Models.VocabularyLesson;
import com.example.dualingo.Models.WrongQuestion;

@Database(
        entities = {Arranging.class, FillBlank.class, Grammar.class, Introduction.class,
                Lecture.class, Listening.class, Session.class, Speaking.class,
                Vocabulary.class, VocabularyLesson.class, User.class, WrongQuestion.class, UserExerciseProgress.class, CompletedLesson.class},
        version = 2
)

@TypeConverters({ListStringConverter.class, FormulaConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDAO userDAO();
    public abstract ArrangingDAO arrangingDAO();
    public abstract FillBlankDAO fillBlankDAO();
    public abstract GrammarDAO grammarDAO();
    public abstract IntroductionDAO introductionDAO();
    public abstract LectureDAO lectureDAO();
    public abstract ListeningDAO listeningDAO();
    public abstract SessionDAO sessionDAO();
    public abstract SpeakingDAO speakingDAO();
    public abstract VocabularyDAO vocabularyDAO();
    public abstract VocabularyLessonDAO vocabularyLessonDAO();
    public abstract WrongQuestionDAO wrongQuestionDAO();
    public abstract UserExerciseProgressDAO userExerciseProgressDAO();
    public abstract CompletedLessonDAO completedLessonDAO();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "dualingo_db")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
