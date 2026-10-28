package com.chambule.controle_gastos.mapper;
import com.chambule.controle_gastos.dto.user.UserResponse;
import com.chambule.controle_gastos.entities.User;

public class UserMapper {

    public  static UserResponse toResponse(User user){

        UserResponse response = new UserResponse();
        response.setUserId(user.getId());
        response.setEmail(user.getLogin());
        response.setCreationDate(user.getCreationDate());

        return  response;
    }
}
