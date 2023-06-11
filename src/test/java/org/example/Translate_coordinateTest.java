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
        NumCoordinate numCoordinate;
        numCoordinate = Translate_coordinate.translate_coordinate_to_int("A23");
        Assert.assertEquals(1, (int) numCoordinate.getNumColum());

        numCoordinate = Translate_coordinate.translate_coordinate_to_int("AB23");
        Assert.assertEquals(28, (int) numCoordinate.getNumColum());

        numCoordinate = Translate_coordinate.translate_coordinate_to_int("XN23");
        Assert.assertEquals(638, (int) numCoordinate.getNumColum());

        numCoordinate = Translate_coordinate.translate_coordinate_to_int("AAA23");
        Assert.assertEquals(703, (int) numCoordinate.getNumColum());

        numCoordinate = Translate_coordinate.translate_coordinate_to_int("ZZZ23");
        Assert.assertEquals(18278, (int) numCoordinate.getNumColum());

        numCoordinate = Translate_coordinate.translate_coordinate_to_int("AAAA23");
        Assert.assertEquals(18279, (int) numCoordinate.getNumColum());

        numCoordinate = Translate_coordinate.translate_coordinate_to_int("XFD123");
        Assert.assertEquals(16384, (int) numCoordinate.getNumColum());

        numCoordinate = Translate_coordinate.translate_coordinate_to_int("UYT123");
        Assert.assertEquals(14866, (int) numCoordinate.getNumColum());
    }

    @Test
    public void test_parse1() {
        Formula.tokenize("+ 1 - 2 +5");
        Assert.assertFalse(Formula.is_parseable());//false - redo
    }

    @Test
    public void test_parse2() {
        Formula.tokenize("1 - 2 +5+");
        Assert.assertFalse(Formula.is_parseable());
    }

    @Test
    public void test_parse3() {
        Formula.tokenize("1 * 2 +5");//true
        Assert.assertTrue(Formula.is_parseable());
    }

    @Test
    public void test_parse4() {
        Formula.tokenize("1");//true
        Assert.assertTrue(Formula.is_parseable());
    }

    @Test
    public void test_parse5() {
        Formula.tokenize("+ 3");//false
        Assert.assertFalse(Formula.is_parseable());
    }

    @Test
    public void test_parse6() {
        Formula.tokenize("++ 3");//false
        Assert.assertFalse(Formula.is_parseable());
    }

    @Test
    public void test_parse7() {
        Formula.tokenize("+");//false
        Assert.assertFalse(Formula.is_parseable());
    }

    @Test
    public void test_parse8() {
        Formula.tokenize("(");//false
        Assert.assertFalse(Formula.is_parseable());
    }

    @Test
    public void test_parse9() {
        Formula.tokenize(")");//false
        Assert.assertFalse(Formula.is_parseable());
    }

    @Test
    public void test_parse10() {
        Formula.tokenize("(())");
        Assert.assertTrue(Formula.is_parseable());
    }

    @Test
    public void test_parse11() {
        Formula.tokenize("(3(");//false
        Assert.assertFalse(Formula.is_parseable());
    }

    @Test
    public void test_parse12() {
        Formula.tokenize("((3+6)(");//false
        Assert.assertFalse(Formula.is_parseable());
    }

    @Test
    public void test_parse13() {
        Formula.tokenize("((3+6)())");// what this should be?? Ilegal in our case, not supported in first approach
        Assert.assertFalse(Formula.is_parseable()); //for this case both checkers should work together??
    }

    @Test
    public void test_parse14() {
        Formula.tokenize("((3+6)*(2))");// what this should be??
        Assert.assertTrue(Formula.is_parseable());
    }

    @Test
    public void test_parse15() {
        Formula.tokenize("A1");// what this should be??
        Assert.assertTrue(Formula.is_parseable());
    }
}