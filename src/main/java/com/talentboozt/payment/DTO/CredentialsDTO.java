package com.talentboozt.payment.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CredentialsDTO {
    private String employeeId;
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private String role;
    private String userLevel; // Free, Pro
}
