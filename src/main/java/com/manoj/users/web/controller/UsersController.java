package com.manoj.users.web.controller;

import com.manoj.users.dto.UserDTO;
import com.manoj.users.service.UsersService;
import com.manoj.users.web.model.request.UserDetailsRequestModel;
import com.manoj.users.web.model.response.UserRest;
import org.springframework.beans.BeanUtils;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.modelmapper.ModelMapper;

@RestController
@RequestMapping("/api/users")
public class UsersController {

    private Environment environment;
    private UsersService usersService;

    public UsersController(Environment environment, UsersService usersService) {
        this.environment = environment;
        this.usersService = usersService;
    }

    @GetMapping("/status/check")
    public String status() {
        return "Users Service Running on "+environment.getProperty("local.server.port") +", with token "+environment.getProperty("token.secret");
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<UserRest> createUser(@RequestBody UserDetailsRequestModel userDetailsRequestModel) {

//        if(userDetailsRequestModel.getLastName().isEmpty()) throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());

//        BeanUtils.copyProperties(userDetailsRequestModel, userDTO);
        ModelMapper mapper = new ModelMapper();
        UserDTO userDTO = mapper.map(userDetailsRequestModel, UserDTO.class);

        UserDTO savedUser = usersService.createUser(userDTO);
        UserRest response = mapper.map(savedUser, UserRest.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<UserRest> getUser(@PathVariable String id) {
        UserRest response = new UserRest();

        UserDTO userDTO = usersService.getUserByUserId(id);

        BeanUtils.copyProperties(userDTO, response);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
