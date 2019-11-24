package com.example.DateCalculator.form;

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
public class DateInputForm {
    @Pattern(regexp = "\\d{4}\\d{2}\\d{2}", message = "有効な日付を入力してください")
    @NotBlank(message = "入力必須です")
    private String date;
    private String dateId;
}
