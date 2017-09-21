package com.example.subjecthub.repository;

import com.example.subjecthub.entity.Subject;
import com.example.subjecthub.entity.SubjectComment;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface SubjectCommentRepository extends CrudRepository<SubjectComment, Long> {

    List<SubjectComment> findBySubject_Id(Long subject_id);

    List<SubjectComment> findByUser_Id(Long user_id);

    SubjectComment findBySubject_IdAndId(Long subject_id, Long comment_id);
}
