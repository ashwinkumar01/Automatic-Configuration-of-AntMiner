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
@Before
public void before() throws Exception {
    frame = new GUIAntMinerJFrame();
    frame.setVisible(true);
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
     CrossValidation cv = frame.getThread();
    cv.calculateRuleQuality(frame.getAnt(), )
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
//TODO: Test goes here... 
/* 
try { 
   Method method = CrossValidation.getClass().getMethod("ruleQualityChoice", int.class, int.class, int.class, int.class, int.class, int.class); 
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
* Method: updateInstancesIndexList(Ant ant) 
* 
*/ 
@Test
public void testUpdateInstancesIndexList() throws Exception { 
//TODO: Test goes here... 
/* 
try { 
   Method method = CrossValidation.getClass().getMethod("updateInstancesIndexList", Ant.class); 
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
