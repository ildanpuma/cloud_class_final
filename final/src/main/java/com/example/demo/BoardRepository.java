package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.Board;

public interface BoardRepository extends JpaRepository<Board, Integer> {
}
