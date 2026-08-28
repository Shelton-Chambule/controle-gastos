package com.chambule.controle_gastos.entities.enums;
public enum LaunchType {

    INCOME(1),
    EXPENSE(2);

    private int code;

     LaunchType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static LaunchType launch(int code){
         for(LaunchType launchType: LaunchType.values()){
             if(launchType.getCode() == code){
                 return launchType;
             }
         }
         throw new IllegalArgumentException("invalid code");
    }

}
