import org.junit.Assert;
import org.junit.Test;
import org.junit.Before; 
import org.junit.After;

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
*  2 different values
 *
*/ 
@Test
    public void testRuleQualityChoice() throws Exception {
      Assert.assertEquals(cv.ruleQualityChoice(5, 0, 177, 75, 1, 0), 0.0273224, 0.001);    //  ntp =5, nFP = 0, nFN = 177, nTN = 75, P = 182.0, N = 75.0
      Assert.assertEquals(cv.ruleQualityChoice(5, 80, 3, 75, 1, 0), 0.3024193, 0.001);
      Assert.assertEquals(cv.ruleQualityChoice(90, 80, 3, 75, 1, 0), 0.46826, 0.001);

      Assert.assertEquals(cv.ruleQualityChoice(5, 0, 177, 75, 2, 0), 0.0625, 0.001);
      Assert.assertEquals(cv.ruleQualityChoice(55, 90, 2, 40, 2, 0), 0.578947, 0.001);
      Assert.assertEquals(cv.ruleQualityChoice(80, 0, 177, 75, 2, 0), 0.516129, 0.001);

      Assert.assertEquals(cv.ruleQualityChoice(5, 0, 177, 75, 3, 0), 0.074074, 0.001);
      Assert.assertEquals(cv.ruleQualityChoice(5, 0, 177, 75, 4, 0), 374, 0.01);
      Assert.assertEquals(cv.ruleQualityChoice(55, 2, 27, 75, 4, 0), 278, 0.01);


      Assert.assertEquals(cv.ruleQualityChoice(5, 0, 177, 75, 5, 0), -0.9725274, 0.001);
      Assert.assertEquals(cv.ruleQualityChoice(5, 0, 177, 75, 6, 0), 0.01945525, 0.001);
      Assert.assertEquals(cv.ruleQualityChoice(55, 20, 57, 35, 6, 0), 0.329341, 0.001);

      Assert.assertEquals(cv.ruleQualityChoice(5, 0, 177, 75, 7, 0), -70, 0.001); //0.71428571+
      Assert.assertEquals(cv.ruleQualityChoice(5, 0, 177, 75, 8, 0), -0.95485, 0.001);
      Assert.assertEquals(cv.ruleQualityChoice(55, 20, 57, 35, 8, 0), -0.1369828, 0.001);

      Assert.assertEquals(cv.ruleQualityChoice(5, 0, 177, 75, 9, 0), -75, 0.001);

      Assert.assertEquals(cv.ruleQualityChoice(5, 0, 177, 75, 10, 1), 0.027475, 0.01);
      Assert.assertEquals(cv.ruleQualityChoice(5, 0, 177, 75, 11, 1), 0.311284, 0.001);
      Assert.assertEquals(cv.ruleQualityChoice(5, 0, 177, 75, 12, 1), 0.0381679, 0.001);
      Assert.assertEquals(cv.ruleQualityChoice(55, 20, 57, 35, 12, 0.2),0.605419136, 0.001);

      Assert.assertEquals(cv.ruleQualityChoice(5, 0, 177, 75, 13, 1), 0.0704712, 0.001);
      Assert.assertEquals(cv.ruleQualityChoice(5, 0, 177, 75, 14, 1), -0.2009871, 0.001);
      Assert.assertEquals(cv.ruleQualityChoice(55, 20, 57, 35, 14, 0.2),-0.0526222, 0.001);

      Assert.assertEquals(cv.ruleQualityChoice(5, 0, 177, 75, 15, 1), 0.027472527, 0.001);
      Assert.assertEquals(cv.ruleQualityChoice(55, 20, 57, 35, 15, 0.2),0.416666666, 0.001);

}

/** 
* 
* Method: updateInstancesIndexList(Ant ant) 
* 
*/ 
@Test
public void testRuleRefinementChoice() throws Exception {
    //int nTruePositive, int nTrueNegative, int P, int N, int choice, double nParam
    //(N - nTrueNegative) / ((P + N) - (nTruePositive + nTrueNegative
    Assert.assertEquals(cv.ruleRefinementChoice(5, 75, 182, 75, 1, 0), 0, 0.001);
    Assert.assertEquals(cv.ruleRefinementChoice(5, 75, 182, 90, 1, 1), 0.078125, 0.001);
    Assert.assertEquals(cv.ruleRefinementChoice(5, 75, 60, 90, 1, 1), 0.214285, 0.001);

    Assert.assertEquals(cv.ruleRefinementChoice(5, 75, 182, 75, 2, 0), 0.00558659, 0.001);
    Assert.assertEquals(cv.ruleRefinementChoice(5, 75, 182, 90, 2, 1), 0.082474226, 0.001);
    Assert.assertEquals(cv.ruleRefinementChoice(5, 75, 60, 90, 1, 1), 0.214285, 0.001);

    Assert.assertEquals(cv.ruleRefinementChoice(5, 75, 182, 90, 3, 1), 0.055470893, 0.001);
    Assert.assertEquals(cv.ruleRefinementChoice(5, 75, 182, 75, 3, 0.5), 0.001994848, 0.001);
    Assert.assertEquals(cv.ruleRefinementChoice(5, 75, 60, 90, 3, 0), 0.08571428, 0.001);
} 


} 
