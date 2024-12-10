package it.cnr.istc.pst.platinum.stats.mongo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;



import it.cnr.istc.pst.platinum.stats.ModelDataset;
import it.cnr.istc.pst.platinum.stats.TokenExecutionData;

/**
 * 
 * @author alessandroumbrico
 *
 */
public class MongoModelDatasetTest 
{
	/**
	 * 
	 */
	@Test
	public void setupTest() 
	{
		try
		{
			// create a data-set
			ModelDataset dataset = new MongoModelDataset("platinum");
			assertNotNull(dataset);
		}
		catch (Exception ex) {
			fail(ex.getMessage());
		}
	}
	
	
	/**
	 * 
	 */
	@Test
	public void saveDataTest() 
	{
		try
		{
			// create a data-set
			ModelDataset dataset = new MongoModelDataset("platinum");
			assertNotNull(dataset);
			
			// select data-set
			dataset.select("test_model");
			
			dataset.save(new TokenExecutionData(
					System.currentTimeMillis(), 
					"token_test_00", 
					12l, 
					23l, 
					"comp_test_00"));
			
			// check database data
			List<TokenExecutionData> list = dataset.getTokenExecutionData();
			assertTrue(list.size() == 1);
			System.out.println("One document inserted");
			
			dataset.save(new TokenExecutionData(
					System.currentTimeMillis(), 
					"token_test_00", 
					7l, 
					56l, 
					"comp_test_00"));
			
			// check database data
			list = dataset.getTokenExecutionData();
			assertTrue(list.size() == 2);
			System.out.println("Two documents inserted");
			
			dataset.save(new TokenExecutionData(
					System.currentTimeMillis(), 
					"token_test_00", 
					5l, 
					11l, 
					"comp_test_00"));
			
			// check database data
			list = dataset.getTokenExecutionData();
			assertTrue(list.size() == 3);
			System.out.println("Three documents inserted");
			
			// clear all the data set
			dataset.clear();
			System.out.println("Dataset cleared");
		}
		catch (Exception ex) {
			fail(ex.getMessage());
		}
	}
	
	
	/**
	 * 
	 */
	@Test
	public void saveAndGetDataTest() 
	{
		try
		{
			// create a data-set
			ModelDataset dataset = new MongoModelDataset("platinum");
			assertNotNull(dataset);
			
			// select data-set
			dataset.select("test_model_01");
			// clear all the data set
			dataset.clear();
			// select data-set
			dataset.select("test_model_02");
			dataset.clear();
			System.out.println("Datasets cleared");
			
			// select data-set
			dataset.select("test_model_01");
			
			dataset.save(new TokenExecutionData(
					System.currentTimeMillis(), 
					"token_test_00", 
					12l, 
					23l, 
					"comp_test_00"));
			
			// check database data
			List<TokenExecutionData> list1 = dataset.getTokenExecutionData();
			assertTrue(list1.size() == 1);
			System.out.println("One document inserted");
			
			dataset.save(new TokenExecutionData(
					System.currentTimeMillis(), 
					"token_test_00", 
					7l, 
					56l, 
					"comp_test_00"));
			
			// check database data
			list1 = dataset.getTokenExecutionData();
			assertTrue(list1.size() == 2);
			System.out.println("Two documents inserted");
			
			dataset.save(new TokenExecutionData(
					System.currentTimeMillis(), 
					"token_test_00", 
					5l, 
					11l, 
					"comp_test_00"));
			
			// check database data
			list1 = dataset.getTokenExecutionData();
			assertTrue(list1.size() == 3);
			System.out.println("Three documents inserted");
			
			// change data-set
			dataset.select("test_model_02");
			
			
			dataset.save(new TokenExecutionData(
					System.currentTimeMillis(), 
					"token_test_01", 
					12l, 
					23l, 
					"comp_test_00"));
			
			// check database data
			List<TokenExecutionData> list2 = dataset.getTokenExecutionData();
			assertTrue(list2.size() == 1);
			System.out.println("One document inserted");
			
			dataset.save(new TokenExecutionData(
					System.currentTimeMillis(), 
					"token_test_00", 
					7l, 
					56l, 
					"comp_test_00"));
			
			// check database data
			list2 = dataset.getTokenExecutionData();
			assertTrue(list2.size() == 2);
			System.out.println("Two documents inserted");
			
			assertTrue((list1.size() - list2.size()) == 1);
		}
		catch (Exception ex) {
			fail(ex.getMessage());
		}
	}
	
	
	
	/**
	 * 
	 */
	@Test
	public void filterTest() 
	{
		try
		{
			// create a data-set
			ModelDataset dataset = new MongoModelDataset("platinum");
			assertNotNull(dataset);
			
			// select data-set
			dataset.select("test_model_01");
			// clear all the data set
			dataset.clear();
			System.out.println("Datasets cleared");
			
			
			// select data-set
			dataset.select("test_model_01");
			
			dataset.save(new TokenExecutionData(
					System.currentTimeMillis(), 
					"token_test_00", 
					12l, 
					23l, 
					"comp_test_00"));
			
			// check database data
			List<TokenExecutionData> list = dataset.getTokenExecutionData();
			assertTrue(list.size() == 1);
			System.out.println("One document inserted");
			
			dataset.save(new TokenExecutionData(
					System.currentTimeMillis(), 
					"token_test_00", 
					7l, 
					56l, 
					"comp_test_01"));
			
			// check database data
			list = dataset.getTokenExecutionData();
			assertTrue(list.size() == 2);
			System.out.println("Two documents inserted");
			
			dataset.save(new TokenExecutionData(
					System.currentTimeMillis(), 
					"token_test_00", 
					5l, 
					11l, 
					"comp_test_00"));
			
			// check database data
			list = dataset.getTokenExecutionData();
			assertTrue(list.size() == 3);
			System.out.println("Three documents inserted");
			
			
			
			dataset.save(new TokenExecutionData(
					System.currentTimeMillis(), 
					"token_test_01", 
					12l, 
					23l, 
					"comp_test_00"));
			
			// check database data
			list = dataset.getTokenExecutionData();
			assertTrue(list.size() == 4);
			System.out.println("Four document inserted");
			
			dataset.save(new TokenExecutionData(
					System.currentTimeMillis(), 
					"token_test_00", 
					7l, 
					56l, 
					"comp_test_00"));
			
			// check database data
			list = dataset.getTokenExecutionData();
			assertTrue(list.size() == 5);
			System.out.println("Five documents inserted");
			
			
			// retrieve data concerning component "comp_test_00"
			List<TokenExecutionData> lComp00 = dataset.getTokenExecutionDataByComponent("comp_test_00");
			
			List<TokenExecutionData> lComp01 = dataset.getTokenExecutionDataByComponent("comp_test_01");
			
			assertTrue(lComp00.size() == 4);
			assertTrue(lComp01.size() == 1);
			
		}
		catch (Exception ex) {
			fail(ex.getMessage());
		}
	}

}
