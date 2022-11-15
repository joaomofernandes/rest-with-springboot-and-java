package com.example.demo.Service;

import com.example.demo.Controllers.v1.PersonControllerV1;
import com.example.demo.Exceptions.RequiredObjectIsNullException;
import com.example.demo.Exceptions.ResourceNotFoundException;
import com.example.demo.Model.Person;
import com.example.demo.Model.User;
import com.example.demo.Repository.PersonRepository;
import com.example.demo.Repository.UserRepository;
import com.example.demo.data.vo.v1.PersonVO;
import com.example.demo.data.vo.v2.PersonVOV2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    private Logger logger = Logger.getLogger(PersonService.class.getName());

    private ModelMapper mapper = new ModelMapper();

    @Autowired
    UserRepository userRepository;
    @Autowired
    PersonMapper personMapper;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("Finding one user with name " + username);
        User user = userRepository.findByUsername(username);
        if (user != null){
            return user;
        } else{
            throw new UsernameNotFoundException("Username " + username + " not found!");
        }
    }
}
