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
        Num_coordinate num_coordinate = new Num_coordinate();
        num_coordinate = Translate_coordinate.translate_coordinate_to_int("ACA23");

        Assert.assertEquals(811, (int) num_coordinate.num_column);

        num_coordinate = Translate_coordinate.translate_coordinate_to_int("AA23");

        Assert.assertEquals(28, (int) num_coordinate.num_column);
    }
}