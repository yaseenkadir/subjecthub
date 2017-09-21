package com.example.subjecthub.api;

import com.example.subjecthub.entity.Subject;
import com.example.subjecthub.entity.SubjectComment;
import org.bouncycastle.cert.ocsp.Req;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("/api/universities/university/{universityId}/subjects")
@CrossOrigin(origins = "http://localhost:4200")
public interface SubjectServiceApi {

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<Subject> getSubjects(
        @PathVariable Long universityId,
        @RequestParam(required = false) String subjectCode,
        @RequestParam(required = false) String name,
        @RequestParam(required = false) Long facultyId,
        @RequestParam(required = false) String facultyName,
        @RequestParam(required = false) Double ratingStart,
        @RequestParam(required = false) Double ratingEnd,
        @RequestParam(required = false) Integer creditPoints,
        @RequestParam(required = false) String instructor
    );

    @RequestMapping(value = "/subject/{subjectId}", method = RequestMethod.GET)
    public Subject getSubject(
        @PathVariable Long universityId,
        @PathVariable Long subjectId
    );

    @RequestMapping(value = "/users/user/{userId}/comments", method = RequestMethod.GET)
    public List<SubjectComment> getCommentsByUser(
        @PathVariable Long universityId,
        @PathVariable Long userId
    );

    @RequestMapping(value = "/subject/{subjectId}/comments", method = RequestMethod.GET)
    public List<SubjectComment> getComments(
        @PathVariable Long universityId,
        @PathVariable Long subjectId
    );

    @RequestMapping(value = "/subject/{subjectId}/comments/comment/{commentId}", method = RequestMethod.GET)
    public SubjectComment getComment(
        @PathVariable Long universityId,
        @PathVariable Long subjectId,
        @PathVariable Long commentId
    );

    @RequestMapping(value = "/subject/{subjectId}/comments/comment/add", method = RequestMethod.GET)
    public SubjectComment commentAdd(
        @PathVariable Long universityId,
        @PathVariable Long subjectId,
        @RequestParam Long userId,
        @RequestParam String comment
    );

    @RequestMapping(value = "/subject/{subjectId}/comments/comment/{commentId}/thumbUp", method = RequestMethod.GET)
    public SubjectComment commentThumbUp(
        @PathVariable Long universityId,
        @PathVariable Long subjectId,
        @PathVariable Long commentId
    );

    @RequestMapping(value = "/subject/{subjectId}/comments/comment/{commentId}/thumbDown", method = RequestMethod.GET)
    public SubjectComment commentThumbDown(
        @PathVariable Long universityId,
        @PathVariable Long subjectId,
        @PathVariable Long commentId
    );

    @RequestMapping(value = "/subject/{subjectId}/comments/comment/{commentId}/flag", method = RequestMethod.GET)
    public SubjectComment commentFlag(
        @PathVariable Long universityId,
        @PathVariable Long subjectId,
        @PathVariable Long commentId
    );

    @RequestMapping(value = "/subject/{subjectId}/comments/comment/{commentId}/unflag", method = RequestMethod.GET)
    public SubjectComment commentUnflag(
        @PathVariable Long universityId,
        @PathVariable Long subjectId,
        @PathVariable Long commentId
    );
}
