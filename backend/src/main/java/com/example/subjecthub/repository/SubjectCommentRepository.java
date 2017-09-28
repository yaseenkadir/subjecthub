package com.example.subjecthub.repository;

import com.example.subjecthub.entity.Subject;
import com.example.subjecthub.entity.SubjectComment;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface SubjectCommentRepository extends CrudRepository<SubjectComment, Long> {

    List<SubjectComment> findBySubject_Id(Long subjectId);

    SubjectComment findBySubject_IdAndId(Long subjectId, Long commentId);
}
