package com.example.subjecthub.testutils;

public class UrlUtils {

    public static final String API_URL = "/api";

    public static String buildUniApiUrl(Long uniId) {
        return API_URL + "/universities/university/"  + uniId;
    }

    public static String buildFacultiesApiUrl(Long uniId) {
        // /api/universities/university/{uniId}/faculties
        return buildUniApiUrl(uniId) + "/faculties";
    }

    public static String buildFacultyApiUrl(Long uniId, Long facultyId) {
        // /api/universities/university/{uniId}/faculties/faculty/{facultyId}
        return buildFacultiesApiUrl(uniId) + "/faculty/" + facultyId ;
    }

    public static String buildSubjectsApiUrl(Long uniId) {
        // /api/universities/university/{uniId}/subjects
        return buildUniApiUrl(uniId) + "/subjects";
    }

    public static String buildSubjectApiUrl(Long uniId, Long subjectId) {
        // /api/universities/university/{uniId}/subjects/subject/{subjectId}
        return buildSubjectsApiUrl(uniId) + "/subject/" + subjectId;
    }

    public static String buildCommentsApiUrl(Long uniId, Long subjectId) {
        // /api/universities/university/{uniId}/subjects/subject/{subjectId}/comments
        return buildSubjectApiUrl(uniId, subjectId) + "/comments";
    }

    public static String buildCommentApiUrl(Long uniId, Long subjectId, Long commentId) {
        // /api/universities/university/{uniId}/subjects/subject/{subjectId}/comments/comment/{commentId}
        return buildCommentsApiUrl(uniId, subjectId) + "/comment/" + commentId;
    }

    public static String buildAssessmentsApiUrl(Long uniId, Long subjectId) {
        // /api/universities/university/{uniId}/subjects/subject/{subjectId}/assessments
        return buildSubjectApiUrl(uniId, subjectId) + "/assessments";
    }

    public static String buildAssessmentApiUrl(Long uniId, Long subjectId, Long assessmentId) {
        // /api/universities/university/{uniId}/subjects/subject/{subjectId}/assessments/assessment/{assessmentId}
        return buildAssessmentsApiUrl(uniId, subjectId) + "/assessment/" + assessmentId;
    }
}
