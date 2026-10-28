package com.chambule.controle_gastos.mapper;
import com.chambule.controle_gastos.dto.launch.LaunchResponse;
import com.chambule.controle_gastos.entities.Launch;
public class LaunchMapper {

    public  static LaunchResponse toLaunch(Launch launch){

        LaunchResponse response = new LaunchResponse();
        response.setId(launch.getId());
        response.setDescription(launch.getDescription());
        response.setValue(launch.getValue());
        response.setType(launch.getLaunchType());
        response.setTransactionDate(launch.getTransactionDate());
        response.setPaymentMethod(launch.getPaymentMethod());
        return response;
    }
}
