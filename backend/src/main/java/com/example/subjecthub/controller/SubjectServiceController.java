package com.example.subjecthub.controller;

import com.example.subjecthub.Application;
import com.example.subjecthub.api.SubjectServiceApi;
import com.example.subjecthub.dto.AddCommentRequest;
import com.example.subjecthub.entity.Subject;

import com.example.subjecthub.entity.Tag;

import com.example.subjecthub.entity.SubjectComment;
import com.example.subjecthub.entity.SubjectHubUser;
import com.example.subjecthub.repository.SubjectCommentRepository;
import com.example.subjecthub.repository.SubjectHubUserRepository;

import com.example.subjecthub.repository.SubjectRepository;
import com.example.subjecthub.repository.TagRepository;
import com.example.subjecthub.utils.FuzzyUtils;

import com.example.subjecthub.utils.SubjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.ParametersAreNonnullByDefault;

import com.example.subjecthub.utils.SubjectHubException;
import com.example.subjecthub.utils.SubjectHubUnexpectedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@ParametersAreNonnullByDefault
@CrossOrigin(origins = "*")
public class SubjectServiceController implements SubjectServiceApi {

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private SubjectHubUserRepository subjectHubUserRepository;

    @Autowired
    private SubjectCommentRepository subjectCommentRepository;


    @Override
    public Subject addTagToSubject(@PathVariable Long universityId,  @PathVariable Long subjectId, @RequestBody Tag tag) {

        Subject currentSubject = subjectRepository.findOne(subjectId);

        if (currentSubject == null) {
            throw new SubjectNotFoundException();
        }

        Tag existingTag = tagRepository.findByName(tag.getName());

        if (existingTag != null) {

            // if the subject is included on the tag then throw an error
            if (existingTag.getSubjects().contains(currentSubject)) {
                throw new SubjectHubException("That Tag Already Exists");
            }

            existingTag.getSubjects().add(currentSubject);
            tagRepository.save(existingTag);

            currentSubject.getTags().add(existingTag);
            subjectRepository.save(currentSubject);

            return currentSubject;
        }

        tag.getSubjects().add(currentSubject);
        currentSubject.getTags().add(tag);

        subjectRepository.save(currentSubject);
        return currentSubject;

    }



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
        @RequestBody AddCommentRequest addCommentRequest
    ){
        SubjectHubUser requester = getRequestingUser();
        SubjectComment newComment = new SubjectComment();
        newComment.setPost(addCommentRequest.getComment());
        newComment.setUser(requester);
        newComment.setSubject(subjectRepository.findOne(subjectId));
        newComment.setPostTimeNow();
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

    /**
     * Util method for POST methods
     */
    private SubjectHubUser getRequestingUser(){
        UserDetails userDetails;
        try {
            userDetails =
                (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (NullPointerException|ClassCastException e) {
            throw new SubjectHubException("Not logged in.");
        }

        Optional<SubjectHubUser> user = subjectHubUserRepository.findByUsername(
            userDetails.getUsername());

        if (!user.isPresent()) {
            throw new SubjectHubUnexpectedException(String.format("User %s is authenticated but " +
            "was not found in database", userDetails.getUsername()));
        }
        return user.get();
    }
}
