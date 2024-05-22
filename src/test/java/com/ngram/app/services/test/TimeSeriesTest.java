package com.ngram.app.services.test;

import com.ngram.app.services.TimeSeries;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/** Unit Tests for the TimeSeries class.
 *  @author s-bose7
 */

public class TimeSeriesTest {
	
	@Test
    public void testConstructor() {
		TimeSeries timeSeries = new TimeSeries();
		timeSeries.put(1992, 26.09);
		timeSeries.put(1994, 26.07);
		timeSeries.put(1995, 880.6);
		timeSeries.put(2010, 4.034);
		
		TimeSeries newTimeSeries = new TimeSeries(timeSeries, 1993, 2015);
		List<Integer> expectedYears = new ArrayList<Integer>(Arrays.asList(1994, 1995, 2010));
		assertThat(newTimeSeries.years()).isEqualTo(expectedYears);
    }	
	
    @Test
    public void testPlusMethod() {
        TimeSeries catPopulation = new TimeSeries();
        catPopulation.put(1991, 0.0);
        catPopulation.put(1992, 100.0);
        catPopulation.put(1994, 200.0);

        TimeSeries dogPopulation = new TimeSeries();
        dogPopulation.put(1994, 400.0);
        dogPopulation.put(1995, 500.0);

        TimeSeries totalPopulation = catPopulation.plus(dogPopulation);
        // expected: 1991: 0,
        //           1992: 100
        //           1994: 600
        //           1995: 500

        List<Integer> expectedYears = new ArrayList<>
                (Arrays.asList(1991, 1992, 1994, 1995));

        assertThat(totalPopulation.years()).isEqualTo(expectedYears);

        List<Double> expectedTotal = new ArrayList<>
                (Arrays.asList(0.0, 100.0, 600.0, 500.0));

        for (int i = 0; i < expectedTotal.size(); i += 1) {
            assertThat(totalPopulation.data().get(i)).isWithin(1E-10).of(expectedTotal.get(i));
        }
    }

    @Test
    public void testEmptyBasic() {
        TimeSeries catPopulation = new TimeSeries();
        TimeSeries dogPopulation = new TimeSeries();

        assertThat(catPopulation.years()).isEmpty();
        assertThat(catPopulation.data()).isEmpty();

        TimeSeries totalPopulation = catPopulation.plus(dogPopulation);

        assertThat(totalPopulation.years()).isEmpty();
        assertThat(totalPopulation.data()).isEmpty();
    }
    
    @Test
    public void testDivideByMethod() {
    	
    	TimeSeries tSeriesOne = new TimeSeries();
    	double randomWeightedPopularity = 2.34;
    	
    	for(int year=2000; year<2007; year++) {
    		tSeriesOne.put(year, randomWeightedPopularity);
    		randomWeightedPopularity *= 1.354;
    	}
    	
    	TimeSeries tSeriesTwo = new TimeSeries();
    	tSeriesTwo.put(1900, 2.458);
    	assertThrows(IllegalArgumentException.class, () -> tSeriesOne.dividedBy(tSeriesTwo));
    	tSeriesTwo.put(2000, 2.448);
    	tSeriesTwo.put(2001, 6.423);
    	tSeriesTwo.put(2002, 4.263);
    	tSeriesTwo.put(2003, 6.458);
    	tSeriesTwo.put(2004, 3.458);
    	tSeriesTwo.put(2005, 7.458);
    	tSeriesTwo.put(2006, 7.367);
    	assertEquals(7, tSeriesOne.dividedBy(tSeriesTwo).years().size());
    }
    
} 