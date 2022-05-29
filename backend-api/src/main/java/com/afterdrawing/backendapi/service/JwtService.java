package com.afterdrawing.backendapi.service;


import com.afterdrawing.backendapi.core.entity.User;
import com.afterdrawing.backendapi.core.repository.UserRepository;
import com.afterdrawing.backendapi.sercurity.JWTUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class JwtService implements UserDetailsService {

    @Autowired
    private JWTUtility JWTUtility;

    @Autowired
    private UserRepository userDao;





    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //User user = userDao.findByEmail(username).get();

        /*if (null == user) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        } else {
            return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), new ArrayList<>());
        }*/

        return new org.springframework.security.core.userdetails.User("admin","password",new ArrayList<>());


    }

}
