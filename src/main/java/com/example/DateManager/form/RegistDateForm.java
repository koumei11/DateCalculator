package com.example.DateManager.form;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegistDateForm {
    @NotBlank(message = "日付IDを入力してください")
    private String dateId;
    @NotBlank(message = "日付名を入力してください")
    private String dateType;
    private int year;
    private int month;
    private int day;
    public boolean isNew;
}
