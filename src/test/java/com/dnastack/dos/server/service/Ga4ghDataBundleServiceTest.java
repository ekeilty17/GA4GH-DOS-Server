package com.dnastack.dos.server.service;

import com.dnastack.dos.server.exception.EntityNotFoundException;
import com.dnastack.dos.server.model.DataBundle;
import com.dnastack.dos.server.model.Ga4ghDataBundle;
import com.dnastack.dos.server.repository.Ga4ghDataBundleRepository;

import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
public class Ga4ghDataBundleServiceTest {

	@Mock
	private Ga4ghDataBundleRepository mockRepository;

	@InjectMocks
	private Ga4ghDataBundleService mockService;

	@Before
	public void setUp() throws Exception {
		// Injecting mockRepository into mockService
		MockitoAnnotations.initMocks(this);
	}

	@After
	public void tearDown() {

	}

	@Test
	public void repositoryContexLoads() {
		
		assertNotNull(mockRepository);
		
	}

	@Test(expected = EntityNotFoundException.class)
	public void getObjectByIdAndVersionTest() throws EntityNotFoundException {

		Ga4ghDataBundle testObject = new Ga4ghDataBundle();
		testObject.setId("1");
		testObject.setVersion("1");

		// Mocking the Repository behavior
		when(mockRepository.findByIdAndVersion("1", "1")).thenReturn(testObject);
		when(mockRepository.findByIdAndVersion("1", "2")).thenReturn(null);

		// Valid result
		Ga4ghDataBundle result = mockService.getObjectByIdAndVersion("1", "1");
		assertThat("result", result, is(sameInstance(testObject)));

		// Verify the correct Repository method was called
		verify(mockRepository).findByIdAndVersion("1", "1");

		// Invalid result
		mockService.getObjectByIdAndVersion("1", "2");

	}

	@Test(expected = EntityNotFoundException.class)
	public void getObjectByIdWithMostRecentVersionTest() throws EntityNotFoundException {

		List<Ga4ghDataBundle> testList = new ArrayList<>();
		testList.add(new Ga4ghDataBundle(false, "1", null, null, null, "1", null, null, null, null, null));
		testList.add(new Ga4ghDataBundle(false, "1", null, null, null, "2", null, null, null, null, null));
		testList.add(new Ga4ghDataBundle(true, "1", null, null, null, "3", null, null, null, null, null));

		// Mocking the Repository behavior
		when(mockRepository.findByIdEquals("1")).thenReturn(testList);
		when(mockRepository.findByIdEquals("2")).thenReturn(new ArrayList<>());

		// Valid result: id == 1
		Ga4ghDataBundle result = mockService.getObjectByIdWithMostRecentVersion("1");
		assertThat("result", result, is(sameInstance(testList.get(2))));

		// Verify the correct Repository method was called
		verify(mockRepository).findByIdEquals("1");

		// Invalid result: id == 2
		mockService.getObjectByIdWithMostRecentVersion("2");

	}

	@Test(expected = EntityNotFoundException.class)
	public void getObjectByIdAndAllVersionsTest() throws EntityNotFoundException, Exception {

		List<Ga4ghDataBundle> testList = new ArrayList<>();
		testList.add(new Ga4ghDataBundle(false, "1", null, null, null, "1", null, null, null, null, null));
		testList.add(new Ga4ghDataBundle(false, "1", null, null, null, "2", null, null, null, null, null));
		testList.add(new Ga4ghDataBundle(true, "1", null, null, null, "3", null, null, null, null, null));
		testList.add(new Ga4ghDataBundle(true, "2", null, null, null, "1", null, null, null, null, null));
		testList.add(new Ga4ghDataBundle(false, "3", null, null, null, "1", null, null, null, null, null));
		testList.add(new Ga4ghDataBundle(true, "3", null, null, null, "2", null, null, null, null, null));

		Pageable pageable = new PageRequest(0, 10);

		// Mocking the Repository behavior
		when(mockRepository.findByIdEquals("1"))
				.thenReturn(Arrays.asList(testList.get(0), testList.get(1), testList.get(2)));
		when(mockRepository.findByIdEquals("2")).thenReturn(Arrays.asList(testList.get(3)));
		when(mockRepository.findByIdEquals("3")).thenReturn(Arrays.asList(testList.get(4), testList.get(5)));
		when(mockRepository.findByIdEquals("4")).thenReturn(new ArrayList<>());

		// Valid result: id == 1
		List<DataBundle> subList = new ArrayList<>();
		subList.add(new DataBundle(testList.get(0)));
		subList.add(new DataBundle(testList.get(1)));
		subList.add(new DataBundle(testList.get(2)));
		Page<DataBundle> expectedList = mockService.paginateList(subList, pageable);

		Page<DataBundle> resultList = mockService.getObjectByIdAndAllVersions("1", pageable);
		Assert.assertTrue(EqualsBuilder.reflectionEquals(expectedList, resultList));

		// Valid result: id == 2
		subList = new ArrayList<>();
		subList.add(new DataBundle(testList.get(3)));
		expectedList = mockService.paginateList(subList, pageable);

		resultList = mockService.getObjectByIdAndAllVersions("2", pageable);
		Assert.assertTrue(EqualsBuilder.reflectionEquals(expectedList, resultList));

		// Valid result: id == 3
		subList = new ArrayList<>();
		subList.add(new DataBundle(testList.get(4)));
		subList.add(new DataBundle(testList.get(5)));
		expectedList = mockService.paginateList(subList, pageable);

		resultList = mockService.getObjectByIdAndAllVersions("3", pageable);
		Assert.assertTrue(EqualsBuilder.reflectionEquals(expectedList, resultList));

		// Verify the correct Repository method was called
		verify(mockRepository).findByIdEquals("1");
		verify(mockRepository).findByIdEquals("2");
		verify(mockRepository).findByIdEquals("3");

		// Invalid result: id == 4
		mockService.getObjectByIdAndAllVersions("4", pageable);

	}

	@Test
	public void getAllObjectsWithMostRecentVersionsTest() throws Exception {

		List<Ga4ghDataBundle> testList = new ArrayList<>();
		testList.add(new Ga4ghDataBundle(false, "1", null, null, null, "1", null, null, null, null, null));
		testList.add(new Ga4ghDataBundle(false, "1", null, null, null, "2", null, null, null, null, null));
		testList.add(new Ga4ghDataBundle(true, "1", null, null, null, "3", null, null, null, null, null));
		testList.add(new Ga4ghDataBundle(true, "2", null, null, null, "1", null, null, null, null, null));
		testList.add(new Ga4ghDataBundle(false, "3", null, null, null, "1", null, null, null, null, null));
		testList.add(new Ga4ghDataBundle(true, "3", null, null, null, "2", null, null, null, null, null));

		Pageable pageable = new PageRequest(0, 10);

		// Mocking the Repository behavior
		when(mockRepository.findAll()).thenReturn(testList);

		// Valid result
		List<DataBundle> subList = new ArrayList<>();
		subList.add(new DataBundle(testList.get(2)));
		subList.add(new DataBundle(testList.get(3)));
		subList.add(new DataBundle(testList.get(5)));
		Page<DataBundle> expectedList = mockService.paginateList(subList, pageable);

		Page<DataBundle> resultList = mockService.getAllObjectsWithMostRecentVersions(pageable);
		Assert.assertTrue(EqualsBuilder.reflectionEquals(expectedList, resultList));

		// Verify the correct Repository method was called
		verify(mockRepository).findAll();

	}

	@Test
	public void getAllObjectsAndAllVersionsTest() throws Exception {

		List<Ga4ghDataBundle> testList = new ArrayList<>();
		testList.add(new Ga4ghDataBundle(false, "1", null, null, null, "1", null, null, null, null, null));
		testList.add(new Ga4ghDataBundle(false, "1", null, null, null, "2", null, null, null, null, null));
		testList.add(new Ga4ghDataBundle(true, "1", null, null, null, "3", null, null, null, null, null));
		testList.add(new Ga4ghDataBundle(true, "2", null, null, null, "1", null, null, null, null, null));
		testList.add(new Ga4ghDataBundle(false, "3", null, null, null, "1", null, null, null, null, null));
		testList.add(new Ga4ghDataBundle(true, "3", null, null, null, "2", null, null, null, null, null));

		Pageable pageable = new PageRequest(0, 10);

		// Mocking the Repository behavior
		when(mockRepository.findAll()).thenReturn(testList);

		// Valid result
		List<DataBundle> subList = new ArrayList<>();
		testList.forEach(o -> subList.add(new DataBundle(o)));
		Page<DataBundle> expectedList = mockService.paginateList(subList, pageable);

		Page<DataBundle> resultList = mockService.getAllObjectsAndAllVersions(pageable);
		Assert.assertTrue(EqualsBuilder.reflectionEquals(expectedList, resultList));

		// Verify the correct Repository method was called
		verify(mockRepository).findAll();

	}

	@Test
	public void getObjectsByAliasTest() throws Exception {

		List<Ga4ghDataBundle> testList = new ArrayList<>();
		testList.add(new Ga4ghDataBundle(false, "1", null, null, null, "1", null, null,
				Arrays.asList("alias 1", "alias 2"), null, null));
		testList.add(new Ga4ghDataBundle(false, "1", null, null, null, "2", null, null,
				Arrays.asList("alias 10", "alias 20"), null, null));
		testList.add(new Ga4ghDataBundle(true, "1", null, null, null, "3", null, null,
				Arrays.asList("alias 10", "alias 20"), null, null));
		testList.add(new Ga4ghDataBundle(true, "2", null, null, null, "1", null, null,
				Arrays.asList("alias 1", "alias 3"), null, null));
		testList.add(new Ga4ghDataBundle(false, "3", null, null, null, "1", null, null,
				Arrays.asList("alias 1", "alias 4"), null, null));
		testList.add(new Ga4ghDataBundle(true, "3", null, null, null, "2", null, null,
				Arrays.asList("alias 1", "alias 4"), null, null));

		Pageable pageable = new PageRequest(0, 10);

		// Mocking the Repository behavior
		when(mockRepository.findAll()).thenReturn(testList);

		// Valid result: alias = "alias 1"
		List<DataBundle> subList = new ArrayList<>();
		subList.add(new DataBundle(testList.get(0)));
		subList.add(new DataBundle(testList.get(3)));
		subList.add(new DataBundle(testList.get(4)));
		subList.add(new DataBundle(testList.get(5)));
		Page<DataBundle> expectedList = mockService.paginateList(subList, pageable);

		Page<DataBundle> resultList = mockService.getObjectsByAlias("alias 1", pageable);
		Assert.assertTrue(EqualsBuilder.reflectionEquals(expectedList, resultList));

		// Valid result: alias = "alias 2"
		subList = new ArrayList<>();
		subList.add(new DataBundle(testList.get(0)));
		expectedList = mockService.paginateList(subList, pageable);

		resultList = mockService.getObjectsByAlias("alias 2", pageable);
		Assert.assertTrue(EqualsBuilder.reflectionEquals(expectedList, resultList));

		// Valid result: alias = "alias 10"
		subList = new ArrayList<>();
		subList.add(new DataBundle(testList.get(1)));
		subList.add(new DataBundle(testList.get(2)));
		expectedList = mockService.paginateList(subList, pageable);

		resultList = mockService.getObjectsByAlias("alias 10", pageable);
		Assert.assertTrue(EqualsBuilder.reflectionEquals(expectedList, resultList));

		// Valid result: alias = "alias 20"
		subList = new ArrayList<>();
		subList.add(new DataBundle(testList.get(1)));
		subList.add(new DataBundle(testList.get(2)));
		expectedList = mockService.paginateList(subList, pageable);

		resultList = mockService.getObjectsByAlias("alias 20", pageable);
		Assert.assertTrue(EqualsBuilder.reflectionEquals(expectedList, resultList));

		// Valid result: alias = "alias 3"
		subList = new ArrayList<>();
		subList.add(new DataBundle(testList.get(3)));
		expectedList = mockService.paginateList(subList, pageable);

		resultList = mockService.getObjectsByAlias("alias 3", pageable);
		Assert.assertTrue(EqualsBuilder.reflectionEquals(expectedList, resultList));

		// Valid result: alias = "alias 4"
		subList = new ArrayList<>();
		subList.add(new DataBundle(testList.get(4)));
		subList.add(new DataBundle(testList.get(5)));
		expectedList = mockService.paginateList(subList, pageable);

		resultList = mockService.getObjectsByAlias("alias 4", pageable);
		Assert.assertTrue(EqualsBuilder.reflectionEquals(expectedList, resultList));

		// Invalid result: alias = "none"
		subList = new ArrayList<>();
		expectedList = mockService.paginateList(subList, pageable);

		resultList = mockService.getObjectsByAlias("none", pageable);
		Assert.assertTrue(EqualsBuilder.reflectionEquals(expectedList, resultList));

		// Verify the correct Repository method was called
		verify(mockRepository, times(7)).findAll();
	}

	@Test
	public void getObjectsByAliasWithMostRecentVersionTest() throws Exception {

		List<Ga4ghDataBundle> testList = new ArrayList<>();
		testList.add(new Ga4ghDataBundle(false, "1", null, null, null, "1", null, null,
				Arrays.asList("alias 1", "alias 2"), null, null));
		testList.add(new Ga4ghDataBundle(false, "1", null, null, null, "2", null, null,
				Arrays.asList("alias 10", "alias 20"), null, null));
		testList.add(new Ga4ghDataBundle(true, "1", null, null, null, "3", null, null,
				Arrays.asList("alias 10", "alias 20"), null, null));
		testList.add(new Ga4ghDataBundle(true, "2", null, null, null, "1", null, null,
				Arrays.asList("alias 1", "alias 3"), null, null));
		testList.add(new Ga4ghDataBundle(false, "3", null, null, null, "1", null, null,
				Arrays.asList("alias 1", "alias 4"), null, null));
		testList.add(new Ga4ghDataBundle(true, "3", null, null, null, "2", null, null,
				Arrays.asList("alias 1", "alias 4"), null, null));

		Pageable pageable = new PageRequest(0, 10);

		// Mocking the Repository behavior
		when(mockRepository.findAll()).thenReturn(testList);

		// Valid result: alias = "alias 1"
		List<DataBundle> subList = new ArrayList<>();
		subList.add(new DataBundle(testList.get(3)));
		subList.add(new DataBundle(testList.get(5)));
		Page<DataBundle> expectedList = mockService.paginateList(subList, pageable);

		Page<DataBundle> resultList = mockService.getObjectsByAliasWithMostRecentVersion("alias 1", pageable);
		Assert.assertTrue(EqualsBuilder.reflectionEquals(expectedList, resultList));

		// Valid result: alias = "alias 2"
		subList = new ArrayList<>();
		expectedList = mockService.paginateList(subList, pageable);

		resultList = mockService.getObjectsByAliasWithMostRecentVersion("alias 2", pageable);
		Assert.assertTrue(EqualsBuilder.reflectionEquals(expectedList, resultList));

		// Valid result: alias = "alias 10"
		subList = new ArrayList<>();
		subList.add(new DataBundle(testList.get(2)));
		expectedList = mockService.paginateList(subList, pageable);

		resultList = mockService.getObjectsByAliasWithMostRecentVersion("alias 10", pageable);
		Assert.assertTrue(EqualsBuilder.reflectionEquals(expectedList, resultList));

		// Valid result: alias = "alias 20"
		subList = new ArrayList<>();
		subList.add(new DataBundle(testList.get(2)));
		expectedList = mockService.paginateList(subList, pageable);

		resultList = mockService.getObjectsByAliasWithMostRecentVersion("alias 20", pageable);
		Assert.assertTrue(EqualsBuilder.reflectionEquals(expectedList, resultList));

		// Valid result: alias = "alias 3"
		subList = new ArrayList<>();
		subList.add(new DataBundle(testList.get(3)));
		expectedList = mockService.paginateList(subList, pageable);

		resultList = mockService.getObjectsByAliasWithMostRecentVersion("alias 3", pageable);
		Assert.assertTrue(EqualsBuilder.reflectionEquals(expectedList, resultList));

		// Valid result: alias = "alias 4"
		subList = new ArrayList<>();
		subList.add(new DataBundle(testList.get(5)));
		expectedList = mockService.paginateList(subList, pageable);

		resultList = mockService.getObjectsByAliasWithMostRecentVersion("alias 4", pageable);
		Assert.assertTrue(EqualsBuilder.reflectionEquals(expectedList, resultList));

		// Valid result: alias = "none"
		subList = new ArrayList<>();
		expectedList = mockService.paginateList(subList, pageable);

		resultList = mockService.getObjectsByAliasWithMostRecentVersion("none", pageable);
		Assert.assertTrue(EqualsBuilder.reflectionEquals(expectedList, resultList));

		// Verify the correct Repository method was called
		verify(mockRepository, times(7)).findAll();

	}

	@Test(expected = Exception.class)
	public void addObjectTest() throws Exception {

		// Mocking the Repository behavior
		when(mockRepository.findByIdEquals("1")).thenReturn(new ArrayList<>());
		when(mockRepository.findByIdEquals("2")).thenReturn(Arrays.asList(new Ga4ghDataBundle()));

		// Valid result
		Ga4ghDataBundle testObject = new Ga4ghDataBundle();
		testObject.setId("1");
		testObject.setVersion("1");
		mockService.addObject(testObject);

		// Verify the correct Repository method was called
		verify(mockRepository).findByIdEquals("1");
		verify(mockRepository).save(testObject);

		// Invalid result: object already in database
		testObject.setId("2");
		mockService.addObject(testObject);

	}

	@Test
	public void updateObjectTest_pass() throws EntityNotFoundException, Exception {

		List<Ga4ghDataBundle> testList = new ArrayList<>();
		testList.add(new Ga4ghDataBundle(false, "1", null, null, null, "1", null, null, null, null, null));
		testList.add(new Ga4ghDataBundle(false, "1", null, null, null, "2", null, null, null, null, null));
		testList.add(new Ga4ghDataBundle(true, "1", null, null, null, "3", null, null, null, null, null));
		testList.add(new Ga4ghDataBundle(true, "3", null, null, null, "1", null, null, null, null, null));

		// Mocking the Repository behavior
		when(mockRepository.findByIdEquals("1"))
				.thenReturn(Arrays.asList(testList.get(0), testList.get(1), testList.get(2)));
		when(mockRepository.findByIdEquals("3")).thenReturn(Arrays.asList(testList.get(3)));

		when(mockRepository.findByIdAndMostRecent("1", true)).thenReturn(testList.get(2));
		when(mockRepository.findByIdAndMostRecent("2", true)).thenReturn(null);
		when(mockRepository.findByIdAndMostRecent("3", true)).thenReturn(testList.get(3));

		// Valid result: updating content of a version
		Ga4ghDataBundle testObject = new Ga4ghDataBundle();
		testObject.setId("1");
		testObject.setVersion("3");
		testObject.setDescription("description");
		mockService.updateObject("1", testObject);
		/*
		 * .findByIdEquals("1") times called == 1 .findByIdAndMostRecent("1", true)
		 * times called == 1 .save(testObject) times called == 1
		 */

		// Valid result: updating to a new version
		testObject.setVersion("4");
		mockService.updateObject("1", testObject);
		/*
		 * .findByIdEquals("1") times called == 1 .findByIdAndMostRecent("1", true)
		 * times called == 1 .save(testObject) times called == 1
		 */

		// FIXME
		/*
		 * // Valid result: updating id of a version testObject.setId("2");
		 * mockService.updateObject("1", testObject);
		 */

		// Verify the correct Repository method was called
		verify(mockRepository, times(2)).findByIdEquals("1");
		verify(mockRepository, times(2)).findByIdAndMostRecent("1", true);
		verify(mockRepository, times(2)).save(testObject);
	}

	@Test(expected = EntityNotFoundException.class)
	public void updateObjectTest_fail_DNE() throws EntityNotFoundException, Exception {

		List<Ga4ghDataBundle> testList = new ArrayList<>();
		testList.add(new Ga4ghDataBundle(false, "1", null, null, null, "1", null, null, null, null, null));
		testList.add(new Ga4ghDataBundle(false, "1", null, null, null, "2", null, null, null, null, null));
		testList.add(new Ga4ghDataBundle(true, "1", null, null, null, "3", null, null, null, null, null));
		testList.add(new Ga4ghDataBundle(true, "3", null, null, null, "1", null, null, null, null, null));

		// Mocking the Repository behavior
		when(mockRepository.findByIdEquals("2")).thenReturn(new ArrayList<>());

		// Invalid result: object with id == 2 does not exist in database
		Ga4ghDataBundle testObject = new Ga4ghDataBundle();
		mockService.updateObject("2", testObject);

	}

	@Test(expected = Exception.class)
	public void updateObjectTest_fail_conflict() throws Exception {

		List<Ga4ghDataBundle> testList = new ArrayList<>();
		testList.add(new Ga4ghDataBundle(false, "1", null, null, null, "1", null, null, null, null, null));
		testList.add(new Ga4ghDataBundle(false, "1", null, null, null, "2", null, null, null, null, null));
		testList.add(new Ga4ghDataBundle(true, "1", null, null, null, "3", null, null, null, null, null));
		testList.add(new Ga4ghDataBundle(true, "3", null, null, null, "1", null, null, null, null, null));

		// Mocking the Repository behavior
		when(mockRepository.findByIdAndMostRecent("3", true)).thenReturn(testList.get(3));

		// Invalid result: changing all objects with id == 1 to id == 3 would cause
		// conflict with other objects in database
		Ga4ghDataBundle testObject = new Ga4ghDataBundle();
		testObject.setId("3");
		testObject.setVersion("3");
		mockService.updateObject("1", testObject);

	}

	@Test
	public void deleteAllObjectsTest() {

		mockService.deleteAllObjects();
		verify(mockRepository).deleteAll();

	}

	@Test(expected = Exception.class)
	public void deleteObjectTest() throws EntityNotFoundException {

		List<Ga4ghDataBundle> testList = new ArrayList<>();
		testList.add(new Ga4ghDataBundle(false, "1", null, null, null, "1", null, null, null, null, null));
		testList.add(new Ga4ghDataBundle(false, "1", null, null, null, "2", null, null, null, null, null));
		testList.add(new Ga4ghDataBundle(true, "1", null, null, null, "3", null, null, null, null, null));

		// Mocking the Repository behavior
		when(mockRepository.findByIdEquals("1"))
				.thenReturn(Arrays.asList(testList.get(0), testList.get(1), testList.get(2)));
		when(mockRepository.findByIdEquals("2")).thenReturn(new ArrayList<>());

		// Valid result
		mockService.deleteObject("1");

		verify(mockRepository).findByIdEquals("1");
		verify(mockRepository).delete("1v1");
		verify(mockRepository).delete("1v2");
		verify(mockRepository).delete("1v3");

		// Invalid result
		mockService.deleteObject("2");
	}

}
