package com.bhanu.movie_booking_system.controller;

import com.bhanu.movie_booking_system.dto.request.UserRequest;
import com.bhanu.movie_booking_system.dto.response.UserResponse;
import com.bhanu.movie_booking_system.entity.User;
import com.bhanu.movie_booking_system.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
//@RequestMapping("/api/users")
public class UserController{
    List<Student> students=new ArrayList<>(List.of(new Student(1,"bhanu",50),new Student(2,"ram",90)));
    @Autowired
    UserService userService;
    @GetMapping("/")
    public String home(HttpServletRequest request) {
        return "HOME" + request.getSession().getId();
    }
    @PostMapping("/register")
    public UserResponse registerUser(@Valid  @RequestBody UserRequest userRequest)
    {
        return userService.registerUser(userRequest);
    }
    @GetMapping("/student")
    public List<Student> getStudent()
    {
        return students;
    }
    @GetMapping("/csrf-token")
    public CsrfToken getCsrf(HttpServletRequest request)
    {
        return (CsrfToken) request.getAttribute("_csrf");
    }

    @PostMapping("/student")
    public String addStudent(@RequestBody Student student)
    {
        students.add(student);
        return student.toString();
    }
    @PostMapping("/login")
    public String login(@RequestBody User user)
    {

        return userService.verify(user);
    }



}
