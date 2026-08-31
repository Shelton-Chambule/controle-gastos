package com.chambule.controle_gastos.entities.enums;
public enum CategoryType {

    INCOME(1),
    EXPENSE(2);

    private Integer code;

    CategoryType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static CategoryType category(int code){
        for(CategoryType categoryType: CategoryType.values()){
            if(categoryType.getCode() ==  code){
                return categoryType;
            }
        }
        throw new IllegalArgumentException("invalid code");
    }
}
