package com.minimi.domain.user.repostory;

import com.minimi.domain.user.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Board,Long>, PostRepositoryCustom {
}
