package com.example.subjecthub.api;

import com.example.subjecthub.dto.AddCommentRequest;
import com.example.subjecthub.entity.Subject;

import com.example.subjecthub.entity.Tag;

import com.example.subjecthub.entity.SubjectComment;
import org.bouncycastle.cert.ocsp.Req;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RequestMapping("/api/universities/university/{universityId}/subjects")
@CrossOrigin(origins = "*")
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

    @PreAuthorize("hasAuthority('USER')")
    @RequestMapping(value = "/subject/{subjectId}/addTag", method = RequestMethod.POST)
    public Subject addTagToSubject(@PathVariable Long universityId, @PathVariable Long subjectId, @RequestBody Tag tag);

    @RequestMapping(value = "/subject/{subjectId}", method = RequestMethod.GET)
    public Subject getSubject(
        @PathVariable Long universityId,
        @PathVariable Long subjectId
    );

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(value = "/subject/{subjectId}", method = RequestMethod.DELETE)
    public void deleteSubject(
        @PathVariable Long universityId,
        @PathVariable Long subjectId
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

    @PreAuthorize("hasAuthority('USER')")
    @RequestMapping(value = "/subject/{subjectId}/comments/comment/add", method = RequestMethod.POST)
    public SubjectComment commentAdd(
        @PathVariable Long universityId,
        @PathVariable Long subjectId,
        @RequestBody AddCommentRequest addCommentRequest
    );

    @PreAuthorize("hasAuthority('USER')")
    @RequestMapping(value = "/subject/{subjectId}/comments/comment/{commentId}/addThumbUp", method = RequestMethod.GET)
    public SubjectComment commentAddThumbUp(
        @PathVariable Long universityId,
        @PathVariable Long subjectId,
        @PathVariable Long commentId
    );

    @PreAuthorize("hasAuthority('USER')")
    @RequestMapping(value = "/subject/{subjectId}/comments/comment/{commentId}/addThumbDown", method = RequestMethod.GET)
    public SubjectComment commentAddThumbDown(
        @PathVariable Long universityId,
        @PathVariable Long subjectId,
        @PathVariable Long commentId
    );

    @PreAuthorize("hasAuthority('USER')")
    @RequestMapping(value = "/subject/{subjectId}/comments/comment/{commentId}/flag", method = RequestMethod.GET)
    public SubjectComment commentFlag(
        @PathVariable Long universityId,
        @PathVariable Long subjectId,
        @PathVariable Long commentId
    );

    @PreAuthorize("hasAuthority('USER')")
    @RequestMapping(value = "/subject/{subjectId}/comments/comment/{commentId}/unflag", method = RequestMethod.GET)
    public SubjectComment commentUnflag(
        @PathVariable Long universityId,
        @PathVariable Long subjectId,
        @PathVariable Long commentId
    );
}
