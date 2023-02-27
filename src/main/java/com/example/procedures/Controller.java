package com.example.procedures;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cats")
public class Controller {
    @Autowired
    private CatService catService;

    @PostMapping("/create")
    public int create(@RequestBody Cat cat) {
        return catService.create(cat);
    }

    @GetMapping("/{id}")
    public Cat getById(@PathVariable Integer id) {
        return catService.getById(id);
    }

    @GetMapping("/showAllProcedures")
    public List<String> showAll() {
        return catService.showAllProcedures();
    }
}
