package com.android.compose.mockk.part2

import org.junit.Assert
import org.junit.Test

class CalculatorTest {

    @Test
    fun `add Two Numbers and Should Return Correct Result`() {
        //Arrange
        val calculator = Calculator()
        //Act
        val result = calculator.add(a = 2, b = 2)
        //Assert
        Assert.assertEquals(4, result)
    }

    @Test
    fun `minus Two Numbers and Should Return Correct Result`() {
        //Arrange
        val calculator = Calculator()
        //Act
        val result = calculator.subtract(a = 2, b = 2)
        //Assert
        Assert.assertEquals(0, result)
    }

}
