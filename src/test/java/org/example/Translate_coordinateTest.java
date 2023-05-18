package org.example;

import org.junit.Test;
import org.junit.Assert;

public class Translate_coordinateTest {
    @Test
    public void probatranslate(){

        Assert.assertTrue(Translate_coordinate.translate("AA23"));

        Assert.assertTrue(Translate_coordinate.translate("AsA23"));

        Assert.assertTrue(Translate_coordinate.translate("A5A23"));
    }
}