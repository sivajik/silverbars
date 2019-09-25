package com.silverbars.dao;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ OrderServiceImplTestFeature001.class, OrderServiceImplTestFeature002.class,
		OrderServiceImplTestFeature003.class })
public class AllTests {

}