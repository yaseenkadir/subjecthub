package com.example.subjecthub.controller;

import com.example.subjecthub.Application;
import com.example.subjecthub.api.CommentServiceApi;
import com.example.subjecthub.api.SubjectServiceApi;
import com.example.subjecthub.dto.AddCommentRequest;
import com.example.subjecthub.entity.Subject;
import com.example.subjecthub.entity.SubjectComment;
import com.example.subjecthub.entity.SubjectHubUser;
import com.example.subjecthub.exception.SubjectHubException;
import com.example.subjecthub.exception.SubjectHubUnexpectedException;
import com.example.subjecthub.repository.SubjectCommentRepository;
import com.example.subjecthub.repository.SubjectHubUserRepository;
import com.example.subjecthub.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/universities/university/{universityId}/subjects/subject/{subjectId}/comments")
@CrossOrigin("*")
public class CommentsServiceController implements CommentServiceApi {

    @Autowired
    private SubjectServiceApi subjectServiceApi;

    @Autowired
    private SubjectCommentRepository subjectCommentRepository;

    @Autowired
    private SubjectHubUserRepository subjectHubUserRepository;

    @Override
    @Nonnull
    @RequestMapping(value = "/comment/{commentId}", method = RequestMethod.GET)
    public SubjectComment getComment(
        @PathVariable Long universityId,
        @PathVariable Long subjectId,
        @PathVariable Long commentId
    ) {
        SubjectComment comment = subjectCommentRepository.findOne(commentId);
        Utils.ifNull404(comment, "Comment not found.");
        return comment;
    }

    @Override
    @RequestMapping(method = RequestMethod.GET)
    public List<SubjectComment> getComments(
        @PathVariable Long universityId,
        @PathVariable Long subjectId
    ) {
        // Using getSubject() because it handles 404 check
        return subjectServiceApi.getSubject(universityId, subjectId).getComments();
    }


    @Override
    @PreAuthorize("hasAuthority('USER')")
    @RequestMapping(value = "/comment/add", method = RequestMethod.POST)
    public SubjectComment commentAdd(
        @PathVariable Long universityId,
        @PathVariable Long subjectId,
        @RequestBody AddCommentRequest addCommentRequest
    ) {
        // getSubject() will throw 404 if subject doesn't exist
        Subject s = subjectServiceApi.getSubject(universityId, subjectId);
        SubjectHubUser requester = getRequestingUser();
        SubjectComment newComment = new SubjectComment();
        newComment.setPost(addCommentRequest.getComment());
        newComment.setUser(requester);
        newComment.setSubject(s);
        newComment.setPostTimeNow();
        newComment.setFlagged(false);
        Application.log.info("{} commented \"{}\" on {}", requester.getUsername(),
            addCommentRequest.getComment(), s);
        return subjectCommentRepository.save(newComment);
    }

    @Override
    @PreAuthorize("hasAuthority('USER')")
    @RequestMapping(value = "/comment/{commentId}/addThumbUp", method = RequestMethod.GET)
    public SubjectComment commentAddThumbUp(
        @PathVariable Long universityId,
        @PathVariable Long subjectId,
        @PathVariable Long commentId
    ) {
        SubjectComment result = getComment(universityId, subjectId, commentId);
        result.addThumbUp();
        return subjectCommentRepository.save(result);
    }

    @Override
    @PreAuthorize("hasAuthority('USER')")
    @RequestMapping(value = "/comment/{commentId}/addThumbDown", method = RequestMethod.GET)
    public SubjectComment commentAddThumbDown(
        @PathVariable Long universityId,
        @PathVariable Long subjectId,
        @PathVariable Long commentId
    ) {
        SubjectComment result = getComment(universityId, subjectId, commentId);
        result.addThumbDown();
        return subjectCommentRepository.save(result);
    }

    @Override //flagged comment body should be hidden with placeholder text from frontend view
    @PreAuthorize("hasAuthority('USER')")
    @RequestMapping(value = "/comment/{commentId}/flag", method = RequestMethod.GET)
    public SubjectComment commentFlag(
        @PathVariable Long universityId,
        @PathVariable Long subjectId,
        @PathVariable Long commentId
    ) {
        SubjectComment result = getComment(universityId, subjectId, commentId);
        result.setFlagged(true);
        return subjectCommentRepository.save(result);
    }

    @Override
    @PreAuthorize("hasAuthority('USER')")
    @RequestMapping(value = "/comment/{commentId}/unflag", method = RequestMethod.GET)
    public SubjectComment commentUnflag(
        @PathVariable Long universityId,
        @PathVariable Long subjectId,
        @PathVariable Long commentId
    ) {
        SubjectComment result = getComment(universityId, subjectId, commentId);
        result.setFlagged(false);
        return subjectCommentRepository.save(result);
    }

    /**
     * Util method for POST methods
     *
     * TODO: Create an AuthServiceApi and get user details via that
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
