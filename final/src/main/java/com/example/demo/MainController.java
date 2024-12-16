package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(path="/demo")
public class MainController {
    @Autowired
    private BoardRepository boardRepository;

    @PostMapping(path="/add")
    public String addNewBoard (@RequestParam int password, @RequestParam String nickname, @RequestParam String body) {
        Board n = new Board();
        n.setPassword(password);
        n.setBody(body);
        n.setNickname(nickname);
        boardRepository.save(n);
        return "redirect:/demo/board";
    }

    @PostMapping(path="/delete")
    public String deleteBoard(@RequestParam int id, @RequestParam int password) {
        Board board = boardRepository.findById(id).orElse(null);
        if (board != null && board.getPassword() == password) {//삭제조건 password가 일치할시
            boardRepository.deleteById(id);
            return "redirect:/demo/view?id=" + id + "&status=success";
        } else {
            return "redirect:/demo/view?id=" + id + "&status=failure";
        }
    }

    @GetMapping(path="/board")
    public String getAllBoards(Model model, @RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, 10); //10페이지마다 페이징
        Page<Board> boardPage = boardRepository.findAll(pageable);
        model.addAttribute("boards", boardPage.getContent());
        model.addAttribute("totalPages", boardPage.getTotalPages());
        model.addAttribute("currentPage", page);
        return "board";
    }

    @GetMapping(path="/add")
    public String showAddBoardForm(Model model) {
        model.addAttribute("board", new Board());
        return "addBoard";
    }

    @GetMapping(path="/view")
    public String viewBoard(@RequestParam int id, @RequestParam(required = false) String status, Model model) {
        Board board = boardRepository.findById(id).orElse(null);
        if (board != null) {
            model.addAttribute("board", board);
            if (status != null) {
                model.addAttribute("status", status);
            }
            return "viewBoard";
        }
        return "redirect:/demo/board";
    }
}