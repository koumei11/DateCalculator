package com.example.DateManager.controller;

import com.example.DateManager.NotExecuteAnyMoreException;
import com.example.DateManager.form.DateInputForm;
import com.example.DateManager.form.RegistDateForm;
import com.example.DateManager.service.DateManageService;
import com.example.DateManager.utilities.Formula;
import com.example.DateManager.utilities.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/datemanager")
public class DateManageController {

    private DateManageService service;

    // DateManageServiceをDIコンテナに格納
    @Autowired
    public DateManageController(DateManageService service) {
        this.service = service;
    }

    /**
     * 初期表示
     * @param inputForm DateInputFormオブジェクト
     * @param model モデル
     * @return 表示ページ
     */
    @GetMapping
    public String display(@ModelAttribute("inputForm") DateInputForm inputForm,
                          Model model) {

        // データベースから計算式を取得
        List<Formula> formulas = service.getAll();

        Map<String, String> map = getDateIdAndDateType(formulas);

        // 日付IDと日付名のMapをModelに格納
        model.addAttribute("map", map);

        return "date/index";
    }

    /**
     * 計算結果を表示します
     * @param inputForm DateInputFormオブジェクト
     * @param result バリデーション
     * @param model モデル
     * @return 表示ページ
     */
    @PostMapping("/result")
    public String displayResults(@Validated @ModelAttribute("inputForm") DateInputForm inputForm,
                                 BindingResult result,
                                 Model model) {

        // データベースから計算式を取得
        List<Formula> formulas = service.getAll();

        Map<String, String> map = getDateIdAndDateType(formulas);

        // 日付IDと日付名のMapをModelに格納
        model.addAttribute("map", map);

        // 削除後にページから取得結果が消えないようにモデルに格納
        model.addAttribute("userInputDate", inputForm.getDate());
        model.addAttribute("userInputDateId", inputForm.getDateId());

        // バリデーションチェック
        if(result.hasErrors()){
            return "date/index";
        }

        List<Formula> results = getResults(inputForm, formulas);
        // 結果をModelに格納
        model.addAttribute("results", results);
        return "date/index";
    }

    /**
     * 新規登録ページ
     * @param registForm RegistDateFormオブジェクト
     * @param model モデル
     * @return 表示ページ
     */
    @GetMapping("/createFormula")
    public String showInsertPage(@ModelAttribute("registForm")RegistDateForm registForm,
                                 Model model){
        model.addAttribute("title", "登録");
        registForm.setNew(true);
        return "date/regist";
    }

    /**
     * 新規登録
     * @param registForm RegistDateFormオブジェクト
     * @param result バリデーション
     * @param model モデル
     * @return 表示ページ
     */
    @PostMapping("/insert")
    public String insert(@Validated @ModelAttribute("registForm")RegistDateForm registForm,
                         BindingResult result,
                         Model model){
        // バリデーションチェック
        if(result.hasErrors()){
            model.addAttribute("title", "登録");
            registForm.setNew(true);
            return "date/regist";
        }

        model.addAttribute("title", "新規登録");

        Formula formula = makeFormula(registForm, 0, 0);
        // 同じ日付IDが登録されないようにチェック
        if(service.getFormula(formula.getType().getDateId()) != null){
            throw new NotExecuteAnyMoreException("すでに登録された日付IDです");
        }
        service.insertData(formula, formula.getType());
        return "redirect:/datemanager";
    }

    /**
     * 更新用ページ
     * @param registForm RegistDateFormオブジェクト
     * @param dateId 日付ID
     * @param model モデル
     * @return 表示ページ
     */
    @GetMapping("/updateFormula/{dateId}")
    public String showUpdatePage(@ModelAttribute("registForm")RegistDateForm registForm,
                                 @PathVariable String dateId,
                                 Model model){

        model.addAttribute("title", "更新");
        // updateにどのテーブルを変更するか伝えるためid情報をmodelに格納
        Formula formula = service.getFormula(dateId);
        model.addAttribute("formulaId", formula.getId());
        model.addAttribute("typeId", formula.getTypeId());

        registForm = makeForm(formula);
        model.addAttribute("registForm", registForm);

        return "date/regist";
    }

    /**
     * 更新
     * @param registForm RegistDateFormオブジェクト
     * @param formulaId formulaテーブルのID
     * @param typeId typeテーブルのID
     * @param result バリデーション
     * @param model モデル
     * @return 表示ページ
     */
    @PostMapping("/update")
    public String update(@Validated @ModelAttribute("registForm")RegistDateForm registForm,
                         BindingResult result,
                         @RequestParam("formulaId") int formulaId,
                         @RequestParam("typeId") int typeId,
                         Model model){

        // バリデーションチェック
        if(result.hasErrors()){
            registForm.setNew(false);
            model.addAttribute("title", "更新");
            model.addAttribute("registForm", registForm);
            model.addAttribute("formulaId", formulaId);
            model.addAttribute("typeId", typeId);
            return "date/regist";
        }

        Formula formula = makeFormula(registForm, formulaId, typeId);
        // 重複チェック
        if(service.getFormula(formula.getType().getDateId()) != null){
            throw new NotExecuteAnyMoreException("すでに登録された日付IDです");
        }
        service.updateData(formula);

        return "redirect:/datemanager";
    }

    /**
     * データを削除
     * @param dateId 日付ID
     * @param model モデル
     * @return 表示ページ
     */
    @PostMapping("/delete")
    public String delete(
            @ModelAttribute("inputForm") DateInputForm inputForm,
            @RequestParam("dateId") String dateId,
            @RequestParam("userInputDate") String userInputDate,
            @RequestParam("userInputDateId") String userInputDateId,
            Model model,
            RedirectAttributes redirectAttributes){
        // データを削除
        Formula formula = service.getFormula(dateId);
        service.deleteData(formula);

        // inputFormにユーザーの入力した情報を詰める
        inputForm.setDate(userInputDate);
        inputForm.setDateId(userInputDateId);

        // 連続でデータを削除する場合、ユーザーの入力した情報がないとinputFormにnullが入ってしまう
        model.addAttribute("userInputDate", userInputDate);
        model.addAttribute("userInputDateId", userInputDateId);

        List<Formula> formulas = service.getAll();
        Map<String, String> map = getDateIdAndDateType(formulas);

        model.addAttribute("map", map);

        // 削除後の結果をもう一度取得
        List<Formula> results = getResults(inputForm, formulas);

        model.addAttribute("results", results);
        return "date/index";
    }

    /**
     * DBに登録されている計算式全てに対して計算を行います
     * @param formulas 計算式一覧
     * @param inputDate 入力された日付
     * @param f 日付フォーマット
     * @return 計算式一覧(計算後)
     */
    private List<Formula> calcAllDate(List<Formula> formulas,
                                      LocalDate inputDate,
                                      DateTimeFormatter f) {

        for (Formula formula : formulas) {
            calcDate(formula, inputDate, f);
        }
        return formulas;
    }

    /**
     * 計算式を元に日付を計算します
     * @param formula Formulaオブジェクト
     * @param inputDate 入力された日付
     * @param f 日付フォーマット
     */
    private void calcDate(Formula formula,
                          LocalDate inputDate,
                          DateTimeFormatter f){
        LocalDate newDate = inputDate;
        // 計算式を元に年月日の加減を行う
        if (formula.getYear() != 0) {
            newDate = newDate.plusYears(formula.getYear());
        }
        if (formula.getMonth() != 0) {
            newDate = newDate.plusMonths(formula.getMonth());
        }
        if (formula.getDay() != 0) {
            newDate = newDate.plusDays(formula.getDay());
        }
        formula.setResultDate(newDate.format(f));
    }


    /**
     * 日付IDと日付名をformulaから取得します
     * @param formulas 計算式一覧
     * @return 日付ID,日付名
     */
    private Map<String, String> getDateIdAndDateType(List<Formula> formulas) {
        Map<String, String> formulaMap = new HashMap<>();

        for (Formula formula : formulas){
            String dateId = formula.getType().getDateId();
            String dateType = formula.getType().getDateType();
            formulaMap.put(dateId, dateType);
        }

        return formulaMap;
    }

    /**
     * RegistDateFormオブジェクトからFormulaオブジェクトを作成する
     * @param registForm RegistDateFormオブジェクト
     * @return Formulaオブジェクト
     */
    private Formula makeFormula(RegistDateForm registForm, int formulaId, int typeId){
        Formula formula = new Formula();
        Type type = new Type();

        if(formulaId != 0) {
            formula.setId(formulaId);
        }

        if(typeId != 0){
            type.setId(typeId);
        }

        type.setDateId(registForm.getDateId());
        type.setDateType(registForm.getDateType());
        formula.setType(type);
        formula.setYear(registForm.getYear());
        formula.setMonth(registForm.getMonth());
        formula.setDay(registForm.getDay());

        return formula;
    }

    /**
     * FormulaオブジェクトからRegistDateFormオブジェクトを作成する
     * @param formula Formulaオブジェクト
     * @return RegistDateFormオブジェクト
     */
    private RegistDateForm makeForm(Formula formula){
        RegistDateForm registForm = new RegistDateForm();
        registForm.setDateId(formula.getType().getDateId());
        registForm.setDateType(formula.getType().getDateType());
        registForm.setYear(formula.getYear());
        registForm.setMonth(formula.getMonth());
        registForm.setDay(formula.getDay());
        registForm.setNew(false);

        return registForm;
    }

    /**
     * 計算結果を返すメソッド
     * @param inputForm DateInputFormオブジェクト
     * @param formulas Formulaのリスト
     * @return Formulaのリスト
     */
    private List<Formula> getResults(DateInputForm inputForm, List<Formula> formulas){
        // 結果オブジェクト
        List<Formula> results = new ArrayList<>();

        try {
            // 使用するフォーマットを指定
            DateTimeFormatter format = DateTimeFormatter.ofPattern("uuuuMMdd").withResolverStyle(ResolverStyle.STRICT);

            // 入力された日付をLocalDateに型変換
            LocalDate inputDate = LocalDate.parse(inputForm.getDate(), format);

            // 日付IDが選択されているか判定
            if (inputForm.getDateId() == "") {
                results = calcAllDate(formulas, inputDate, format);
            } else {
                Formula formula = service.getFormula(inputForm.getDateId());
                // 空のリストを返す
                // 日付IDを指定して検索した後に結果を削除するとここでnullが返ってきてしまうためここでチェック
                if(formula == null){
                    return results;
                }
                calcDate(formula, inputDate, format);
                results.add(formula);
            }

            return results;

        } catch (DateTimeException e){
            throw new NotExecuteAnyMoreException("指定された日付は存在しません");
        }
    }
}
