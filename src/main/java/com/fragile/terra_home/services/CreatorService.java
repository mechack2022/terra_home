package com.fragile.terra_home.services;

import com.fragile.terra_home.entities.User;

public interface CreatorService {

    User getCreatorProfile(String email);

    User findUserByJwt(String jwt);

    User findUserByEmail(String email);
}
