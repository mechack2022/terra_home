package com.fragile.terra_home.services;

import com.fragile.terra_home.entities.User;
import com.fragile.terra_home.exceptions.UserException;
import com.fragile.terra_home.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CreatorServiceImpl implements CreatorService {

    private final UserRepository userRepository;
    @Override
    public User getCreatorProfile(String email) {
        return findUserByEmail(email);
    }

    @Override
    public User findUserByEmail(String email){
      Optional<User> user = Optional.ofNullable(userRepository.findByEmail(email));
      if(user.isEmpty()){
          throw new UserException("User not found");
      }else{
          return user.get();
      }
    }
}
