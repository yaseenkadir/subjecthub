package com.example.subjecthub.controller;

import com.example.subjecthub.Application;
import com.example.subjecthub.api.SubjectServiceApi;
import com.example.subjecthub.dto.AddCommentRequest;
import com.example.subjecthub.entity.Subject;
import com.example.subjecthub.entity.SubjectComment;
import com.example.subjecthub.entity.SubjectHubUser;
import com.example.subjecthub.entity.Tag;
import com.example.subjecthub.exception.SubjectHubException;
import com.example.subjecthub.exception.SubjectHubUnexpectedException;
import com.example.subjecthub.repository.SubjectCommentRepository;
import com.example.subjecthub.repository.SubjectHubUserRepository;
import com.example.subjecthub.repository.SubjectRepository;
import com.example.subjecthub.repository.TagRepository;
import com.example.subjecthub.utils.FuzzyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.annotation.ParametersAreNonnullByDefault;
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
    public Subject addTagToSubject(
        @PathVariable Long universityId,
        @PathVariable Long subjectId,
        @RequestBody Tag tag
    ) {

        // TODO: call getSubject() here instead of manually null checking
        Subject currentSubject = subjectRepository.findOne(subjectId);

        if (currentSubject == null) {
            throw new SubjectHubException(HttpStatus.NOT_FOUND,
                "Subject not found. Unable to add tag.");
        }

        Tag existingTag = tagRepository.findByName(tag.getName());

        if (existingTag != null) {

            // if the subject is included on the tag then throw an error
            if (existingTag.getSubjects().contains(currentSubject)) {
                throw new SubjectHubException("Tag already exists for subject.");
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
    ) {
        List<SubjectComment> results = subjectCommentRepository.findBySubject_Id(subjectId);
        if (results.isEmpty()) {
            throw new SubjectHubException(String.format("No comments found for subject id: %s", subjectId));
        }
        return results;
    }

    @Override
    public SubjectComment getComment(
        @PathVariable Long universityId,
        @PathVariable Long subjectId,
        @PathVariable Long commentId
    ) {
        SubjectComment result = subjectCommentRepository.findOne(commentId);
        if (result == null) {
            throw new SubjectHubException(String.format("Specified comment id: %s, not found for subject" +
                " id: %s", commentId, subjectId));
        }
        return result;
    }

    @Override
    public SubjectComment commentAdd(
        @PathVariable Long universityId,
        @PathVariable Long subjectId,
        @RequestBody AddCommentRequest addCommentRequest
    ) {
        SubjectHubUser requester = getRequestingUser();
        SubjectComment newComment = new SubjectComment();
        newComment.setPost(addCommentRequest.getComment());
        newComment.setUser(requester);
        newComment.setSubject(subjectRepository.findOne(subjectId));
        newComment.setPostTimeNow();
        return subjectCommentRepository.save(newComment);
    }

    @Override
    public SubjectComment commentAddThumbUp(
        @PathVariable Long universityId,
        @PathVariable Long subjectId,
        @PathVariable Long commentId
    ) {
        SubjectComment result = subjectCommentRepository.findOne(commentId);
        if (result == null) {
            throw new SubjectHubException(String.format(
                "Specified comment id: %s, not found for subject id: %s. Unable to add thumb up.",
                commentId, subjectId));
        }
        result.addThumbUp();
        return subjectCommentRepository.save(result);
    }

    @Override
    public SubjectComment commentAddThumbDown(
        @PathVariable Long universityId,
        @PathVariable Long subjectId,
        @PathVariable Long commentId
    ) {
        SubjectComment result = subjectCommentRepository.findOne(commentId);
        if (result == null) {
            throw new SubjectHubException(String.format(
                "Specified comment id: %s, not found for subject id: %s. Unable to add thumb down.",
                commentId, subjectId));
        }
        result.addThumbDown();
        return subjectCommentRepository.save(result);
    }

    @Override //flagged comment body should be hidden with placeholder text from frontend view
    public SubjectComment commentFlag(
        @PathVariable Long universityId,
        @PathVariable Long subjectId,
        @PathVariable Long commentId
    ) {
        SubjectComment result = subjectCommentRepository.findOne(commentId);
        if (result == null) {
            throw new SubjectHubException(String.format(
                "Specified comment id: %s, not found for subject id: %s. Unable to flag.",
                commentId, subjectId));
        }
        result.setFlagged(true);
        return subjectCommentRepository.save(result);
    }

    @Override
    public SubjectComment commentUnflag(
        @PathVariable Long universityId,
        @PathVariable Long subjectId,
        @PathVariable Long commentId
    ) {
        SubjectComment result = subjectCommentRepository.findOne(commentId);
        if (result == null) {
            throw new SubjectHubException(String.format(
                "Specified comment id: %s, not found for subject id: %s. Unable to unflag.",
                commentId, subjectId));
        }
        result.setFlagged(false);
        return subjectCommentRepository.save(result);
    }

    /**
     * Util method for POST methods
     */
    private SubjectHubUser getRequestingUser() {
        UserDetails userDetails;
        try {
            userDetails =
                (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (NullPointerException | ClassCastException e) {
            throw new SubjectHubException("Not logged in.");
        }

        Optional<SubjectHubUser> user = subjectHubUserRepository.findByUsername(
            userDetails.getUsername());

        if (!user.isPresent()) {
            throw new SubjectHubUnexpectedException(String.format(
                "User %s is authenticated but was not found in database",
                userDetails.getUsername()));
        }
        return user.get();
    }
}
