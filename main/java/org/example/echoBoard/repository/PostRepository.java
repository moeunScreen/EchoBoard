package org.example.echoBoard.repository;


import org.example.echoBoard.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUserId(Long userId);
    List<Post> findAllByOrderByCreatedAtDesc();
    Page<Post> findAllByOrderByCreatedAtDesc(Pageable pageable);
    @Query("SELECT p.id FROM Post p")
    List<Long> findAllPostIds();

    @Query("""
       SELECT p 
       FROM Post p 
       WHERE p.title LIKE CONCAT('%', :query, '%')
          OR p.content LIKE CONCAT('%', :query, '%')
       """)
    Page<Post> findByKeyword(@Param("query") String query, Pageable pageable);

}