package com.example.gistcompetitioncnserver.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostId(Long postId);
    Optional<Comment> findByCommentId(Long postId);

    @Query("select c.userId from Comment c where c.commentId = :commentId")
    Long findUserIdByCommentId(@Param("commentId") Long commentId);

    @Transactional
    @Modifying
    @Query("delete from Comment c where c.commentId in :ids")
    void deleteAllByPostIdInQuery(@Param("ids") List<Long> ids);
}
