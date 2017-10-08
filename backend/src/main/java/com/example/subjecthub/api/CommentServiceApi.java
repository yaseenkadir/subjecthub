package com.example.subjecthub.api;

import com.example.subjecthub.dto.AddCommentRequest;
import com.example.subjecthub.entity.SubjectComment;

import java.util.List;

public interface CommentServiceApi {

    SubjectComment getComment(
        Long universityId,
        Long subjectId,
        Long commentId
    );

    List<SubjectComment> getComments(
        Long universityId,
        Long subjectId
    );

    SubjectComment commentAdd(
        Long universityId,
        Long subjectId,
        AddCommentRequest addCommentRequest
    );

    SubjectComment commentAddThumbUp(
        Long universityId,
        Long subjectId,
        Long commentId
    );

    SubjectComment commentAddThumbDown(
        Long universityId,
        Long subjectId,
        Long commentId
    );

    SubjectComment commentFlag(
        Long universityId,
        Long subjectId,
        Long commentId
    );

    SubjectComment commentUnflag(
        Long universityId,
        Long subjectId,
        Long commentId
    );
}
