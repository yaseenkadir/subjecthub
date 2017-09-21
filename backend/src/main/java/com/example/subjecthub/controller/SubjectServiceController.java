package com.example.subjecthub.controller;

import com.example.subjecthub.Application;
import com.example.subjecthub.api.SubjectServiceApi;
import com.example.subjecthub.entity.Subject;
import com.example.subjecthub.entity.SubjectComment;
import com.example.subjecthub.entity.SubjectHubUser;
import com.example.subjecthub.repository.SubjectCommentRepository;
import com.example.subjecthub.repository.SubjectHubUserRepository;
import com.example.subjecthub.repository.SubjectRepository;
import com.example.subjecthub.utils.FuzzyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@ParametersAreNonnullByDefault
@CrossOrigin(origins = "http://localhost:4200")
public class SubjectServiceController implements SubjectServiceApi {

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private SubjectHubUserRepository subjectHubUserRepository;

    @Autowired
    private SubjectCommentRepository subjectCommentRepository;

    @Override
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
    ) {
        // TODO: Move subjectCode retrieval to it's own method.
        // It is unique within a uni. We shouldn't return a list.
        Application.log.info("Received:\nsubjectCode {}\nfacultyId: {}\nratingStart: {}\n" +
                "ratingEnd: {}\nuniversityId: {}", subjectCode, facultyId, ratingStart, ratingEnd,
            universityId
        );

        // If subjectCode is supplied we don't check other GET params
        if (subjectCode != null) {
            return subjectRepository.findByCodeContainingIgnoreCase(subjectCode);
        }

        // If any of the params are not null we filter by their criteria.
        return subjectRepository.findByFaculty_University_Id(universityId).stream()
            .filter(s -> (facultyId == null || s.getFaculty().getId().equals(facultyId)))
            .filter(s -> (facultyName == null ||
                    FuzzyUtils.isSimilar(s.getFaculty().getName(), facultyName)))
            .filter(s -> (name == null || FuzzyUtils.isSimilar(s.getName(), name)))
            .filter(s -> (creditPoints == null || s.getCreditPoints() == creditPoints))
            .filter(s -> (ratingStart == null || s.getRating() >= ratingStart))
            .filter(s -> (ratingEnd == null) || s.getRating() <= ratingEnd)
            .collect(Collectors.toList());
    }

    @Override
    public Subject getSubject(
        @PathVariable Long universityId,
        @PathVariable Long subjectId
    ) {
        // TODO: Move annotations from Service interface to implementation
        // TODO: Return null or throw exception if no subject is found
        // TODO: Don't allow cross university fetching
        return subjectRepository.findOne(subjectId);
    }

    @Override
    public List<SubjectComment> getCommentsByUser(
        @PathVariable Long universityId,
        @PathVariable Long userId
    ){
        return subjectCommentRepository.findByUser_Id(userId);
    }

    //TODO: ALL COMMENT IMPLEMENTATIONS: as above, reflect null on no subject, no cross-uni fetch
    @Override
    public List<SubjectComment> getComments(
        @PathVariable Long universityId,
        @PathVariable Long subjectId
    ){
        return subjectCommentRepository.findBySubject_Id(subjectId);
    }

    @Override
    public SubjectComment getComment(
        @PathVariable Long universityId,
        @PathVariable Long subjectId,
        @PathVariable Long commentId
    ){
        return subjectCommentRepository.findBySubject_IdAndId(subjectId,commentId);
    }

    @Override
    public SubjectComment commentAdd(
        @PathVariable Long universityId,
        @PathVariable Long subjectId,
        @RequestParam Long userId,
        @RequestParam String comment
    ){
        SubjectComment newComment = new SubjectComment();
        newComment.setPost(comment);
        newComment.setUser(subjectHubUserRepository.findOne(userId));
        newComment.setSubject(subjectRepository.findOne(subjectId));
        return subjectCommentRepository.save(newComment);
    }

    @Override
    public SubjectComment commentThumbUp(
        @PathVariable Long universityId,
        @PathVariable Long subjectId,
        @PathVariable Long commentId
    ){
        SubjectComment comment = subjectCommentRepository.findBySubject_IdAndId(subjectId,commentId);
        comment.addThumbUp();
        return subjectCommentRepository.save(comment);
    }

    @Override
    public SubjectComment commentThumbDown(
        @PathVariable Long universityId,
        @PathVariable Long subjectId,
        @PathVariable Long commentId
    ){
        SubjectComment comment = subjectCommentRepository.findBySubject_IdAndId(subjectId,commentId);
        comment.addThumbDown();
        return subjectCommentRepository.save(comment);
    }

    @Override
    public SubjectComment commentFlag(
        @PathVariable Long universityId,
        @PathVariable Long subjectId,
        @PathVariable Long commentId
    ){
        SubjectComment comment = subjectCommentRepository.findBySubject_IdAndId(subjectId,commentId);
        comment.setFlagged(true);
        return subjectCommentRepository.save(comment);
    }

    @Override
    public SubjectComment commentUnflag(
        @PathVariable Long universityId,
        @PathVariable Long subjectId,
        @PathVariable Long commentId
    ){
        SubjectComment comment = subjectCommentRepository.findBySubject_IdAndId(subjectId,commentId);
        comment.setFlagged(false);
        return subjectCommentRepository.save(comment);
    }
}
