package org.example;

import org.junit.Test;
import org.junit.Assert;

public class Translate_coordinateTest {
    @Test
    public void probatranslate(){
        Assert.assertTrue(Translate_coordinate.is_correct_coordinate("ACA23"));

        Assert.assertFalse(Translate_coordinate.is_correct_coordinate("AsA23"));

        Assert.assertFalse(Translate_coordinate.is_correct_coordinate("A5A23"));
    }

    @Test
    public void test_translate_coordinate_to_int(){
        Num_coordinate num_coordinate;
        num_coordinate = Translate_coordinate.translate_coordinate_to_int("A23");
        Assert.assertEquals(1, (int) num_coordinate.num_column);

        num_coordinate = Translate_coordinate.translate_coordinate_to_int("AB23");
        Assert.assertEquals(28, (int) num_coordinate.num_column);

        num_coordinate = Translate_coordinate.translate_coordinate_to_int("XN23");
        Assert.assertEquals(638, (int) num_coordinate.num_column);

        num_coordinate = Translate_coordinate.translate_coordinate_to_int("AAA23");
        Assert.assertEquals(703, (int) num_coordinate.num_column);

        num_coordinate = Translate_coordinate.translate_coordinate_to_int("ZZZ23");
        Assert.assertEquals(18278, (int) num_coordinate.num_column);

        num_coordinate = Translate_coordinate.translate_coordinate_to_int("AAAA23");
        Assert.assertEquals(18279, (int) num_coordinate.num_column);

        num_coordinate = Translate_coordinate.translate_coordinate_to_int("XFD123");
        Assert.assertEquals(16384, (int) num_coordinate.num_column);

        num_coordinate = Translate_coordinate.translate_coordinate_to_int("UYT123");
        Assert.assertEquals(14866, (int) num_coordinate.num_column);
    }

    @Test
    public void test_parse1() {
        Formula.setFormula_body("+ 1 - 2 +5");
        Formula.tokenize();
        Assert.assertFalse(Formula.is_parseable());//false - redo
    }

    @Test
    public void test_parse2() {
        Formula.setFormula_body("1 - 2 +5+");//false - redo
        Formula.tokenize();
        Assert.assertFalse(Formula.is_parseable());
    }

    @Test
    public void test_parse3() {
        Formula.setFormula_body("1 * 2 +5");//true
        Formula.tokenize();
        Assert.assertTrue(Formula.is_parseable());
    }

    @Test
    public void test_parse4() {
        Formula.setFormula_body("1");//true
        Formula.tokenize();
        Assert.assertTrue(Formula.is_parseable());
    }

    @Test
    public void test_parse5() {
        Formula.setFormula_body("+ 3");//false
        Formula.tokenize();
        Assert.assertFalse(Formula.is_parseable());
    }

    @Test
    public void test_parse6() {
        Formula.setFormula_body("++ 3");//false
        Formula.tokenize();
        Assert.assertFalse(Formula.is_parseable());
    }

    @Test
    public void test_parse7() {
        Formula.setFormula_body("+");//false
        Formula.tokenize();
        Assert.assertFalse(Formula.is_parseable());
    }

    @Test
    public void test_parse8() {
        Formula.setFormula_body("(");//false
        Formula.tokenize();
        Assert.assertFalse(Formula.is_parseable());
    }

    @Test
    public void test_parse9() {
        Formula.setFormula_body(")");//false
        Formula.tokenize();
        Assert.assertFalse(Formula.is_parseable());
    }

    @Test
    public void test_parse10() {
        Formula.setFormula_body("(())");
        Formula.tokenize();
        Assert.assertTrue(Formula.is_parseable());
    }

    @Test
    public void test_parse11() {
        Formula.setFormula_body("(3(");//false
        Formula.tokenize();
        Assert.assertFalse(Formula.is_parseable());
    }

    @Test
    public void test_parse12() {
        Formula.setFormula_body("((3+6)(");//false
        Formula.tokenize();
        Assert.assertFalse(Formula.is_parseable());
    }

    @Test
    public void test_parse13() {
        Formula.setFormula_body("((3+6)())");// what this should be?? Ilegal in our case, not supported in first approach
        Formula.tokenize();
        Assert.assertFalse(Formula.is_parseable()); //for this case both checkers should work together??
    }

    @Test
    public void test_parse14() {
        Formula.setFormula_body("((3+6)*(2))");// what this should be??
        Formula.tokenize();
        Assert.assertTrue(Formula.is_parseable());
    }
}