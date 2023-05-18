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
}