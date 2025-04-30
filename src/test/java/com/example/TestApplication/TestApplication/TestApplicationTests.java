package com.example.TestApplication.TestApplication;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@Slf4j
//@SpringBootTest after removing this there is no icon of the springBoot of the context load //
class TestApplicationTests {

//	@Test
//	void contextLoads() {
//	}

//	@AfterEach // har ek method ke bad ye line print Hogi //
//	void tearDown(){
//		log.info(" Tearing down the method");
//	}


//	@BeforeAll  // sare test  case se Pehle ye line print Hogi //
//	static void setUpOnce(){
//		log.info(" Set Up Once");
//	}

//
//	@AfterAll // Sare  Test cases ke bad ye line print Hogi //
//	static void tearDownOnce(){
//		log.info(" tearing Down Once");
//	}

	@Test
	void testWithOne(){
		log.info(" Test One is Run ");
	}
	@Test
	void checkAssertMethod(){
		int a=10;
		int b=5;
		int result= sum(a,b);
//		Assertions.assertEquals(15,result);

//		assertThat(result)
//				.isEqualTo(15)
//				.isCloseTo(14, Offset.offset(1));


		assertThat("Apple")
				.isEqualTo("Apple")
				.startsWith("App")
				.endsWith("le")
				.hasSize(5);

	}

	int sum(int a , int b){
		 return a+b;
	}
	@Test
	void TestNumberTwo(){
		log.info("test Two is run");
	}
	@Test
	void testDivideTwoNumber_whenDenominatorIsZero(){
		int a = 13;
		int b = 0;
		assertThatThrownBy(() -> divideTwoNumber(a,b))
				.isInstanceOf(ArithmeticException.class)
				.hasMessage("try to be divide by zero");


	}


	double divideTwoNumber(int a,int b) throws Exception {
		try{
			return a/b;
		}
		catch( ArithmeticException e){
			log.error("ArithmeticException"+ e.getLocalizedMessage());
//			 throw new ArithmeticException(e.getLocalizedMessage());
			throw new ArithmeticException("try to be divide by zero");
		}
	}
}
