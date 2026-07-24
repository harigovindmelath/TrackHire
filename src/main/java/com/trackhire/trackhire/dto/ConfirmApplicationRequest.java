package com.trackhire.trackhire.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConfirmApplicationRequest {
    private String companyName;
    private String role;
    private String skills;
    private String location;
    private String experienceRequired;
    private String jobDescriptionRaw;
}