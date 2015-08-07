import org.junit.Assert;
import org.junit.Test;
import org.junit.Before; 
import org.junit.After;

import java.io.File;
import java.lang.reflect.Method;

/** 
* CrossValidation Tester. 
* 
* @author <Authors name> 
* @since <pre>Jul 9, 2015</pre> 
* @version 1.0 
*/ 
public class CrossValidationTest {
    GUIAntMinerJFrame frame;
    CrossValidation cv;

@Before
public void before() throws Exception {
    frame = new GUIAntMinerJFrame();
    frame.setVisible(true);
    cv = new CrossValidation(null);
}


@After
public void after() throws Exception { 
}

/** 
* 
* Method: initializePheromoneTrails() 
* 
*/ 
@Test
public void testInitializePheromoneTrails() throws Exception { 
//TODO: Test goes here... 
/* 
try { 
   Method method = CrossValidation.getClass().getMethod("initializePheromoneTrails"); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/ 
} 

/** 
* 
* Method: calculateFreqTij() 
* 
*/ 
@Test
public void testCalculateFreqTij() throws Exception { 
//TODO: Test goes here... 
/* 
try { 
   Method method = CrossValidation.getClass().getMethod("calculateFreqTij"); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/ 
} 

/** 
* 
* Method: calculateInfoTij() 
* 
*/ 
@Test
public void testCalculateInfoTij() throws Exception { 
//TODO: Test goes here... 
/* 
try { 
   Method method = CrossValidation.getClass().getMethod("calculateInfoTij"); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/ 
} 

/** 
* 
* Method: calculateHeuristicFunction(Ant ant) 
* 
*/ 
@Test
public void testCalculateHeuristicFunction() throws Exception { 
//TODO: Test goes here... 
/* 
try { 
   Method method = CrossValidation.getClass().getMethod("calculateHeuristicFunction", Ant.class); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/ 
} 

/** 
* 
* Method: calculateProbabilities(Ant ant, double [][] pheromoneArray) 
* 
*/ 
@Test
public void testCalculateProbabilities() throws Exception { 
//TODO: Test goes here... 
/* 
try { 
   Method method = CrossValidation.getClass().getMethod("calculateProbabilities", Ant.class, double.class); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/ 
} 

/** 
* 
* Method: ruleConstructor(Ant ant, int i, int j) 
* 
*/ 
@Test
public void testRuleConstructor() throws Exception { 
//TODO: Test goes here... 
/* 
try { 
   Method method = CrossValidation.getClass().getMethod("ruleConstructor", Ant.class, int.class, int.class); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/ 
} 

/** 
* 
* Method: determineRuleConsequent(Ant ant) 
* 
*/ 
@Test
public void testDetermineRuleConsequent() throws Exception { 
//TODO: Test goes here... 
/* 
try { 
   Method method = CrossValidation.getClass().getMethod("determineRuleConsequent", Ant.class); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/ 
} 

/** 
* 
* Method: calculateRuleQuality(Ant ant, int nParam) 
* 
*/ 
@Test
public void testCalculateRuleQuality() throws Exception { 
    // CrossValidation cv = frame.getThread();
    //cv.calculateRuleQuality(frame.getAnt(), )
/* 
try { 
   Method method = CrossValidation.getClass().getMethod("calculateRuleQuality", Ant.class, int.class); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/ 
} 

/** 
* 
* Method: ruleQualityChoice(int nTruePositive, int nFalsePositive, int nFalseNegative, int nTrueNegative, int nQualityChoice, int nParam) 
* 
*/ 
@Test
    public void testRuleQualityChoice() throws Exception {
      Assert.assertEquals(cv.ruleQualityChoice(5, 0, 177, 75, 1, 0), 0.0273224, 0.001);    //  ntp =5, nFP = 0, nFN = 177, nTN = 75, P = 182.0, N = 75.0
      Assert.assertEquals(cv.ruleQualityChoice(5, 0, 177, 75, 2, 0), 1, 0.001);
      Assert.assertEquals(cv.ruleQualityChoice(5, 0, 177, 75, 3, 0), 0.074074, 0.001);
      Assert.assertEquals(cv.ruleQualityChoice(5, 0, 177, 75, 4, 0), 0.31007, 0.01);  //80/
      Assert.assertEquals(cv.ruleQualityChoice(5, 0, 177, 75, 5, 0), -1.0126776, 0.1);
      Assert.assertEquals(cv.ruleQualityChoice(5, 0, 177, 75, 6, 0), 0.3217, 0.1);    //83/
      Assert.assertEquals(cv.ruleQualityChoice(5, 0, 177, 75, 7, 0), 1.0193798, 0.001); //0.71428571+
      Assert.assertEquals(cv.ruleQualityChoice(5, 0, 177, 75, 8, 0), 1, 0.001);
      Assert.assertEquals(cv.ruleQualityChoice(5, 0, 177, 75, 9, 0), -0.962, 0.1);
      Assert.assertEquals(cv.ruleQualityChoice(5, 0, 177, 75, 10, 1), 5, 0.01);
      Assert.assertEquals(cv.ruleQualityChoice(5, 0, 177, 75, 11, 1), 0.0274725, 0.001);
      Assert.assertEquals(cv.ruleQualityChoice(5, 0, 177, 75, 12, 1), 0.053191481, 0.001);
      Assert.assertEquals(cv.ruleQualityChoice(5, 0, 177, 75, 13, 1), 0.05245712, 0.001);
      Assert.assertEquals(cv.ruleQualityChoice(5, 0, 177, 75, 14, 1), -0.01256, 0.001);
      Assert.assertEquals(cv.ruleQualityChoice(5, 0, 177, 75, 15, 1), 0.027472527, 0.001);

}

/** 
* 
* Method: updateInstancesIndexList(Ant ant) 
* 
*/ 
@Test
public void testRuleRefinementChoice() throws Exception {
    Assert.assertEquals(cv.ruleRefinementChoice(5, 75, 182, 75, 1, 0), 0, 0.001);
    Assert.assertEquals(cv.ruleRefinementChoice(5, 75, 182, 75, 2, 0), 0.00558659, 0.001);
    Assert.assertEquals(cv.ruleRefinementChoice(5, 75, 182, 75, 3, 1), 0.00397848, 0.001);
} 

/** 
* 
* Method: pruneRule(Ant ant) 
* 
*/ 
@Test
public void testPruneRule() throws Exception { 
//TODO: Test goes here... 
/* 
try { 
   Method method = CrossValidation.getClass().getMethod("pruneRule", Ant.class); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/ 
} 

/** 
* 
* Method: updatePheromone(Ant ant) 
* 
*/ 
@Test
public void testUpdatePheromone() throws Exception { 
//TODO: Test goes here... 
/* 
try { 
   Method method = CrossValidation.getClass().getMethod("updatePheromone", Ant.class); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/ 
} 


} 
